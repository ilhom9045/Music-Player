package tj.ilhom.musicappplayer.modules.splash.view

import android.animation.Animator
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.airbnb.lottie.LottieAnimationView
import tj.ilhom.musicappplayer.R
import com.ilhom.core_ui.extention.findViewById
import com.ilhom.core_ui.extention.transaction
import com.ilhom.core_ui.extention.transparentToolbar
import tj.ilhom.musicappplayer.modules.main.view.PermissionFragment

class SplashScreenFragment : com.ilhom.core_ui.view.BaseFragment(R.layout.activity_splash_screen) {

    private lateinit var fullscreenSpash: LottieAnimationView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as AppCompatActivity).transparentToolbar()

        fullscreenSpash = findViewById(R.id.splash)
        fullscreenSpash.addAnimatorListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator?) {}

            override fun onAnimationEnd(animation: Animator?) {
                transaction(R.id.start_fragment_container, PermissionFragment())
            }

            override fun onAnimationCancel(animation: Animator?) {}

            override fun onAnimationRepeat(animation: Animator?) {}
        })
    }
}