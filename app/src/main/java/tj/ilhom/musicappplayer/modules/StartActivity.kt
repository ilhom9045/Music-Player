package tj.ilhom.musicappplayer.modules

import android.os.Bundle
import dagger.hilt.android.AndroidEntryPoint
import tj.ilhom.musicappplayer.R
import com.ilhom.core_ui.extention.transaction
import tj.ilhom.musicappplayer.modules.splash.view.SplashScreenFragment

@AndroidEntryPoint
class StartActivity : com.ilhom.core_ui.view.BaseActivity(R.layout.activity_main) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            transaction(R.id.start_fragment_container, SplashScreenFragment())
        }
    }

}