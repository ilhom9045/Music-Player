package tj.ilhom.musicappplayer.module.main.music

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import tj.ilhom.musicappplayer.R
import tj.ilhom.musicappplayer.core.BaseFragment
import tj.ilhom.musicappplayer.extention.*
import tj.ilhom.musicappplayer.module.main.adapter.MusicAdapter
import tj.ilhom.musicappplayer.module.main.callback.OnMusicAdapterItemClickListener
import tj.ilhom.musicappplayer.module.main.model.MusicItemDTO
import tj.ilhom.musicappplayer.module.main.vm.MusicViewModel
import tj.ilhom.musicappplayer.repository.musicrepository.ResManager
import tj.ilhom.musicappplayer.service.MusicNotificationBroadcast.Companion.musicNotificationListener
import tj.ilhom.musicappplayer.service.MusicService
import tj.ilhom.musicappplayer.service.NotificationUtil
import javax.inject.Inject

@AndroidEntryPoint
class MusicFragment : BaseFragment(R.layout.fragment_music), OnMusicAdapterItemClickListener {

    private val viewmodel: MusicViewModel by viewModels()

    @Inject
    lateinit var resManager: ResManager

    private lateinit var progress: ProgressBar
    private lateinit var recyclerView: RecyclerView
    private lateinit var recyclerViewAdapter: MusicAdapter
    private lateinit var search_edittext: EditText
    private lateinit var clearImageView: ImageView
    private lateinit var music_image: ImageView
    private lateinit var playImageView: ImageView
    private lateinit var music_name: TextView
    private lateinit var playCardView: CardView
    private lateinit var nextCardView: CardView
    private lateinit var bottom_linearLayout: LinearLayout

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        initListener()
        viewmodel(savedInstanceState)
    }

    override fun onStart() {
        super.onStart()
        musicNotificationListener = viewmodel
    }

    override fun onDestroy() {
        super.onDestroy()
        musicNotificationListener = null
    }

    private fun viewmodel(savedInstanceState: Bundle?) {

        viewmodel.musics.observe(viewLifecycleOwner) {
            progress.visibility = View.GONE
            recyclerView.visibility = View.VISIBLE
            if (!it.isNullOrEmpty()) {
                recyclerViewAdapter.addData(it)
            } else {
                showLongToast("Список пуст")
            }
        }

        viewmodel.musicIcon.observe(viewLifecycleOwner) {
            music_image.setImageDrawable(it ?: resManager.drawable(R.drawable.music_icon))
        }

        viewmodel.musicName.observe(viewLifecycleOwner) {
            music_name.text = it
        }

        viewmodel.playMusicIcon.observe(viewLifecycleOwner) {
            resManager.drawable(it)?.let {
                playImageView.setImageDrawable(it)
            }
        }

        lifecycleScope.launchWhenStarted {
            viewmodel.exit.collectLatest {
                if (it) {
                    requireActivity().finish()
                }
            }
        }

        if (savedInstanceState == null) {
            viewmodel.readMusic()
            viewmodel.getAllMusics()
        }
    }

    private fun initListener() {
        music_name.isSelected = true
        nextCardView.setOnClickListener {
            requireContext().sendBroadcast(requireContext().newActionIntent(NotificationUtil.NEXT))
        }
        playImageView.setOnClickListener {
            requireContext().sendBroadcast(requireContext().newActionIntent(NotificationUtil.PLAY))
        }
        clearImageView.setOnClickListener {
            search_edittext.setText("")
        }
        search_edittext.addTextChangedListener {
            clearImageView.isVisible = !it.isNullOrEmpty()
            recyclerViewAdapter.filter.filter(it)
        }
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = recyclerViewAdapter
        recyclerViewAdapter.musicAdapterItemClickListener = this
    }

    private fun initViews() {
        recyclerViewAdapter = MusicAdapter()
        progress = findViewById(R.id.progress_bar)
        recyclerView = findViewById(R.id.music_recyclerview)
        clearImageView = findViewById(R.id.clearImageView)
        search_edittext = findViewById(R.id.search_EditText)
        music_image = findViewById(R.id.music_image)
        music_name = findViewById(R.id.music_name)
        playCardView = findViewById(R.id.playCardView)
        nextCardView = findViewById(R.id.nextCardView)
        playImageView = findViewById(R.id.playImageView)
        bottom_linearLayout = findViewById(R.id.bottom_linearLayout)
    }

    override fun onItemClicked(item: MusicItemDTO) {

        val model = item.toMusicItem()
        bottom_linearLayout.isVisible = true
        viewmodel.showMusicData(model)

        val intent = Intent(requireContext(), MusicService::class.java)
        intent.putExtra(MusicService.NOTIFICATION_MODEL, model)
        requireContext().startService(intent)
    }

}