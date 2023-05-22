package com.dongbin.popple.ui.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import com.dongbin.popple.R
import com.dongbin.popple.databinding.ActivitySplashBinding
import com.dongbin.popple.ui.login.LoginActivity
import com.dongbin.popple.ui.main.MainActivity

class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding
    lateinit var animFadeIn: Animation

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )

        animFadeIn = AnimationUtils.loadAnimation(applicationContext, R.anim.anim_splash_fadein).apply {
            setAnimationListener(object: Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation?) {
                }

                override fun onAnimationEnd(animation: Animation?) {
                    Intent(this@SplashActivity, LoginActivity::class.java).run { startActivity(this) }
                }

                override fun onAnimationRepeat(animation: Animation?) {
                }

            })
        }
        binding.tvSplashTitle.startAnimation(animFadeIn)
    }
}