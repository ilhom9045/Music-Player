package com.ilhom.equalizer.model

data class EqualizerSettings(
    var seekbarpos: IntArray,
    var presetPos: Int = 0,
    var reverbPreset: Short = 0,
    var bassStrength: Short = 0
) : java.io.Serializable
