package com.example.materialapp

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.transition.Explode
import android.transition.Slide
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.databinding.DataBindingUtil
import com.example.materialapp.databinding.ActivityCollectionBinding

class CollectionActivity : AppCompatActivity() {

    lateinit var binding: ActivityCollectionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_collection)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            binding.imageCard.transitionName = "transition_name_image"
        }

        binding.card.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                val image: Pair<View, String> = Pair.create(binding.imageCard, binding.imageCard.transitionName)
                val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, image)
                startActivity(intent, options.toBundle())
            } else {
                startActivity(intent)
            }
        }

        binding.imageStar.animateVectorDrawableInfinite(R.drawable.avd_anim_start)
    }

}