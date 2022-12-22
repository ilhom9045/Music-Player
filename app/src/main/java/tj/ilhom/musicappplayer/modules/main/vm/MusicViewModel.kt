package tj.ilhom.musicappplayer.modules.main.vm

import android.graphics.drawable.Drawable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import tj.ilhom.musicappplayer.R
import tj.ilhom.musicappplayer.core.utils.SingleLiveEvent
import tj.ilhom.musicappplayer.core.vm.BaseViewModel
import tj.ilhom.musicappplayer.extention.await
import tj.ilhom.musicappplayer.extention.toLatestMusicModel
import tj.ilhom.musicappplayer.modules.main.model.MusicItemDTO
import tj.ilhom.musicappplayer.repository.Observable
import tj.ilhom.musicappplayer.repository.TimerRepository
import tj.ilhom.musicappplayer.repository.localStorage.MutableMusicConfig
import tj.ilhom.musicappplayer.repository.localStorage.latestMusic.MutableLatestMusic
import tj.ilhom.musicappplayer.repository.localStorage.latestMusic.ReadLatestMusic
import tj.ilhom.musicappplayer.repository.musicrepository.ReadAllMusicRepository
import tj.ilhom.musicappplayer.repository.musicrepository.ReadMedia
import tj.ilhom.musicappplayer.repository.room.dao.MusicDao
import tj.ilhom.musicappplayer.service.MusicNotificationBroadcast
import tj.ilhom.musicappplayer.service.MusicPlayerUtil
import tj.ilhom.musicappplayer.service.NotificationUtil
import tj.ilhom.musicappplayer.service.model.MusicItem
import java.io.File
import javax.inject.Inject

@HiltViewModel
class MusicViewModel @Inject constructor(
    private val musicDao: MusicDao,
    private val readMediaIcon: ReadMedia,
    private val readAllMusicRepository: ReadAllMusicRepository,
    private val timerRepository: TimerRepository,
    private val musicPlayerUtil: MusicPlayerUtil,
    private val musicConfig: MutableMusicConfig,
    private val mutableLatestMusic: MutableLatestMusic,
    private val notificationUtil: NotificationUtil
) : BaseViewModel(), MusicNotificationBroadcast.MusicNotificationListener {

    private val _changeMusicConfig = MutableLiveData<Int>()
    val changeMusicConfig: LiveData<Int> = _changeMusicConfig

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private val _music = MutableLiveData<List<MusicItemDTO>>()
    val musics: LiveData<List<MusicItemDTO>> = _music

    private val _musicName = MutableLiveData<String>()
    val musicName: LiveData<String> = _musicName

    private val _musicDuration = MutableLiveData<String>()
    val musicDuration: LiveData<String> = _musicDuration

    private val _musicCurrentTime = MutableLiveData<String>()
    val musicCurrentTime: LiveData<String> = _musicCurrentTime

    private val _musicIcon = MutableLiveData<Drawable?>()
    val musicIcon: LiveData<Drawable?> = _musicIcon

    private val _playMusicIcon = MutableLiveData<Int>()
    val playMusicIcon: LiveData<Int> = _playMusicIcon

    private val _latestMusic = SingleLiveEvent<MusicItem>()
    val latestMusic: LiveData<MusicItem> = _latestMusic

    private val _exit = MutableSharedFlow<Boolean>()
    val exit = _exit.asSharedFlow()

    private val _playMusic = SingleLiveEvent<MusicItem>()
    val playMusic: LiveData<MusicItem> = _playMusic

    private val _search = SingleLiveEvent<String>()
    val search: LiveData<String> = _search

    init {
        initMusicControllerIcon()
        readLatestMusic()
    }

    private fun readLatestMusic() {
        notificationUtil.notificationModel()?.let {
            showMusicData(it)
            return
        }
        viewModelScope.launch(Dispatchers.IO) {
            val data = mutableLatestMusic.read().await()
            data?.let {

                if (!File(it.musicPath).exists()) return@launch

                launch(Dispatchers.Main) {
                    val model = MusicItem(
                        true,
                        it.id,
                        it.musicPath,
                        it.title,
                        it.artist,
                        it.duration
                    )
                    showMusicData(model)
                    _playMusicIcon.value = R.drawable.play_icon
                    _latestMusic.postValue(model)
                    delay(500)
                    changeMusicPosition(it.stopedPosition)
                    _musicCurrentTime.postValue(it.stopedPosition.toString())
                }
            }
        }
    }

    fun readMusic() {
        viewModelScope.launch(Dispatchers.IO) {
            val result = readAllMusicRepository.getAllMusics()
            _music.postValue(result)
            musicDao.deleteAll()
            musicDao.insertAll(result)
        }
    }

    fun getAllMusics() {
        _loading.postValue(true)
        viewModelScope.launch(Dispatchers.IO) {
            readMusic()
            _music.postValue(musicDao.getAll())
            _loading.postValue(false)
        }
    }

    private var job: Job? = null

    fun showMusicData(item: MusicItem) {
        viewModelScope.launch {
            val file = File(item.musicPath)
            if (!file.exists()) {
                musicDao.delete(
                    MusicItemDTO(
                        item.id,
                        item.musicPath,
                        item.duration,
                        item.title,
                        item.artist
                    )
                )
                getAllMusics()
                return@launch
            }
            _musicDuration.postValue(timerRepository.timeToDuration(item.duration).toString())
            _musicIcon.postValue(readMediaIcon.readIcon(item.musicPath))
            _musicName.postValue(item.title)
            if (item.isPlay && !musicPlayerUtil.player().isPlaying) {
                play()
            } else {
                pause()
            }
            job?.let {
                job = null
            }
            job = timerRepository.downTimer(
                viewModelScope,
                timerRepository.timeToDuration(item.duration),
                1000L,
                {
                    _musicCurrentTime.postValue(musicPlayerUtil.player().currentPosition.toString())
                }) {
                job?.cancel()
                job = null
            }
            _playMusic.postValue(item)
            mutableLatestMusic.save(item.toLatestMusicModel(0))
        }

    }

    override fun previous(item: MusicItem) {
        showMusicData(item)
    }

    override fun play() {
        _playMusicIcon.postValue(R.drawable.pause_icon)
    }

    override fun pause() {
        _playMusicIcon.postValue(R.drawable.play_icon)
    }

    override fun next(item: MusicItem) {
        showMusicData(item)
    }

    override fun exit() {
        viewModelScope.launch {
            _exit.emit(true)
        }
    }

    fun changeMusicPosition(progress: Int) {
        musicPlayerUtil.onSeekTo(progress)
    }

    fun changeMusicControl() {
        viewModelScope.launch {
            val localMusicConfig =
                musicConfig.read(MutableMusicConfig.MusicConfig.Replay()).first()
            val changeToConfig = when (localMusicConfig) {
                is MutableMusicConfig.MusicConfig.PlayOnes -> {
                    MutableMusicConfig.MusicConfig.Replay()
                }

                is MutableMusicConfig.MusicConfig.Replay -> {
                    MutableMusicConfig.MusicConfig.Random()
                }

                is MutableMusicConfig.MusicConfig.Random -> {
                    MutableMusicConfig.MusicConfig.PlayOnes()
                }
            }
            musicConfig.save(changeToConfig)
            initMusicControllerIcon()
        }
    }

    private fun initMusicControllerIcon() {
        viewModelScope.launch(Dispatchers.IO) {
            when (musicConfig.read(MutableMusicConfig.MusicConfig.Replay()).first()) {

                is MutableMusicConfig.MusicConfig.PlayOnes -> {
                    _changeMusicConfig.postValue(R.drawable.ic_baseline_repeat_one_24)
                }

                is MutableMusicConfig.MusicConfig.Replay -> {
                    _changeMusicConfig.postValue(R.drawable.ic_baseline_repeat_24)
                }

                is MutableMusicConfig.MusicConfig.Random -> {
                    _changeMusicConfig.postValue(R.drawable.ic_random)
                }
            }
        }
    }


    private var searchJob: Job? = null

    fun findMusic(query: String) {

        searchJob?.cancel()

        searchJob = viewModelScope.launch {
            delay(700)
            _search.postValue(query)
            job = null
        }
    }
}