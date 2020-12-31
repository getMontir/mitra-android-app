package com.getmontir.lib.presentation.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Window
import com.getmontir.lib.R

class LoaderDialog(context: Context): Dialog(context) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // SET LAYOUT
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dialog_loader)

        // SET DIALOG
        setCancelable(false)

    }

}