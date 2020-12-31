package com.getmontir.mitra.view.ui

import android.os.Bundle
import com.getmontir.lib.presentation.base.BaseActivity
import com.getmontir.mitra.databinding.ActivitySplashScreenBinding

class SplashScreenActivity : BaseActivity() {

    private lateinit var binding: ActivitySplashScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}