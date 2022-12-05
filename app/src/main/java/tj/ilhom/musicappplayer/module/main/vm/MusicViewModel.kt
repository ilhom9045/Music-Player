package tj.ilhom.musicappplayer.module.main.vm

import android.graphics.drawable.Drawable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import tj.ilhom.musicappplayer.R
import tj.ilhom.musicappplayer.core.BaseViewModel
import tj.ilhom.musicappplayer.module.main.model.MusicItemDTO
import tj.ilhom.musicappplayer.repository.musicrepository.ReadAllMusicRepository
import tj.ilhom.musicappplayer.repository.musicrepository.ReadMediaIcon
import tj.ilhom.musicappplayer.repository.musicrepository.ResManager
import tj.ilhom.musicappplayer.repository.room.dao.MusicDao
import tj.ilhom.musicappplayer.service.MusicNotificationBroadcast
import tj.ilhom.musicappplayer.service.model.MusicItem
import javax.inject.Inject

@HiltViewModel
class MusicViewModel @Inject constructor(
    private val musicDao: MusicDao,
    private val readMediaIcon: ReadMediaIcon,
    private val readAllMusicRepository: ReadAllMusicRepository
) : BaseViewModel(), MusicNotificationBroadcast.MusicNotificationListener {

    private val _music = MutableLiveData<List<MusicItemDTO>>()
    val musics: LiveData<List<MusicItemDTO>> = _music

    private val _musicName = MutableLiveData<String>()
    val musicName: LiveData<String> = _musicName

    private val _musicIcon = MutableLiveData<Drawable?>()
    val musicIcon: LiveData<Drawable?> = _musicIcon

    private val _playMusicIcon = MutableLiveData<Int>()
    val playMusicIcon: LiveData<Int> = _playMusicIcon

    private val _exit = MutableSharedFlow<Boolean>()
    val exit = _exit.asSharedFlow()

    fun readMusic() {
        viewModelScope.launch(Dispatchers.IO) {
            val result = readAllMusicRepository.getAllMusics()
            _music.postValue(result)
            musicDao.deleteAll()
            musicDao.insertAll(result)
        }
    }

    fun getAllMusics() {
        viewModelScope.launch(Dispatchers.IO) {
            _music.postValue(musicDao.getAll())
        }
    }

    fun showMusicData(item: MusicItem) {
        _musicIcon.value = readMediaIcon.readIcon(item.musicPath)
        _musicName.value = item.title
        _playMusicIcon.value = R.drawable.pause_icon
    }

    override fun previous(item: MusicItem) {
        showMusicData(item)
    }

    override fun play() {
        _playMusicIcon.value = R.drawable.pause_icon
    }

    override fun pause() {
        _playMusicIcon.value = R.drawable.play_icon
    }

    override fun next(item: MusicItem) {
        showMusicData(item)
    }

    override fun exit() {
        viewModelScope.launch {
            _exit.emit(true)
        }
    }
}