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
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.NavigationUI
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.getmontir.lib.ext.isEmailNotNull
import com.getmontir.lib.ext.isPassword
import com.getmontir.mitra.R
import com.getmontir.mitra.databinding.FragmentAuthLoginBinding
import com.getmontir.mitra.utils.enums.LoginType
import com.getmontir.mitra.utils.enums.Role
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
 * Use the [LoginFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LoginFragment : GetFragment() {

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment LoginFragment.
         */
        @JvmStatic
        fun newInstance() = LoginFragment()
        private val TAG = LoginFragment::class.java.simpleName
    }

    private lateinit var binding: FragmentAuthLoginBinding

    private lateinit var resultLauncher: ActivityResultLauncher<Intent>

    private lateinit var callbackManager: CallbackManager

    private val args: LoginFragmentArgs by navArgs()

    private val viewModel: AuthViewModel by inject()

    private val firebaseAuth: FirebaseAuth by inject()

    private val googleSignInClient: GoogleSignInClient by inject()

    private var fcmToken: String = ""

    private var idToken: String? = null

    private var idEmail: String? = null

    private var account: GoogleSignInAccount? = null

    private var loginType: LoginType? = null

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

                if( args.role == Role.MECHANIC ) {
                    idToken?.let { viewModel.mechanicLoginGoogle(it, fcmToken ) }
                } else {
                    idToken?.let { viewModel.stationLoginGoogle(it, fcmToken ) }
                }
                loginType = LoginType.GOOGLE
            }
        }

        // Setup View
        binding = FragmentAuthLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    @InternalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Setup toolbar
        val toolbar: Toolbar = binding.toolbar
        val navHostFragment = NavHostFragment.findNavController(this)
        NavigationUI.setupWithNavController(toolbar, navHostFragment)

        // Setup args
        Timber.tag(TAG).d(args.role.toString())
        if( args.role == Role.MECHANIC ) {
            binding.iconMechanic.visibility = View.VISIBLE
        } else {
            binding.iconStation.visibility = View.VISIBLE
        }

        // Setup FCM
        setupFCM()
        callbackManager = CallbackManager.Factory.create()

        // Setup view model
        viewModel.token.observe( viewLifecycleOwner, {
            processData("token", it)
        })
        viewModel.user.observe( viewLifecycleOwner, {
            processData("user", it)
        })

        // Setup view
        binding.divider.btnSocialFacebook.text = resources.getString(R.string.button_social_login_facebook)
        binding.divider.btnSocialGoogle.text = resources.getString(R.string.button_social_login_google)

        // Setup listener
        binding.btnSignIn.setOnClickListener {
            doLogin()
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
        binding.divider.btnFacebook.registerCallback(callbackManager, object:
            FacebookCallback<LoginResult> {
            override fun onSuccess(result: LoginResult?) {
                accessToken = result?.accessToken
                val token = accessToken?.token
                Timber.tag(TAG).d("facebook:onSuccess $token")
                if (token != null) {
                    loginType = LoginType.FACEBOOK
                    if( args.role == Role.MECHANIC ) {
                        viewModel.mechanicLoginFacebook(token, fcmToken)
                    } else {
                        viewModel.stationLoginFacebook(token, fcmToken)
                    }
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
        binding.textActionForgot.setOnClickListener {
//            val action = LoginFragmentDirections.actionLoginFragmentToForgotPasswordFragment()
//            findNavController().navigate(action)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Timber.tag(TAG).d("ACTIVITYRESULT:RESULTCODE:: $resultCode")
        Timber.tag(TAG).d("ACTIVITYRESULT:REQUESTCODE:: $requestCode")

        if( resultCode == Activity.RESULT_OK ) {
            // Pass the activity result back to the Facebook SDK
            callbackManager.onActivityResult(requestCode, resultCode, data)
        }
    }

    @InternalCoroutinesApi
    override fun processResult(tag: String, data: Any?) {
        super.processResult(tag, data)

        if( tag == "token" ) {
            showLoader()
            // Firebase Auth With Google
            if( loginType == LoginType.GOOGLE ) {
                account?.let {
                    firebaseAuthWithGoogle(it)
                }
            }else if( loginType == LoginType.FACEBOOK ) {
                accessToken?.let {
                    firebaseAuthWithFacebook(it)
                }
            } else {
                // Load user information
                viewModel.profile()
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
            if( loginType == null ) {
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
            if( loginType == null ) {
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
    private fun doLogin() {
        val email = binding.textInputEmail.text.toString().trim()
        val password = binding.textInputPassword.text.toString().trim()

        if(
            binding.textInputEmail.isEmailNotNull(
                getString(R.string.error_field_email_empty),
                getString(R.string.error_field_email_invalid)
            )
            && binding.textInputPassword.isPassword(
                getString(R.string.error_field_password_empty),
                getString(R.string.error_field_password_length)
            )
        ) {
            loginType = null
            binding.textLayoutEmail.error = null
            binding.textLayoutPassword.error = null
            if( args.role == Role.MECHANIC ) {
                viewModel.mechanicLogin(email, password)
            } else {
                viewModel.stationLogin(email, password)
            }
        }
    }

    private fun doLogout() {}

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