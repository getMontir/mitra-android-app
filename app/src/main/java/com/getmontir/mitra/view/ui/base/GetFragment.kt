package com.getmontir.mitra.view.ui.base

import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.util.TypedValue
import android.view.ContextThemeWrapper
import androidx.annotation.StyleRes
import androidx.navigation.fragment.findNavController
import com.getmontir.lib.presentation.base.BaseFragment
import com.getmontir.mitra.R

open class GetFragment: BaseFragment() {

    override fun handleNetworkNoConnectivity(tag: String, e: Exception) {
        super.handleNetworkNoConnectivity(tag, e)
        findNavController().navigate(R.id.offlineFragment)
    }

    open fun navigateToUpdate() {
        findNavController().navigate(R.id.updateFragment)
    }

    protected fun wrapContextTheme(activity: Activity?, @StyleRes styleRes: Int): Context {

        return ContextThemeWrapper(activity, styleRes)
    }

    protected fun fetchPrimaryDarkColor(context: Resources.Theme): Int {
        val typedValue = TypedValue()
        val a = context.obtainStyledAttributes(typedValue.data, intArrayOf())
        val color = a.getColor(0, 0)
        a.recycle()
        return color
    }
}