package com.ilhom.equalizer

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.media.audiofx.BassBoost
import android.media.audiofx.Equalizer
import android.media.audiofx.PresetReverb
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.widget.SeekBar.OnSeekBarChangeListener
import androidx.appcompat.widget.SwitchCompat
import androidx.fragment.app.Fragment
import com.db.chart.model.LineSet
import com.db.chart.view.AxisController
import com.db.chart.view.ChartView
import com.db.chart.view.LineChartView
import com.ilhom.equalizer.AnalogController.onProgressChangedListener
import kotlin.properties.Delegates.notNull


class EqualizerFragment : Fragment(R.layout.fragment_equalizer) {

    var mEqualizer: Equalizer? = null
    var equalizerSwitch: SwitchCompat by notNull()
    var bassBoost: BassBoost? = null
    var chart: LineChartView? = null
    var presetReverb: PresetReverb? = null
    var backBtn: ImageView by notNull()
    var y = 0
    var spinnerDropDownIcon: ImageView by notNull()
    var fragTitle: TextView? = null
    var mLinearLayout: LinearLayout? = null
    var seekBarFinal = arrayOfNulls<SeekBar>(5)
    var bassController: AnalogController by notNull()
    var reverbController: AnalogController by notNull()
    var presetSpinner: Spinner? = null
    var equalizerBlocker: FrameLayout? = null
    var dataset: LineSet? = null
    var paint: Paint? = null

    var points: FloatArray by notNull()

    var numberOfFrequencyBands: Short = 0
    private var audioSesionId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Settings.isEditing = true
        if (arguments != null && arguments?.containsKey(ARG_AUDIO_SESSIOIN_ID) == true) {
            audioSesionId = arguments?.getInt(ARG_AUDIO_SESSIOIN_ID)!!
        }
        if (Settings.equalizerModel == null) {
            Settings.equalizerModel = EqualizerModel()
            Settings.equalizerModel?.reverbPreset = (PresetReverb.PRESET_NONE)
            Settings.equalizerModel?.bassStrength = ((1000 / 19).toShort())
        }
        mEqualizer = Equalizer(0, audioSesionId)
        bassBoost = BassBoost(0, audioSesionId)
        bassBoost!!.enabled = Settings.isEqualizerEnabled
        val bassBoostSettingTemp = bassBoost!!.properties
        val bassBoostSetting = BassBoost.Settings(bassBoostSettingTemp.toString())
        bassBoostSetting.strength = Settings.equalizerModel?.bassStrength!!
        bassBoost!!.properties = bassBoostSetting
        presetReverb = PresetReverb(0, audioSesionId)
        presetReverb!!.preset = Settings.equalizerModel?.reverbPreset!!
        presetReverb!!.enabled = Settings.isEqualizerEnabled
        mEqualizer!!.enabled = Settings.isEqualizerEnabled
        if (Settings.presetPos === 0) {
            for (bandIdx in 0 until mEqualizer!!.numberOfBands) {
                mEqualizer!!.setBandLevel(bandIdx.toShort(), Settings.seekbarpos[bandIdx].toShort())
            }
        } else {
            mEqualizer!!.usePreset(Settings.presetPos.toShort())
        }
    }


    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        backBtn = view.findViewById(R.id.equalizer_back_btn)
        backBtn.setVisibility(if (showBackButton) View.VISIBLE else View.GONE)
        backBtn.setOnClickListener {
            activity?.onBackPressed()
        }
        fragTitle = view.findViewById(R.id.equalizer_fragment_title)
        equalizerSwitch = view.findViewById(R.id.equalizer_switch)
        equalizerSwitch.setChecked(Settings.isEqualizerEnabled)
        equalizerSwitch.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
            mEqualizer!!.enabled = isChecked
            bassBoost!!.enabled = isChecked
            presetReverb!!.enabled = isChecked
            Settings.isEqualizerEnabled = isChecked
            Settings.equalizerModel?.isEqualizerEnabled = (isChecked)
        })
        spinnerDropDownIcon = view.findViewById(R.id.spinner_dropdown_icon)
        spinnerDropDownIcon.setOnClickListener(View.OnClickListener { presetSpinner!!.performClick() })
        presetSpinner = view.findViewById(R.id.equalizer_preset_spinner)
        equalizerBlocker = view.findViewById(R.id.equalizerBlocker)
        chart = view.findViewById(R.id.lineChart)
        paint = Paint()
        dataset = LineSet()
        bassController = view.findViewById(R.id.controllerBass)
        reverbController = view.findViewById(R.id.controller3D)
        bassController.label = ("BASS")
        reverbController.label = ("3D")
        bassController.circlePaint2.color = themeColor
        bassController.linePaint.color = themeColor
        bassController.invalidate()
        reverbController.circlePaint2.color = themeColor
        bassController.linePaint.color = themeColor
        reverbController.invalidate()
        if (!Settings.isEqualizerReloaded) {
            var x = 0
            if (bassBoost != null) {
                try {
                    x = bassBoost!!.roundedStrength * 19 / 1000
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            if (presetReverb != null) {
                try {
                    y = presetReverb!!.preset * 19 / 6
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            if (x == 0) {
                bassController.progress = (1)
            } else {
                bassController.progress = (x)
            }
            if (y == 0) {
                reverbController.progress = (1)
            } else {
                reverbController.progress = (y)
            }
        } else {
            val x = Settings.bassStrength * 19 / 1000
            y = Settings.reverbPreset * 19 / 6
            if (x == 0) {
                bassController.progress = (1)
            } else {
                bassController.progress = (x)
            }
            if (y == 0) {
                reverbController.progress = (1)
            } else {
                reverbController.progress = (y)
            }
        }
        bassController.setOnProgressChangedListener(object : onProgressChangedListener {
            override fun onProgressChanged(progress: Int) {
                Settings.bassStrength = (1000f / 19 * progress).toInt().toShort()
                try {
                    bassBoost!!.setStrength(Settings.bassStrength)
                    Settings.equalizerModel?.bassStrength = (Settings.bassStrength)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        })
        reverbController.setOnProgressChangedListener(object : onProgressChangedListener {
            override fun onProgressChanged(progress: Int) {
                Settings.reverbPreset = (progress * 6 / 19).toShort()
                Settings.equalizerModel?.reverbPreset = (Settings.reverbPreset)
                try {
                    presetReverb!!.preset = Settings.reverbPreset
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                y = progress
            }
        })
        mLinearLayout = view.findViewById(R.id.equalizerContainer)
        val equalizerHeading = TextView(context)
        equalizerHeading.setText(R.string.eq)
        equalizerHeading.textSize = 20f
        equalizerHeading.gravity = Gravity.CENTER_HORIZONTAL
        numberOfFrequencyBands = 5
        points = FloatArray(numberOfFrequencyBands.toInt())
        val lowerEqualizerBandLevel = mEqualizer!!.bandLevelRange[0]
        val upperEqualizerBandLevel = mEqualizer!!.bandLevelRange[1]
        for (i in 0 until numberOfFrequencyBands) {
            val equalizerBandIndex = i.toShort()
            val frequencyHeaderTextView = TextView(context)
            frequencyHeaderTextView.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            frequencyHeaderTextView.gravity = Gravity.CENTER_HORIZONTAL
            frequencyHeaderTextView.setTextColor(Color.parseColor("#FFFFFF"))
            frequencyHeaderTextView.text =
                (mEqualizer!!.getCenterFreq(equalizerBandIndex) / 1000).toString() + "Hz"
            val seekBarRowLayout = LinearLayout(context)
            seekBarRowLayout.orientation = LinearLayout.VERTICAL
            val lowerEqualizerBandLevelTextView = TextView(context)
            lowerEqualizerBandLevelTextView.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            lowerEqualizerBandLevelTextView.setTextColor(Color.parseColor("#FFFFFF"))
            lowerEqualizerBandLevelTextView.text = (lowerEqualizerBandLevel / 100).toString() + "dB"
            val upperEqualizerBandLevelTextView = TextView(context)
            lowerEqualizerBandLevelTextView.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            upperEqualizerBandLevelTextView.setTextColor(Color.parseColor("#FFFFFF"))
            upperEqualizerBandLevelTextView.text = (upperEqualizerBandLevel / 100).toString() + "dB"
            val layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            layoutParams.weight = 1f
            var seekBar = SeekBar(context)
            var textView = TextView(context)
            when (i) {
                0 -> {
                    seekBar = view.findViewById(R.id.seekBar1)
                    textView = view.findViewById(R.id.textView1)
                }
                1 -> {
                    seekBar = view.findViewById(R.id.seekBar2)
                    textView = view.findViewById(R.id.textView2)
                }
                2 -> {
                    seekBar = view.findViewById(R.id.seekBar3)
                    textView = view.findViewById(R.id.textView3)
                }
                3 -> {
                    seekBar = view.findViewById(R.id.seekBar4)
                    textView = view.findViewById(R.id.textView4)
                }
                4 -> {
                    seekBar = view.findViewById(R.id.seekBar5)
                    textView = view.findViewById(R.id.textView5)
                }
            }
            seekBarFinal[i] = seekBar
            seekBar.progressDrawable.colorFilter =
                PorterDuffColorFilter(Color.DKGRAY, PorterDuff.Mode.SRC_IN)
            seekBar.thumb.colorFilter =
                PorterDuffColorFilter(themeColor, PorterDuff.Mode.SRC_IN)
            seekBar.id = i
            //            seekBar.setLayoutParams(layoutParams);
            seekBar.max = upperEqualizerBandLevel - lowerEqualizerBandLevel
            textView.text = frequencyHeaderTextView.text
            textView.setTextColor(Color.WHITE)
            textView.textAlignment = View.TEXT_ALIGNMENT_CENTER
            if (Settings.isEqualizerReloaded) {
                points[i] = (Settings.seekbarpos[i] - lowerEqualizerBandLevel).toFloat()
                dataset?.addPoint(frequencyHeaderTextView.text.toString(), points[i])
                seekBar.progress = Settings.seekbarpos[i] - lowerEqualizerBandLevel
            } else {
                points[i] =
                    (mEqualizer!!.getBandLevel(equalizerBandIndex) - lowerEqualizerBandLevel).toFloat()
                dataset?.addPoint(frequencyHeaderTextView.text.toString(), points[i])
                seekBar.progress =
                    mEqualizer!!.getBandLevel(equalizerBandIndex) - lowerEqualizerBandLevel
                Settings.seekbarpos[i] = mEqualizer!!.getBandLevel(equalizerBandIndex).toInt()
                Settings.isEqualizerReloaded = true
            }
            seekBar.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                    mEqualizer!!.setBandLevel(
                        equalizerBandIndex,
                        (progress + lowerEqualizerBandLevel).toShort()
                    )
                    points[seekBar.id] =
                        (mEqualizer!!.getBandLevel(equalizerBandIndex) - lowerEqualizerBandLevel).toFloat()
                    Settings.seekbarpos[seekBar.id] = progress + lowerEqualizerBandLevel
                    Settings.equalizerModel?.seekbarpos?.set(
                        seekBar.id,
                        progress + lowerEqualizerBandLevel
                    )
                    dataset?.updateValues(points)
                    chart?.notifyDataUpdate()
                }

                override fun onStartTrackingTouch(seekBar: SeekBar) {
                    presetSpinner?.setSelection(0)
                    Settings.presetPos = 0
                    Settings.equalizerModel?.presetPos = (0)
                }

                override fun onStopTrackingTouch(seekBar: SeekBar) {}
            })
        }
        equalizeSound()
        paint!!.color = Color.parseColor("#555555")
        paint!!.strokeWidth = (1.10 * Settings.ratio).toFloat()
        dataset?.setColor(themeColor)
        dataset?.setSmooth(true)
        dataset?.setThickness(5f)
        chart?.setXAxis(false)
        chart?.setYAxis(false)
        chart?.setYLabels(AxisController.LabelPosition.NONE)
        chart?.setXLabels(AxisController.LabelPosition.NONE)
        chart?.setGrid(ChartView.GridType.NONE, 7, 10, paint)
        chart?.setAxisBorderValues(-300, 3300)
        chart?.addData(dataset)
        chart?.show()
        val mEndButton = Button(context)
        mEndButton.setBackgroundColor(themeColor)
        mEndButton.setTextColor(Color.WHITE)
    }

    fun equalizeSound() {
        val equalizerPresetNames = ArrayList<String>()
        val equalizerPresetSpinnerAdapter = ArrayAdapter(
            requireContext(),
            R.layout.spinner_item,
            equalizerPresetNames
        )
        equalizerPresetSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        equalizerPresetNames.add("Custom")
        for (i in 0 until mEqualizer!!.numberOfPresets) {
            equalizerPresetNames.add(mEqualizer!!.getPresetName(i.toShort()))
        }
        presetSpinner!!.adapter = equalizerPresetSpinnerAdapter
        //presetSpinner.setDropDownWidth((Settings.screen_width * 3) / 4);
        if (Settings.isEqualizerReloaded && Settings.presetPos !== 0) {
//            correctPosition = false;
            presetSpinner!!.setSelection(Settings.presetPos)
        }
        presetSpinner!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View,
                position: Int,
                id: Long
            ) {
                try {
                    if (position != 0) {
                        mEqualizer!!.usePreset((position - 1).toShort())
                        Settings.presetPos = position
                        val numberOfFreqBands: Short = 5
                        val lowerEqualizerBandLevel = mEqualizer!!.bandLevelRange[0]
                        for (i in 0 until numberOfFreqBands) {
                            seekBarFinal[i]!!.progress =
                                mEqualizer!!.getBandLevel(i.toShort()) - lowerEqualizerBandLevel
                            points[i] =
                                (mEqualizer!!.getBandLevel(i.toShort()) - lowerEqualizerBandLevel).toFloat()
                            Settings.seekbarpos[i] =
                                mEqualizer!!.getBandLevel(i.toShort()).toInt()
                            Settings.equalizerModel?.seekbarpos?.set(
                                i, mEqualizer!!.getBandLevel(
                                    i.toShort()
                                ).toInt()
                            )
                        }
                        dataset?.updateValues(points)
                        chart?.notifyDataUpdate()
                    }
                } catch (e: Exception) {
                    Toast.makeText(
                        requireContext(),
                        "Error while updating Equalizer",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                Settings.equalizerModel?.presetPos = position
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (mEqualizer != null) {
            mEqualizer!!.release()
        }
        if (bassBoost != null) {
            bassBoost!!.release()
        }
        if (presetReverb != null) {
            presetReverb!!.release()
        }
        Settings.isEditing = false
    }

    class Builder {
        private var id = -1
        fun setAudioSessionId(id: Int): Builder {
            this.id = id
            return this
        }

        fun setAccentColor(color: Int): Builder {
            themeColor = color
            return this
        }

        fun setShowBackButton(show: Boolean): Builder {
            showBackButton = show
            return this
        }

        fun build(): EqualizerFragment {
            return newInstance(id)
        }
    }

    companion object {
        const val ARG_AUDIO_SESSIOIN_ID = "audio_session_id"
        var themeColor = Color.parseColor("#B24242")
        var showBackButton = true
        fun newInstance(audioSessionId: Int): EqualizerFragment {
            val args = Bundle()
            args.putInt(ARG_AUDIO_SESSIOIN_ID, audioSessionId)
            val fragment = EqualizerFragment()
            fragment.arguments = args
            return fragment
        }

        fun newBuilder(): Builder {
            return Builder()
        }
    }
}
