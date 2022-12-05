package tj.ilhom.musicappplayer.module.splash.view

import android.animation.Animator
import android.content.Intent
import android.os.Bundle
import com.airbnb.lottie.LottieAnimationView
import tj.ilhom.musicappplayer.R
import tj.ilhom.musicappplayer.core.BaseActivity
import tj.ilhom.musicappplayer.extention.transparentToolbar
import tj.ilhom.musicappplayer.module.main.view.MainActivity

class SplashScreenActivity : BaseActivity(R.layout.activity_splash_screen) {

    private lateinit var fullscreenSpash: LottieAnimationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        transparentToolbar()

        fullscreenSpash = findViewById(R.id.splash)
        fullscreenSpash.addAnimatorListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator?) {}

            override fun onAnimationEnd(animation: Animator?) {
                startActivity(Intent(this@SplashScreenActivity, MainActivity::class.java))
                finish()
            }

            override fun onAnimationCancel(animation: Animator?) {}

            override fun onAnimationRepeat(animation: Animator?) {}
        })
    }
}