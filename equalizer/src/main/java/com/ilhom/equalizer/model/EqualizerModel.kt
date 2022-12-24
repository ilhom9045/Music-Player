package com.ilhom.equalizer.model

import java.io.Serializable

class EqualizerModel : Serializable {
    var isEqualizerEnabled = true
    var seekbarpos = IntArray(5)
    var presetPos = 0
    var reverbPreset: Short = -1
    var bassStrength: Short = -1
}