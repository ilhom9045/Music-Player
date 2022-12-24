package tj.ilhom.musicappplayer.modules.main.view.fragment

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
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ilhom.core_ui.extention.*
import com.ilhom.equalizer.view.EqualizerFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import tj.ilhom.musicappplayer.R
import tj.ilhom.musicappplayer.extention.*
import tj.ilhom.musicappplayer.modules.main.adapter.MusicAdapter
import tj.ilhom.musicappplayer.modules.main.callback.OnMusicAdapterItemClickListener
import tj.ilhom.musicappplayer.modules.main.model.MusicItemDTO
import tj.ilhom.musicappplayer.modules.main.vm.MusicViewModel
import tj.ilhom.musicappplayer.service.MusicNotificationBroadcast.Companion.musicNotificationListener
import javax.inject.Inject

@AndroidEntryPoint
class MusicFragment : com.ilhom.core_ui.view.BaseFragment(R.layout.fragment_music), OnMusicAdapterItemClickListener {

    private val viewmodel: MusicViewModel by activityViewModels()

    @Inject
    lateinit var resManager: com.ilhom.core.musicrepository.ResManager

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
    private lateinit var equalizerCardView: CardView

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
        musicNotificationListener = null
        super.onDestroy()
    }

    private fun viewmodel(savedInstanceState: Bundle?) {

        viewmodel.search.observe(viewLifecycleOwner) {
            recyclerViewAdapter.filter(it)
        }

        viewmodel.musics.observe(viewLifecycleOwner) {
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
            bottom_linearLayout.isVisible = true
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

        viewmodel.playMusic.observe(viewLifecycleOwner) {
            requireContext().playMusic(it)
        }

        viewmodel.latestMusic.observe(viewLifecycleOwner) {
            requireContext().playMusic(it)
            requireContext().playMusic()
        }

        viewmodel.loading.observe(viewLifecycleOwner) {
            progress.isVisible = it
            recyclerView.isVisible = !it
        }

        if (savedInstanceState == null) {
            viewmodel.getAllMusics()
        }
    }

    private fun initListener() {

        equalizerCardView.setOnClickListener {
            val fragment = EqualizerFragment.newInstance(viewmodel.musicSessionId())
            transaction(R.id.start_fragment_container, fragment, addToBackStack = true)
        }

        bottom_linearLayout.setOnClickListener {
            DetailsBottomSheet().show(childFragmentManager, this::class.java.simpleName)
        }

        music_name.isSelected = true

        nextCardView.setOnClickListener {
            requireContext().nextMusic()
        }

        playImageView.setOnClickListener {
            requireContext().playMusic()
        }

        clearImageView.setOnClickListener {
            search_edittext.setText("")
        }

        search_edittext.addTextChangedListener {
            clearImageView.isVisible = !it.isNullOrEmpty()
            viewmodel.findMusic(it.toString())
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
        equalizerCardView = findViewById(R.id.equalizerCardView)
    }

    override fun onItemClicked(item: MusicItemDTO) {
        viewmodel.showMusicData(item.toMusicItem())
    }
}