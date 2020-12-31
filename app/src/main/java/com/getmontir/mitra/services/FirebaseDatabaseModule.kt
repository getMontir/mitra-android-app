package com.getmontir.mitra.services

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

object FirebaseDatabaseModule {
    fun create(): DatabaseReference {
        return FirebaseDatabase.getInstance()
            .reference
            .database
            .getReference("3.3.0")
    }
}