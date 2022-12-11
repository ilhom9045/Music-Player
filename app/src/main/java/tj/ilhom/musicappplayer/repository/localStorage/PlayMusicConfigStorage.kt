package tj.ilhom.musicappplayer.repository.localStorage

import android.content.SharedPreferences
import tj.ilhom.musicappplayer.repository.JsonParserRepository
import tj.ilhom.musicappplayer.repository.localStorage.core.AbstractLocalData
import javax.inject.Inject

class PlayMusicConfigStorage @Inject constructor(
    sharedPreferences: SharedPreferences,jsonParserRepository: JsonParserRepository
) : AbstractLocalData(sharedPreferences,jsonParserRepository)