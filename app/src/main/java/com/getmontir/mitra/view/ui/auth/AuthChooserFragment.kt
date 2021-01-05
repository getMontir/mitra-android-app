package com.getmontir.mitra.view.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.getmontir.mitra.R
import com.getmontir.mitra.databinding.FragmentAuthChooserBinding
import com.getmontir.mitra.view.ui.base.GetFragment
import com.getmontir.mitra.viewmodel.AuthViewModel
import org.koin.android.ext.android.inject

class AuthChooserFragment : GetFragment() {

    companion object {
        fun newInstance() = AuthChooserFragment()
    }

    private lateinit var binding : FragmentAuthChooserBinding

    private val viewModel: AuthViewModel by inject()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = FragmentAuthChooserBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Setup toolbar
        val toolbar: Toolbar = binding.toolbar
        val appBarConfiguration = AppBarConfiguration(setOf(
            R.id.authChooserFragment
        ))
        val navHostFragment = NavHostFragment.findNavController(this)
        NavigationUI.setupWithNavController(toolbar, navHostFragment, appBarConfiguration)

        // Setup listener
        binding.btnRegister.setOnClickListener {  }
        binding.btnLogin.setOnClickListener {  }
    }
}