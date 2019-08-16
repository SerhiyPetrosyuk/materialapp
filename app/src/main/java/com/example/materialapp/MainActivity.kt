package com.example.materialapp

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ObservableField
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat
import androidx.viewpager.widget.PagerAdapter
import com.example.materialapp.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.tabs.TabLayout


class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    lateinit var pagerAdapter: PagerAdapter
    private val viewModel = MainViewModel()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        setSupportActionBar(binding.toolbar)
        binding.viewModel = viewModel
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        findViewById<BottomNavigationView>(R.id.navigation).itemIconTintList = null
        findViewById<TabLayout>(R.id.tab_layout).tabIconTint = null
        initViewPager()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            binding.imageAvatar.transitionName = "transition_name_image"
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.title.set("Test title from ViewModel")
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun initViewPager() {
        pagerAdapter = PagedAdapter(supportFragmentManager)
        binding.tabLayout.setupWithViewPager(binding.viewPager)
        binding.viewPager.adapter = pagerAdapter
        binding.tabLayout.getTabAt(TAB_GENERAL_POSITION)?.icon =
            ContextCompat.getDrawable(this, R.drawable.tab_general)
        binding.tabLayout.getTabAt(TAB_DOCTORS_POSITION)?.icon =
            ContextCompat.getDrawable(this, R.drawable.tab_doctors)
        binding.tabLayout.getTabAt(TAB_REVIEWS_POSITION)?.icon =
            ContextCompat.getDrawable(this, R.drawable.tab_review)
    }

    override fun onBackPressed() {
        supportFinishAfterTransition()
        super.onBackPressed()
    }

    inner class PagedAdapter(fm: FragmentManager?) : FragmentStatePagerAdapter(fm) {

        override fun getPageTitle(position: Int): CharSequence? = when (position) {
            TAB_GENERAL_POSITION -> TAB_GENERAL
            TAB_DOCTORS_POSITION -> TAB_DOCTORS
            TAB_REVIEWS_POSITION -> TAB_REVIEWS
            else -> null
        }

        override fun getItem(position: Int): Fragment = when (position) {
            TAB_GENERAL_POSITION -> GeneralInfoFragment()
            else -> StubFragment()
        }

        override fun getCount(): Int = TABS_COUNT

    }

    companion object {
        internal const val TAB_GENERAL = "General"
        internal const val TAB_DOCTORS = "Doctors"
        internal const val TAB_REVIEWS = "Reviews"
        internal const val TAB_GENERAL_POSITION = 0
        internal const val TAB_DOCTORS_POSITION = 1
        internal const val TAB_REVIEWS_POSITION = 2
        internal const val TABS_COUNT = 3
    }

}

class MainViewModel {
    val title = ObservableField<String>()
}

fun getVector(activity: Context, resourceId: Int): Drawable? =
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
        VectorDrawableCompat.create(activity.resources, resourceId, activity.theme)
    } else {
        activity.resources.getDrawable(resourceId, activity.theme)
    }
