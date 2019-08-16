package com.example.materialapp

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.Toolbar
import androidx.core.view.children
import androidx.dynamicanimation.animation.DynamicAnimation
import androidx.dynamicanimation.animation.SpringAnimation
import androidx.dynamicanimation.animation.SpringForce
import com.example.materialapp.AppBarState.*
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.AppBarLayout.OnOffsetChangedListener
import kotlin.math.abs

class SecondaryAppBarLayout(context: Context?, attrs: AttributeSet?) : AppBarLayout(context, attrs) {

    constructor(context: Context?) : this(context, null)

    var collapsedTitle: String? = null
    var expandedTitle: String? = null
    var defaultElevation: Float = 0f

    private val topAppBarElevation: Float
        get() = if (defaultElevation == 0f) context.resources.getDimensionPixelSize(R.dimen.default_app_bar_elevation).toFloat()
        else defaultElevation * 2f


    private var firstLaunch = true

    private val topToolbar: Toolbar? by lazy {
        ((parent?.parent as? ViewGroup?)
            ?.getChildAt(0) as ViewGroup)
            .getChildAt(0) as? Toolbar?
    }

    private val offsetChangedListener = OnOffsetChangedListener { _, offset ->
        val topAppBarLayout: View? = topToolbar?.parent as? View

        when (getState(offset)) {
            COLLAPSED -> {
                changeTitle(collapsedTitle ?: DEFAULT_TITLE, true)
                topAppBarLayout?.spring(SpringAnimation.TRANSLATION_Z)?.animateToFinalPosition(0f)
                spring(SpringAnimation.TRANSLATION_Z).animateToFinalPosition(topAppBarElevation)
            }
            EXPANDED -> {
                changeTitle(expandedTitle ?: DEFAULT_TITLE, false)
                topAppBarLayout?.spring(SpringAnimation.TRANSLATION_Z)?.animateToFinalPosition(0f)
                spring(SpringAnimation.TRANSLATION_Z).animateToFinalPosition(defaultElevation)
            }
            IDLE -> {
                spring(SpringAnimation.TRANSLATION_Z).animateToFinalPosition(defaultElevation)
                topAppBarLayout?.spring(SpringAnimation.TRANSLATION_Z)?.animateToFinalPosition(topAppBarElevation)
            }
        }

    }

    init {
        addOnOffsetChangedListener(offsetChangedListener)

        attrs?.let {
            val attributes = this.context.obtainStyledAttributes(it, R.styleable.SecondaryAppBarLayout)

            collapsedTitle = attributes.getString(R.styleable.SecondaryAppBarLayout_collapsedTitle) ?: DEFAULT_TITLE
            expandedTitle = attributes.getString(R.styleable.SecondaryAppBarLayout_expandedTitle) ?: DEFAULT_TITLE
            defaultElevation =
                attributes.getDimensionPixelSize(R.styleable.SecondaryAppBarLayout_defaultElevation, 0).toFloat()

            attributes.recycle()
        }
    }

    private fun changeTitle(title: String, collapsed: Boolean) {

        topToolbar?.let { toolbar ->
            if (toolbar.title == title)
                return

            (toolbar as ViewGroup).children.forEach { child ->
                if (child is AppCompatTextView) {
                    if (firstLaunch) {
                        firstLaunch = false
                        toolbar.title = expandedTitle
                        return
                    }

                    val fadeOutAnimator = ObjectAnimator.ofFloat(child, View.ALPHA, 0f)
                    val fadeInAnimator = ObjectAnimator.ofFloat(child, View.ALPHA, 1f)
                    val translateToTop = ObjectAnimator.ofFloat(child, View.TRANSLATION_Y, 0f, -75f)
                    val translateToBottom = ObjectAnimator.ofFloat(child, View.TRANSLATION_Y, 0f, 75f)
                    val translateFromBottom = ObjectAnimator.ofFloat(child, View.TRANSLATION_Y, 75f, 0f)
                    val translateFromTop = ObjectAnimator.ofFloat(child, View.TRANSLATION_Y, -75f, 0f)
                    fadeOutAnimator.duration = TRANSLATION_DURATION
                    fadeInAnimator.duration = TRANSLATION_DURATION
                    translateToTop.duration = TRANSLATION_DURATION
                    translateFromBottom.duration = TRANSLATION_DURATION
                    translateFromTop.duration = TRANSLATION_DURATION
                    translateToBottom.duration = TRANSLATION_DURATION

                    fadeOutAnimator.addUpdateListener {
                        val alpha = it.animatedValue as Float
                        if (alpha == 0f) toolbar.title = title
                    }

                    val animation = AnimatorSet()

                    if (collapsed)
                        animation.playSequentially(translateToTop, translateFromBottom)
                    else
                        animation.playSequentially(translateToBottom, translateFromTop)

                    animation.playSequentially(fadeOutAnimator, fadeInAnimator)
                    animation.start()
                }
            }
        }
    }

    companion object {
        internal const val TRANSLATION_DURATION = 150L
        internal const val DEFAULT_TITLE = " "
    }

    private fun getState(offset: Int): AppBarState = when {
        offset == 0 -> EXPANDED
        abs(offset) >= this.totalScrollRange -> COLLAPSED
        else -> IDLE
    }

}

fun View.spring(property: DynamicAnimation.ViewProperty): SpringAnimation {
    val key = when (property) {
        DynamicAnimation.ALPHA -> R.id.alpha
        DynamicAnimation.TRANSLATION_Y -> R.id.translation_y
        DynamicAnimation.TRANSLATION_Z -> R.id.translation_z
        else -> -1
    }

    var springAnimation = getTag(key) as? SpringAnimation?

    if (springAnimation == null) {
        springAnimation = SpringAnimation(this, property)
        setTag(key, springAnimation)
    }

    return springAnimation
}

enum class AppBarState {
    COLLAPSED,
    EXPANDED,
    IDLE
}