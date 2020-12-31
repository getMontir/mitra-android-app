package com.getmontir.mitra.services

import com.google.firebase.auth.FirebaseAuth

object FirebaseAuthModule {
    fun create(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }
}