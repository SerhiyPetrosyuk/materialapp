package com.example.materialapp

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.airbnb.lottie.LottieCompositionFactory
import com.example.materialapp.databinding.ActivityLottieAnimationBinding

class LottieAnimationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityLottieAnimationBinding>(
            this,
            R.layout.activity_lottie_animation
        )

        val lottieCompositionTask =
            LottieCompositionFactory.fromRawRes(this, R.raw.native_lottie_animation)
                ?: throw IllegalArgumentException("Invalid composition animation")
        lottieCompositionTask.addFailureListener {
            Toast.makeText(this, it.localizedMessage, Toast.LENGTH_LONG).show()
        }
        lottieCompositionTask.addListener {
            binding.animationView.setComposition(it)
            binding.animationView.playAnimation()
        }
    }

}