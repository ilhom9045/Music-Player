package tj.ilhom.musicappplayer.modules

import android.os.Bundle
import dagger.hilt.android.AndroidEntryPoint
import tj.ilhom.musicappplayer.R
import tj.ilhom.musicappplayer.core.view.BaseActivity
import tj.ilhom.musicappplayer.extention.transaction
import tj.ilhom.musicappplayer.modules.splash.view.SplashScreenFragment

@AndroidEntryPoint
class StartActivity : BaseActivity(R.layout.activity_main) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            transaction(R.id.start_fragment_container, SplashScreenFragment())
        }
    }

}