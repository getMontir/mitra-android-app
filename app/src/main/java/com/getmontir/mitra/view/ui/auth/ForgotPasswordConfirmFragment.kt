package com.getmontir.mitra.view.ui.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.NavigationUI
import com.getmontir.lib.ext.isNotNullAndMinLength
import com.getmontir.mitra.R
import com.getmontir.mitra.databinding.FragmentAuthForgotPasswordConfirmBinding
import com.getmontir.mitra.utils.enums.Role
import com.getmontir.mitra.view.ui.base.GetFragment
import com.getmontir.mitra.viewmodel.AuthViewModel
import kotlinx.coroutines.InternalCoroutinesApi
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

/**
 * A simple [Fragment] subclass.
 * Use the [ForgotPasswordConfirmFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ForgotPasswordConfirmFragment : GetFragment() {

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment ForgotPasswordConfirmFragment.
         */
        @JvmStatic
        fun newInstance() = ForgotPasswordConfirmFragment()
    }

    private lateinit var binding: FragmentAuthForgotPasswordConfirmBinding

    private val viewModel: AuthViewModel by viewModel()

    private val args: ForgotPasswordConfirmFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAuthForgotPasswordConfirmBinding.inflate(inflater, container, false)
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
        viewModel.resendForgotToken.observe(viewLifecycleOwner, {
            processData("code", it)
        })

        // Setup listener
        binding.textOtp.requestFocus()
        binding.textOtp.setOnEditorActionListener { _, actionId, _ ->
            if( actionId == EditorInfo.IME_ACTION_DONE ) {
                Timber.tag("B").d("Done clicked")
                binding.btnSend.performClick()
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }
        binding.btnSend.setOnClickListener {
            doConfirm()
        }
        binding.textResendCode.setOnClickListener {
            sessionManager.forgotEmail?.let { it1 ->
                if( args.role == Role.MECHANIC ) {
                    viewModel.mechanicResendForgotCode(it1)
                } else {
                    viewModel.stationResendForgotCode(it1)
                }
            }
        }
    }

    override fun processResult(tag: String, data: Any?) {
        super.processResult(tag, data)
        if( tag == "token" ) {
            // If confirm load confirm password
            showChangePassword()
        }

        if( tag == "code" ) {
            // If resend
            activity?.let {
                val alert = AlertDialog.Builder(it, R.style.ThemeOverlay_MaterialComponents_Dialog_Alert)
                    .setTitle(getString(R.string.forgot_confirm_resend_title))
                    .setPositiveButton("Ok") { _, _ ->
                        sessionManager.token = data as String?
                    }
                    .create()

                alert.setMessage(getString(R.string.forgot_confirm_resend_message))
                alert.show()
            }
        }
    }

    @InternalCoroutinesApi
    private fun doConfirm() {
        if( binding.textOtp.isNotNullAndMinLength(
                4,
                getString(R.string.error_field_otp_empty),
                getString(R.string.error_field_otp_empty),
            )
        ) {
            sessionManager.forgotToken?.let {
                if( args.role == Role.MECHANIC ) {
                    viewModel.mechanicForgotConfirm(
                        binding.textOtp.text.toString().trim(),
                        it,
                        sessionManager.forgotEmail!!
                    )
                } else {
                    viewModel.stationForgotConfirm(
                        binding.textOtp.text.toString().trim(),
                        it,
                        sessionManager.forgotEmail!!
                    )
                }
            }
        }
    }

    /**
     * Invalid User
     */
    override fun handleHttpUnauthorized(tag: String, e: Exception) {
        if( tag == "token" ) {
            Timber.tag("BISMILLAH").e("Invalid user")
        }
    }

    /**
     * Token not exists
     */
    override fun handleHttpBadRequest(tag: String, e: Exception) {
        if( tag == "token" ) {
            Timber.tag("BISMILLAH").e("Token not exists")
        }
    }

    /**
     * OTP not exists
     */
    override fun handleHttpNotFound(tag: String, e: Exception) {
        if( tag == "token" ) {
            binding.textOtp.error = getString(R.string.error_auth_forgot_otp_wrong)
        }
    }

    private fun showChangePassword() {
//        val action = ForgotConfirmFragmentDirections.actionForgotConfirmFragmentToForgotChangePasswordFragment()
//        findNavController().navigate(action)
    }
}