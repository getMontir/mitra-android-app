package com.getmontir.mitra.view.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.getmontir.mitra.R
import com.getmontir.mitra.databinding.AuthActivityBinding
import com.getmontir.mitra.view.ui.auth.AuthChooserFragment

class AuthActivity : AppCompatActivity() {

    private lateinit var binding: AuthActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = AuthActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}