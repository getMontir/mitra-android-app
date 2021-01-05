package com.getmontir.mitra.view.ui.splash

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.getmontir.mitra.BuildConfig
import com.getmontir.mitra.R
import com.getmontir.mitra.view.ui.WalkThroughActivity
import com.getmontir.mitra.view.ui.base.GetFragment
import com.getmontir.mitra.viewmodel.SplashScreenViewModel
import kotlinx.coroutines.InternalCoroutinesApi
import org.koin.androidx.viewmodel.ext.android.viewModel

class SplashScreenFragment : GetFragment() {

    companion object {
        fun newInstance() = SplashScreenFragment()
    }

    private val viewModel: SplashScreenViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_splash_screen, container, false)
    }

    @InternalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Setup view model
        viewModel.version.observe(viewLifecycleOwner, {
            processData("version", it)
        })
        viewModel.loadVersion(BuildConfig.VERSION_CODE)
    }

    override fun processResult(tag: String, data: Any?) {
        super.processResult(tag, data)
        if( tag == "version" ) {
            val d = data as Boolean
            val navController = findNavController()

            if( d ) {
                navController.navigate(R.id.updateFragment)
            } else {
                if( !sessionManager.isLoggedIn ) {
                    if( !sessionManager.isUsed ) {
                        // Start walk through activity
                        startActivity(
                            Intent(requireContext(), WalkThroughActivity::class.java)
                        )
                    } else {
                        // Start auth activity
                    }
                } else {
                    // Start main activity
                }
                activity?.finish()
            }
        }
    }
}