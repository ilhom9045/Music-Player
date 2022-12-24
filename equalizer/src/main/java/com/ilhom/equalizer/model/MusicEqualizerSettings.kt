package com.ilhom.equalizer.model

import com.ilhom.equalizer.model.EqualizerModel


object MusicEqualizerSettings {
    var isEqualizerEnabled = true
    var isEqualizerReloaded = true
    var seekbarpos = IntArray(5)
    var presetPos = 0
    var reverbPreset: Short = -1
    var bassStrength: Short = -1
    var equalizerModel: EqualizerModel? = null
    var ratio = 1.0
    var isEditing = false
}