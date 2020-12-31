package com.getmontir.lib.presentation.utils

import android.content.Context
import com.getmontir.lib.presentation.session

class SessionManager(private val context: Context, private val prefName: String) {

    init {
        PrefDelegate.init(context)
    }

    var isUsed: Boolean by booleanPref( prefName, session.isUsed, false)

    var language: String? by stringPref( prefName, session.language, "id")

    var isLoggedIn: Boolean by booleanPref( prefName, session.isLoggedIn, false)

    var token: String? by stringPref( prefName, session.token, null )

    var forgotEmail: String? by stringPref( prefName, session.forgotEmail, null )

    var forgotToken: String? by stringPref( prefName, session.forgotToken, null )

    var userId: String? by stringPref( prefName, session.userId, null )

    var userImage: String? by stringPref( prefName, session.userImage, null )

    var userName: String? by stringPref( prefName, session.userName, null )

    var userPhone: String? by stringPref( prefName, session.userPhone, null )

    var userEmail: String? by stringPref( prefName, session.userEmail, null )

    var userBanned: Boolean by booleanPref( prefName, session.userBanned, false )

    var userAccepted: Boolean by booleanPref( prefName, session.userAccepted, false )

    var userInformationCompleted: Boolean by booleanPref( prefName, session.userInformationCompleted, false )

    var userDocumentCompleted: Boolean by booleanPref( prefName, session.userDocumentCompleted, false )

    var userEmailVerifiedAt: String? by stringPref( prefName, session.userEmailVerifiedAt, null )

}