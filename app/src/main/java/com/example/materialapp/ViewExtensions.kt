package com.example.materialapp

import android.graphics.drawable.Drawable
import android.view.View
import androidx.annotation.DrawableRes
import androidx.vectordrawable.graphics.drawable.Animatable2Compat
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat

fun View.animateVectorDrawableInfinite(@DrawableRes animationRes: Int) {
    val avdCompat: AnimatedVectorDrawableCompat? = AnimatedVectorDrawableCompat.create(context, animationRes)
    background = avdCompat

    avdCompat?.registerAnimationCallback(object : Animatable2Compat.AnimationCallback() {
        override fun onAnimationEnd(drawable: Drawable?) {
            this@animateVectorDrawableInfinite.post { avdCompat.start() }
        }
    })

    post { avdCompat?.start() }
}


fun View.animateVectorDrawable(@DrawableRes animationRes: Int) {
    val avdCompat: AnimatedVectorDrawableCompat? = AnimatedVectorDrawableCompat.create(context, animationRes)
    background = avdCompat
    post { avdCompat?.start() }
}