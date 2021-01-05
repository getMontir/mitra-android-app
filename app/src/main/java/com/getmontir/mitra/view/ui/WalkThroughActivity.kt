package com.getmontir.mitra.view.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.text.Html
import android.view.View
import android.widget.TextView
import androidx.viewpager.widget.ViewPager
import com.getmontir.lib.presentation.base.BaseActivity
import com.getmontir.mitra.R
import com.getmontir.mitra.databinding.ActivityWalkthroughBinding
import com.getmontir.mitra.view.adapter.WalkThroughAdapter

class WalkThroughActivity : BaseActivity() {

    private lateinit var binding: ActivityWalkthroughBinding

    private lateinit var sliderAdapter: WalkThroughAdapter

    private var layouts: IntArray = intArrayOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWalkthroughBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // View pager lists
        layouts = intArrayOf(
            R.layout.item_walkthrough_01,
            R.layout.item_walkthrough_02,
            R.layout.item_walkthrough_03
        )

        // Add first dots
        addSliderDots(0)

        // Set Adapter
        sliderAdapter = WalkThroughAdapter(this, layouts)
        binding.viewPager.adapter = sliderAdapter

        // Setup listener
        binding.btnNext.setOnClickListener {
            onNextButtonClicked()
        }
        binding.btnSkip.setOnClickListener {
            onSkipButtonClicked()
        }
        binding.viewPager.addOnPageChangeListener(object: ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) = onSliderChange(position)
            override fun onPageScrollStateChanged(state: Int) {
            }
        })
    }

    private fun onSkipButtonClicked() {
        // Set session is not first used
        sessionManager.isUsed = true

        // Launch auth activity
//        NavigationActivity.launchActivity(this, AuthActivity::class.java)
        finish()
    }

    private fun onNextButtonClicked() {
        val current = getItem(+1)
        if (current < layouts.size) {
            // move to next screen
            binding.viewPager.currentItem = current
        } else {
            onSkipButtonClicked()
        }
    }

    fun onSliderChange(position: Int) {
        addSliderDots(position)

        // changing the next button text 'NEXT' / 'GOT IT'
        if (position == layouts.size - 1) {
            // last page. make button text to GOT IT
            binding.btnNext.text = getString(R.string.walkthrough_button_finish)
            binding.btnSkip.visibility = View.GONE

        } else {

            // still pages are left
            binding.btnNext.text = getString(R.string.walkthrough_button_next)
            binding.btnSkip.visibility = View.VISIBLE
        }
    }

    private fun addSliderDots(currentIndex: Int) {
        val dots = arrayOfNulls<TextView>(layouts.size)

        val colorsActive = resources.getIntArray(R.array.array_dot_active)
        val colorsInactive = resources.getIntArray(R.array.array_dot_inactive)

        binding.layoutDots.removeAllViews()

        for (i in dots.indices) {
            dots[i] = TextView(this)
            dots[i]?.text = Html.fromHtml("&#8226;")
            dots[i]?.textSize = 35f
            dots[i]?.setTextColor(colorsInactive[currentIndex])
            binding.layoutDots.addView(dots[i])
        }

        if (dots.isNotEmpty())
            dots[currentIndex]?.setTextColor(colorsActive[currentIndex])
    }

    private fun getItem(n: Int): Int {
        return binding.viewPager.currentItem + n
    }
}