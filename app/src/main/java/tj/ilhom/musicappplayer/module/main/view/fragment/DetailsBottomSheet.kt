package tj.ilhom.musicappplayer.module.main.view.fragment

import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.SeekBar
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.activityViewModels
import dagger.hilt.android.AndroidEntryPoint
import tj.ilhom.musicappplayer.R
import tj.ilhom.musicappplayer.core.extention.durationToTime
import tj.ilhom.musicappplayer.core.extention.timeToDuration
import tj.ilhom.musicappplayer.core.view.BaseBottomSheetDialogFragment
import tj.ilhom.musicappplayer.extention.findViewById
import tj.ilhom.musicappplayer.extention.nextMusic
import tj.ilhom.musicappplayer.extention.playMusic
import tj.ilhom.musicappplayer.extention.previousMusic
import tj.ilhom.musicappplayer.module.main.vm.MusicViewModel
import tj.ilhom.musicappplayer.repository.musicrepository.ResManager
import tj.ilhom.musicappplayer.service.MusicManager
import tj.ilhom.musicappplayer.service.MusicNotificationBroadcast
import javax.inject.Inject
import kotlin.properties.Delegates.notNull

@AndroidEntryPoint
class DetailsBottomSheet : BaseBottomSheetDialogFragment(
    layout = R.layout.details_bottom_sheet,
    mtheme = R.style.AppBottomSheetDialogTheme,
    fullScreen = true
), SeekBar.OnSeekBarChangeListener {

    private val viewModel: MusicViewModel by activityViewModels()

    @Inject
    lateinit var resManager: ResManager

    private var musicIcon: ImageView by notNull()
    private var seekBar: SeekBar by notNull()
    private var replayImageView: ImageView by notNull()
    private var previousImageVew: ImageView by notNull()
    private var playImageView: ImageView by notNull()
    private var nextImageVew: ImageView by notNull()
    private var musicsImageVew: ImageView by notNull()
    private var cardView: CardView by notNull()
    private var music_name: TextView by notNull()
    private var startTimeTextView: TextView by notNull()
    private var endTimeTextView: TextView by notNull()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initViews()
        initListener()
        initViewModel()
    }

    private fun initViewModel() {
        viewModel.musicIcon.observe(viewLifecycleOwner) {
            musicIcon.setImageDrawable(it ?: resManager.drawable(R.drawable.music_icon))
        }

        viewModel.musicName.observe(viewLifecycleOwner) {
            music_name.text = it
        }

        viewModel.playMusicIcon.observe(viewLifecycleOwner) {
            resManager.drawable(it)?.let {
                playImageView.setImageDrawable(it)
            }
        }

        viewModel.musicDuration.observe(viewLifecycleOwner) {
            seekBar.max = it.toInt()
            endTimeTextView.text = it.durationToTime()
        }

        viewModel.musicCurrentTime.observe(viewLifecycleOwner) {
            startTimeTextView.text = it.durationToTime()
            seekBar.progress = it.toInt()
        }

    }

    private fun initListener() {
        music_name.isSelected = true

        val width = (requireActivity().window.decorView.rootView.width * 0.7).toInt()

        val layoutParams = LinearLayout.LayoutParams(width, width)
        layoutParams.gravity = Gravity.CENTER
        cardView.layoutParams = layoutParams

        previousImageVew.setOnClickListener {
            requireContext().previousMusic()
        }

        playImageView.setOnClickListener {
            requireContext().playMusic()
        }

        nextImageVew.setOnClickListener {
            requireContext().nextMusic()
        }

        musicsImageVew.setOnClickListener {
            MusicListBottomSheet().show(childFragmentManager, this::class.java.simpleName)
        }

        seekBar.setOnSeekBarChangeListener(this)
    }

    private fun initViews() {
        cardView = findViewById(R.id.cardView)
        musicIcon = findViewById(R.id.musicIconImageView)
        replayImageView = findViewById(R.id.replayImageView)
        previousImageVew = findViewById(R.id.previousImageVew)
        playImageView = findViewById(R.id.playImageView)
        nextImageVew = findViewById(R.id.nextImageVew)
        musicsImageVew = findViewById(R.id.musicsImageVew)
        seekBar = findViewById(R.id.seekBar)
        music_name = findViewById(R.id.music_name)
        endTimeTextView = findViewById(R.id.endTimeTextView)
        startTimeTextView = findViewById(R.id.startTimeTextView)
    }

    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {}

    override fun onStartTrackingTouch(p0: SeekBar?) {}

    override fun onStopTrackingTouch(seekBar: SeekBar?) {
        seekBar?.progress?.let {
            viewModel.changeMusicPosition(it)
            startTimeTextView.text = it.toString().durationToTime()
        }
    }
}