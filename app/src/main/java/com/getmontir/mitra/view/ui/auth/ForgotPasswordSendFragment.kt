package com.getmontir.mitra.view.ui.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.appcompat.widget.Toolbar
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.NavigationUI
import com.getmontir.lib.ext.isEmailNotNull
import com.getmontir.mitra.R
import com.getmontir.mitra.databinding.FragmentAuthForgotPasswordSendBinding
import com.getmontir.mitra.utils.enums.Role
import com.getmontir.mitra.view.ui.base.GetFragment
import com.getmontir.mitra.viewmodel.AuthViewModel
import kotlinx.coroutines.InternalCoroutinesApi
import org.koin.androidx.viewmodel.ext.android.viewModel


/**
 * A simple [Fragment] subclass.
 * Use the [ForgotPasswordSendFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ForgotPasswordSendFragment : GetFragment() {

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment ForgotPasswordSendFragment.
         */
        @JvmStatic
        fun newInstance() = ForgotPasswordSendFragment()
    }

    private lateinit var binding: FragmentAuthForgotPasswordSendBinding

    private val viewModel: AuthViewModel by viewModel()

    private val args: ForgotPasswordSendFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAuthForgotPasswordSendBinding.inflate( inflater, container, false )
        return binding.root
    }

    @InternalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Setup Toolbar
        val toolbar: Toolbar = binding.toolbar
        val navHostFragment = NavHostFragment.findNavController(this)
        NavigationUI.setupWithNavController(toolbar, navHostFragment)

        // Setup view model
        viewModel.forgotToken.observe(viewLifecycleOwner, {
            processData("token", it)
        })

        // Setup listener
        binding.textInputEmail.setOnEditorActionListener { _, actionId, _ ->
            if( actionId == EditorInfo.IME_ACTION_DONE ) {
                binding.btnSend.performClick()
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }
        binding.btnSend.setOnClickListener {
             doReset()
        }
    }

    override fun processResult(tag: String, data: Any?) {
        super.processResult(tag, data)
        if( tag == "token" ) {
            // Show fragment verification
            showConfirm()
        }
    }

    @InternalCoroutinesApi
    private fun doReset() {
        if( binding.textInputEmail.isEmailNotNull(
            getString(R.string.error_field_email_empty),
            getString(R.string.error_field_email_invalid)
        ) ) {
            if( args.role == Role.MECHANIC ) {
                viewModel.mechanicForgotPassword(binding.textInputEmail.text.toString().trim())
            } else {
                viewModel.stationForgotPassword(binding.textInputEmail.text.toString().trim())
            }
        }
    }

    /**
     * User not found
     */
    override fun handleHttpNotFound(tag: String, e: Exception) {
        if( tag == "token" ) {
            binding.textLayoutEmail.error = getString(R.string.error_auth_forgot_email_not_found)
        }
    }

    private fun showConfirm() {
        val action = ForgotPasswordSendFragmentDirections.actionForgotPasswordSendFragmentToForgotPasswordConfirmFragment(args.role)
        findNavController().navigate(action)
    }
}