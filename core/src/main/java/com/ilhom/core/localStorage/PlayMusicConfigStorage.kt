package com.ilhom.core.localStorage

import android.content.SharedPreferences
import com.ilhom.core.JsonParserRepository
import com.ilhom.core.localStorage.core.AbstractLocalData
import javax.inject.Inject

class PlayMusicConfigStorage @Inject constructor(
    sharedPreferences: SharedPreferences,jsonParserRepository: JsonParserRepository
) : AbstractLocalData(sharedPreferences,jsonParserRepository)