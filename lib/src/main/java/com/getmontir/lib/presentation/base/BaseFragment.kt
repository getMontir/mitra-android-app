package com.getmontir.lib.presentation.base

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.getmontir.lib.R
import com.getmontir.lib.data.response.ApiErrorValidation
import com.getmontir.lib.data.response.ResultWrapper
import com.getmontir.lib.presentation.ErrorAlertType
import com.getmontir.lib.presentation.dialog.LoaderDialog
import com.getmontir.lib.presentation.utils.SessionManager
import org.koin.android.ext.android.inject
import timber.log.Timber

open class BaseFragment: Fragment() {

    companion object {
        private var TAG = BaseFragment::class.toString()
    }

    protected val sessionManager: SessionManager by inject()

    protected var showLoader: Boolean = true

    private var loaderDialog: LoaderDialog? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loaderDialog = context?.let { LoaderDialog(it) }
    }

    open fun showLoader() {
        if( showLoader ) {
            loaderDialog?.show()
        }
    }

    open fun hideLoader() {
        if( showLoader ) {
            loaderDialog?.dismiss()
        }
    }

    open fun processData(tag: String, data: ResultWrapper<Any> ) {
        when( data ) {
            is ResultWrapper.Success -> processResult(tag, data.data)
            is ResultWrapper.Error.GenericError -> handleGenericError(tag, data.exception)
            is ResultWrapper.Error.Http.BadRequest -> handleHttpBadRequest(tag, data.exception)
            is ResultWrapper.Error.Http.NotFound -> handleHttpNotFound(tag,data.exception)
            is ResultWrapper.Error.Http.Maintenance -> handleHttpMaintenance(tag,data.exception)
            is ResultWrapper.Error.Http.Unauthorized -> handleHttpUnauthorized(tag,data.exception)
            is ResultWrapper.Error.Http.BadMethod -> handleHttpBadMethod(tag,data.exception)
            is ResultWrapper.Error.Http.ServerError -> handleServerError(tag, data.exception)
            is ResultWrapper.Error.Http.Validation -> handleHttpValidation(tag, data.exception,
                data.data as ApiErrorValidation?
            )
            is ResultWrapper.Error.Network.NoConnectivity -> handleNetworkNoConnectivity(tag,data.exception)
            is ResultWrapper.Loading -> {
                if( data.loading ) {
                    showLoader()
                } else {
                    hideLoader()
                }
            }
        }
    }

    open fun processResult( tag: String, data: Any? ) {
        Timber.tag(tag).d(data.toString())
    }

    open fun handleGenericError( tag: String, e: Exception ) {
        Timber.tag(tag).e("Generic Error")
        activity?.let {
            val alert = AlertDialog.Builder(it, R.style.ThemeOverlay_MaterialComponents_Dialog_Alert)
                .setTitle("Ooopppsss..")
                .setPositiveButton("Ok") { _, _ ->
                    onAlertErrorClosed(tag, ErrorAlertType.UNAUTHORIZED)
                }
                .create()

            alert.setMessage(e.message)
            alert.show()
        }
    }

    open fun handleHttpBadRequest( tag: String, e:Exception ) {
        Timber.tag(tag).d("Bad Request")
        activity?.let {
            val alert = AlertDialog.Builder(it, R.style.ThemeOverlay_MaterialComponents_Dialog_Alert)
                .setTitle("Ooopppsss..")
                .setPositiveButton("Ok") { _, _ ->
                    onAlertErrorClosed(tag, ErrorAlertType.BAD_REQUEST)
                }
                .create()

            alert.setMessage("Kesalah pengiriman data.")
            alert.show()
        }
    }

    open fun handleHttpNotFound( tag: String, e: Exception ) {
        Timber.tag(tag).d("Not Found")
        activity?.let {
            val alert = AlertDialog.Builder(it, R.style.ThemeOverlay_MaterialComponents_Dialog_Alert)
                .setTitle("Ooopppsss..")
                .setPositiveButton("Ok") { _, _ ->
                    onAlertErrorClosed(tag, ErrorAlertType.NOT_FOUND)
                }
                .create()

            alert.setMessage("Data tidak ditemukan.")
            alert.show()
        }
    }

    open fun handleHttpMaintenance( tag: String, e: Exception ) {
        Timber.tag(tag).d("Maintenance")
        activity?.let {
            val alert = AlertDialog.Builder(it, R.style.ThemeOverlay_MaterialComponents_Dialog_Alert)
                .setTitle("Server Maintenance")
                .setPositiveButton("Ok") { _, _ ->
                    onAlertErrorClosed(tag, ErrorAlertType.MAINTENANCE)
                }
                .create()

            alert.setMessage("Mohon maaf, saat ini server sedang mengalami update rutin.\nSilahkan kembali beberapa saat lagi")
            alert.show()
        }
    }

    open fun handleHttpUnauthorized( tag: String, e: Exception ) {
        Timber.tag(tag).d("Unauthorized")
        activity?.let {
            val alert = AlertDialog.Builder(it, R.style.ThemeOverlay_MaterialComponents_Dialog_Alert)
                .setTitle("Ooopppsss..")
                .setPositiveButton("Ok") { _, _ ->
                    onAlertErrorClosed(tag, ErrorAlertType.UNAUTHORIZED)
                }
                .create()

            alert.setMessage("Sesi telah habis, harap login kembali.")
            alert.show()
        }
    }

    open fun handleHttpBadMethod(tag: String, e: Exception ) {
        Timber.tag(tag).d("Bad Method")
        activity?.let {
            val alert = AlertDialog.Builder(it, R.style.ThemeOverlay_MaterialComponents_Dialog_Alert)
                .setTitle("Ooopppsss..")
                .setPositiveButton("Ok") { _, _ ->
                    onAlertErrorClosed(tag, ErrorAlertType.VALIDATION)
                }
                .create()

            alert.setMessage("Ada input yang belum diisin, harap periksa kembali.")
            alert.show()
        }
    }

    open fun handleHttpValidation( tag: String, e: Exception, data: ApiErrorValidation? ) {
        Timber.tag(tag).d("Invalid Validation: $data")
    }

    open fun handleServerError(tag: String, e:Exception) {
        Timber.tag(tag).e("Server Error")
        activity?.let {
            val alert = AlertDialog.Builder(it, R.style.ThemeOverlay_MaterialComponents_Dialog_Alert)
                .setTitle("Ooopppsss..")
                .setPositiveButton("Ok") { _, _ ->
                    onAlertErrorClosed(tag, ErrorAlertType.INTERNAL_ERROR)
                }
                .create()

            alert.setMessage("Terjadi kesalahan pada dari server (500).")
            alert.show()
        }
    }

    open fun handleNetworkNoConnectivity(tag: String, e: Exception) {
        Timber.tag(tag).d("No Connectivity")
//        activity?.let {
//            val alert = AlertDialog.Builder(it, R.style.ThemeOverlay_MaterialComponents_Dialog_Alert)
//                .setTitle("Ooopppsss..")
//                .setNegativeButton("Tutup") { _, _ ->
//                    onAlertErrorClosed(tag, ErrorAlertType.NO_CONNECTION)
//                }
//                .setPositiveButton("Coba Lagi") { _, _ ->
//                    onRetry(tag)
//                }
//                .create()
//
//            alert.setMessage("Tidak ada koneksi internet, harap periksa kembali koneksi internet Anda.")
//            alert.show()
//        }
    }

    open fun onRetry(tag: String) {
        // DO NOTHING
    }

    open fun onAlertErrorClosed( tag: String, type: ErrorAlertType ) {
        // DO NOTHING
    }
}