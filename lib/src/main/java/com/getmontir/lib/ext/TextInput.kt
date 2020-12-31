package com.getmontir.lib.ext

import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.widget.CheckBox
import android.widget.EditText
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

fun TextInputEditText.isPasswordConfirmation(
    editTextInput: TextInputEditText,
    errorNullString: String,
    errorConfirmationNullString: String,
    errorLengthString: String,
    errorConfirmationString: String,
    length: Int = 6
): Boolean {
    val textInputLayout = this.parent.parent as TextInputLayout
    textInputLayout.errorIconDrawable = null

    val matches = editTextInput.text.toString().trim() == this.text.toString().trim()
    return if( this.isPassword(errorNullString, errorLengthString, length) ) {
        if( editTextInput.isPassword(errorConfirmationNullString, errorLengthString, length) ) {
            textInputLayout.error = if( matches ) null else errorConfirmationString
            matches
        } else {
            false
        }
    } else {
        false
    }
}

fun TextInputEditText.isPhoneNotNull( errorNullString: String, errorPhoneString: String ): Boolean {
    val textInputLayout = this.parent.parent as TextInputLayout
    textInputLayout.errorIconDrawable = null
    val matches = this.text.toString().trim().length >= 10
    return if( this.isNotNullOrEmpty(errorNullString) ) {
        textInputLayout.error = if( matches ) null else errorPhoneString
        matches
    } else {
        false
    }
}

fun TextInputEditText.isEmailNotNull( nullString: String, emailString: String ): Boolean {
    val textInputLayout = this.parent.parent as TextInputLayout
    textInputLayout.errorIconDrawable = null
    val matches = Patterns.EMAIL_ADDRESS.matcher(this.text.toString().trim()).matches()
    return if( this.isNotNullOrEmpty(nullString) ) {
        textInputLayout.error = if( matches ) null else emailString
        matches
    } else {
        false
    }
}

fun TextInputEditText.isPassword(errorString: String, lengthErrorString: String, length: Int = 6): Boolean {
    val textInputLayout = this.parent.parent as TextInputLayout
    textInputLayout.errorIconDrawable = null
    val matches = this.text.toString().trim().length >= length
    return if ( this.isNotNullOrEmpty(errorString) ) {
        textInputLayout.error = if( matches ) null else lengthErrorString
        matches
    } else {
        false
    }
}

fun TextInputEditText.isNotNullOrEmpty(errorString: String): Boolean {
    val textInputLayout = this.parent.parent as TextInputLayout
    this.onChange { textInputLayout.error = null }

    return if (this.text.toString().trim().isEmpty()) {
        textInputLayout.error = errorString
        false
    } else {
        true
    }
}

fun EditText.isNotNullAndMinLength( length: Int = 4, errorNullString: String, errorLengthString: String ): Boolean {
    val matches = this.text.toString().trim().length >= length
    return if( this.isNotNullOrEmpty(errorNullString) ) {
        this.error = if( matches ) null else errorLengthString
        matches
    } else {
        false
    }
}

fun EditText.isNotNullOrEmpty(errorString: String): Boolean {
    this.onChange { this.error = null }

    return if (this.text.toString().trim().isBlank()) {
        this.error = errorString
        false
    } else {
        true
    }
}

fun EditText.onChange(cb: (String) -> Unit) {

    this.addTextChangedListener(object : TextWatcher {

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

        override fun afterTextChanged(s: Editable?) {
            cb(s.toString())
        }

    })
}

fun CheckBox.isMustChecked( errorString: String ): Boolean {
    this.onChange {
        if( it ) {
            this.error = null
        } else {
            this.error = errorString
        }
    }

    return this.isChecked
}

fun CheckBox.onChange(cb: (Boolean) -> Unit) {
    this.setOnCheckedChangeListener { _, isChecked ->
        cb(isChecked)
    }
}