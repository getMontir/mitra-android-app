package com.getmontir.mitra.view.ui.auth

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.getmontir.lib.data.data.dto.StationRegisterContactDto
import com.getmontir.mitra.R
import com.getmontir.mitra.databinding.FragmentAuthStationRegisterContactBinding
import com.getmontir.mitra.view.ui.base.GetFragment
import com.getmontir.mitra.viewmodel.AuthViewModel
import kotlinx.coroutines.InternalCoroutinesApi
import org.koin.android.ext.android.inject

/**
 * A simple [Fragment] subclass.
 * Use the [StationRegisterContactFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class StationRegisterContactFragment : GetFragment() {

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment StationRegisterContactFragment.
         */
        @JvmStatic
        fun newInstance() = StationRegisterContactFragment()
    }

    private lateinit var binding: FragmentAuthStationRegisterContactBinding

    private val viewModel: AuthViewModel by inject()

    private var btnCallback: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAuthStationRegisterContactBinding.inflate(inflater, container, false)
        return binding.root
    }

    @InternalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Setup toolbar
        val toolbar: Toolbar = binding.toolbar
        val navHostFragment = NavHostFragment.findNavController(this)
        NavigationUI.setupWithNavController(toolbar, navHostFragment)

        // Setup view model
        viewModel.stationRegisterContract.observe(viewLifecycleOwner, {
            processData("contact", it)
        })
        viewModel.stationRegisterContact()
    }

    override fun processResult(tag: String, data: Any?) {
        super.processResult(tag, data)

        if( tag == "contact" ) {
            val item = data as StationRegisterContactDto
            binding.btnPhone.text = getString(R.string.button_agent, item.name)

            // Setup listener
            if( !btnCallback ) {
                binding.btnPhone.setOnClickListener {
                    startActivity(
                        Intent(
                            Intent.ACTION_DIAL,
                            Uri.parse("tel:" + item.phone)
                        )
                    )
                }
            }
        }
    }
}