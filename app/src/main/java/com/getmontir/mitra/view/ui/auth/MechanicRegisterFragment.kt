package com.getmontir.mitra.view.ui.auth

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.getmontir.lib.ext.*
import com.getmontir.mitra.R
import com.getmontir.mitra.databinding.FragmentAuthMechanicRegisterBinding
import com.getmontir.mitra.utils.enums.LoginType
import com.getmontir.mitra.view.ui.base.GetFragment
import com.getmontir.mitra.viewmodel.AuthViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.InternalCoroutinesApi
import org.koin.android.ext.android.inject
import timber.log.Timber

/**
 * A simple [Fragment] subclass.
 * Use the [MechanicRegisterFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MechanicRegisterFragment : GetFragment() {

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment MechanicRegisterFragment.
         */
        @JvmStatic
        fun newInstance() = MechanicRegisterFragment()
        private val TAG = MechanicRegisterFragment::class.java.simpleName
    }

    private lateinit var binding: FragmentAuthMechanicRegisterBinding

    private lateinit var resultLauncher: ActivityResultLauncher<Intent>

    private lateinit var callbackManager: CallbackManager

    private val viewModel: AuthViewModel by inject()

    private val firebaseAuth: FirebaseAuth by inject()

    private val googleSignInClient: GoogleSignInClient by inject()

    private var fcmToken: String = ""

    private var idToken: String? = null

    private var idEmail: String? = null

    private var account: GoogleSignInAccount? = null

    private var registerType: LoginType? = null

    private var accessToken: AccessToken? = null

    @InternalCoroutinesApi
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Setup activity result launcher
        resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if( result.resultCode == Activity.RESULT_OK ) {
                val data: Intent? = result.data

                val task = GoogleSignIn.getSignedInAccountFromIntent(data)

                account = task.getResult(ApiException::class.java)
                Timber.tag(TAG).d("Account $account")

                idToken = account?.idToken
                Timber.tag(TAG).d("Token $idToken")

                idEmail = account?.email

                idToken?.let { viewModel.mechanicRegisterGoogle(it, fcmToken) }
                registerType = LoginType.GOOGLE
            }
        }

        binding = FragmentAuthMechanicRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    @InternalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Setup Toolbar
        val toolbar: Toolbar = binding.toolbar
        val navHostFragment = NavHostFragment.findNavController(this)
        NavigationUI.setupWithNavController(toolbar, navHostFragment)

        // Setup Firebase
        setupFCM()
        callbackManager = CallbackManager.Factory.create()

        // Setup View Model
        viewModel.token.observe( viewLifecycleOwner, {
            processData("token", it)
        })
        viewModel.user.observe(viewLifecycleOwner, {
            processData("user", it)
        })

        // Setup Listener
        binding.btnSignUp.setOnClickListener {
            doRegister()
        }
        binding.divider.btnSocialGoogle.setOnClickListener {
            val intent = googleSignInClient.signInIntent
            resultLauncher.launch(intent)
        }
        binding.divider.btnSocialFacebook.setOnClickListener {
            binding.divider.btnFacebook.performClick()
        }

        binding.divider.btnFacebook.setPermissions("email", "public_profile")
        binding.divider.btnFacebook.fragment = this
        binding.divider.btnFacebook.registerCallback(callbackManager, object: FacebookCallback<LoginResult> {

            override fun onSuccess(result: LoginResult?) {
                val accessToken = result?.accessToken
                val token = accessToken?.token
                Timber.tag(TAG).d("facebook:onSuccess $token")
                if (token != null) {
                    viewModel.mechanicRegisterFacebook(token, fcmToken)
                }
            }

            override fun onCancel() {
                Timber.tag(TAG).d("facebook:onCancel")
            }

            override fun onError(error: FacebookException?) {
                Timber.tag(TAG).d("facebook:onError")
                Timber.tag(TAG).e(error)
            }

        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        callbackManager.onActivityResult(requestCode, resultCode, data)
    }

    @InternalCoroutinesApi
    override fun processResult(tag: String, data: Any?) {
        super.processResult(tag, data)

        if( tag == "token" ) {
            // Firebase Auth With Google
            when (registerType) {
                LoginType.GOOGLE -> {
                    showLoader()
                    account?.let {
                        firebaseAuthWithGoogle(it)
                    }
                }
                LoginType.FACEBOOK -> {
                    showLoader()
                    accessToken?.let {
                        firebaseAuthWithFacebook(it)
                    }
                }
                else -> {
                    // Load user information
                    viewModel.profile()
                }
            }
        }

        if( tag == "user" ) {
            // Launch main activity
            activity?.finish()
        }
    }

    /**
     * When user credential not found
     */
    override fun handleHttpNotFound(tag: String, e: Exception) {
        if( tag == "token" ) {
            if( registerType == null ) {
                binding.textLayoutEmail.error = getString(R.string.error_auth_login_not_found)
            } else {
                val alert = AlertDialog.Builder(requireContext(), R.style.ThemeOverlay_MaterialComponents_Dialog_Alert)
                    .setTitle("Ooopppsss..")
                    .setPositiveButton("Ok") { _, _ ->
                        doLogout()
                    }
                    .create()

                alert.setMessage( getString(R.string.error_auth_login_not_found) )
                alert.show()
            }
        }
    }

    /**
     * When user credential wrong
     */
    override fun handleHttpBadMethod(tag: String, e: Exception) {
        if( tag == "token" ) {
            binding.textLayoutEmail.error = getString(R.string.error_auth_login_wrong)
        }
    }

    /**
     * When user is banned
     */
    override fun handleHttpBadRequest(tag: String, e: Exception) {
        if( tag == "token" ) {
            if( registerType == null ) {
                binding.textLayoutEmail.error = getString(R.string.error_auth_login_block)
            } else {
                val alert = AlertDialog.Builder(requireContext(), R.style.ThemeOverlay_MaterialComponents_Dialog_Alert)
                    .setTitle("Ooopppsss..")
                    .setPositiveButton("Ok") { _, _ ->
                        doLogout()
                    }
                    .create()

                alert.setMessage( "Pengguna tersebut telah terdaftar di akun lain, silahkan gunakan akun lainnya." )
                alert.show()
            }
        }
    }

    private fun setupFCM() {
        FirebaseMessaging.getInstance().token
            .addOnCompleteListener { task ->
                if (!task.isSuccessful) {
                    return@addOnCompleteListener
                }

                // Get new Instance ID token
                fcmToken = task.result.toString()
                Timber.tag(TAG).d("FCM Token: $fcmToken")
            }
    }

    @InternalCoroutinesApi
    private fun doRegister() {
        val name = binding.textInputName.text.toString().trim()
        val phone = binding.textInputPhone.text.toString().trim()
        val email = binding.textInputEmail.text.toString().trim()
        val password = binding.textInputPassword.text.toString().trim()
        val passwordConfirmation = binding.textInputPasswordRepeat.text.toString().trim()

        resources.apply {
            if(
                binding.textInputName.isNotNullOrEmpty(
                    getString(R.string.error_field_name_empty)
                )
                && binding.textInputPhone.isPhoneNotNull(
                    getString(R.string.error_field_phone_empty),
                    getString(R.string.error_field_phone_length)
                )
                && binding.textInputEmail.isEmailNotNull(
                    getString(R.string.error_field_email_empty),
                    getString(R.string.error_field_email_invalid)
                )
                && binding.textInputPassword.isPasswordConfirmation(
                    binding.textInputPasswordRepeat,
                    getString(R.string.error_field_password_empty),
                    getString(R.string.error_field_password_confirmation_empty),
                    getString(R.string.error_field_password_length),
                    getString(R.string.error_field_password_confirmation)
                )
                && binding.textInputAgree.isMustChecked(
                    getString(R.string.error_field_agree_unchecked)
                )
            ) {
                viewModel.mechanicRegister( name, phone, email, password, passwordConfirmation )
            }
        }
    }

    private fun doLogout() {
        firebaseAuth.signOut()
        LoginManager.getInstance().logOut()
    }

    @InternalCoroutinesApi
    private fun firebaseAuthWithGoogle(account: GoogleSignInAccount) {
        val credential: AuthCredential = GoogleAuthProvider.getCredential(account.idToken, null)
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener {
                hideLoader()
                if( it.isSuccessful ) {
                    // Load user information
                    viewModel.profile()
                    Timber.tag(TAG).w("signInWithCredential:success")
                    firebaseAuth.currentUser
                } else {
                    Timber.tag(TAG).e(it.exception, "signInWithCredential:failure")
                }
            }
    }

    @InternalCoroutinesApi
    private fun firebaseAuthWithFacebook(token: AccessToken) {
        val credential: AuthCredential = FacebookAuthProvider.getCredential(token.token)
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener {
                hideLoader()
                if( it.isSuccessful ) {
                    // Load user information
                    viewModel.profile()
                    Timber.tag(TAG).d("signInWithCredential:success")
                    firebaseAuth.currentUser
                } else {
                    Timber.tag(TAG).e(it.exception, "signInWithCredential:failure")
                    Toast.makeText(context, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                }
            }
    }

}