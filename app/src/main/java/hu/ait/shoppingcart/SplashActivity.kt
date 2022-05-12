package hu.ait.shoppingcart

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import hu.ait.shoppingcart.databinding.ActivitySplashBinding


class SplashActivity : AppCompatActivity() {
    lateinit var binding: ActivitySplashBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val anim = AnimationUtils.loadAnimation(this, R.anim.shopping_anim)
        binding.ivShopping.startAnimation(anim)
        anim.setAnimationListener(
            object: Animation.AnimationListener {
                override fun onAnimationStart(p0: Animation?) {
                }

                override fun onAnimationEnd(p0: Animation?) {
                    startActivity(
                        Intent(
                            this@SplashActivity,
                            LoginActivity::class.java)
                    )
                    finish()
                }

                override fun onAnimationRepeat(p0: Animation?) {
                }
            }
        )
    }
}