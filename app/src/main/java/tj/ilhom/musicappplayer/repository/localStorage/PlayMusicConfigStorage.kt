package tj.ilhom.musicappplayer.repository.localStorage

import android.content.SharedPreferences
import tj.ilhom.musicappplayer.repository.localStorage.core.AbstractLocalData

class PlayMusicConfigStorage(sharedPreferences: SharedPreferences) :
    AbstractLocalData(sharedPreferences)