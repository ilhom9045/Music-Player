package tj.ilhom.musicappplayer.modules.main.view.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import tj.ilhom.musicappplayer.R
import tj.ilhom.musicappplayer.core.view.BaseBottomSheetDialogFragment
import tj.ilhom.musicappplayer.extention.findViewById
import tj.ilhom.musicappplayer.extention.playMusic
import tj.ilhom.musicappplayer.extention.toMusicItem
import tj.ilhom.musicappplayer.modules.main.adapter.MusicAdapter
import tj.ilhom.musicappplayer.modules.main.callback.OnMusicAdapterItemClickListener
import tj.ilhom.musicappplayer.modules.main.model.MusicItemDTO
import tj.ilhom.musicappplayer.modules.main.vm.MusicViewModel
import kotlin.properties.Delegates.notNull

class MusicListBottomSheet : BaseBottomSheetDialogFragment(
    R.layout.music_list_bottom_sheet,
    false
), OnMusicAdapterItemClickListener {

    private val viewModel: MusicViewModel by activityViewModels()

    private var recyclerView: RecyclerView by notNull()
    private var button: MaterialButton by notNull()
    private lateinit var recyclerViewAdapter: MusicAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initView()
        initListener()
        initObservers()
    }

    private fun initObservers() {
        viewModel.musics.observe(viewLifecycleOwner) {
            recyclerViewAdapter.addData(it)
        }

        viewModel.playMusic.observe(viewLifecycleOwner) {
            requireContext().playMusic(it)
        }
    }

    private fun initListener() {
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        recyclerView.adapter = recyclerViewAdapter

        recyclerViewAdapter.musicAdapterItemClickListener = this

        button.setOnClickListener {
            dismiss()
        }
    }

    private fun initView() {
        recyclerViewAdapter = MusicAdapter(lifecycleScope)
        recyclerView = findViewById(R.id.music_recyclerview)
        button = findViewById(R.id.button)
    }

    override fun onItemClicked(item: MusicItemDTO) {
        viewModel.showMusicData(item.toMusicItem())
    }
}