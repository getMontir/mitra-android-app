package com.getmontir.lib.presentation.utils

import android.content.Context
import android.content.SharedPreferences
import at.favre.lib.armadillo.Armadillo
import at.favre.lib.armadillo.PBKDF2KeyStretcher
import java.security.SecureRandom
import kotlin.reflect.KProperty

abstract class PrefDelegate<T>(val prefName: String?, val prefKey: String) {

    companion object {
        private var context: Context? = null

        /**
         * Initialize PrefDelegate with a Context reference
         * !! This method needs to be called before any other usage of PrefDelegate !!
         */
        fun init(context: Context) {
            this.context = context
        }
    }

    protected val prefs: SharedPreferences by lazy {
        if (context != null)
//            if (prefName != null) context!!.getSharedPreferences(prefName, Context.MODE_PRIVATE) else PreferenceManager.getDefaultSharedPreferences(context!!)
            Armadillo.create(context, prefName)
                .password("BlackJaguar909@".toCharArray()) //use user provided password
                .keyStretchingFunction(PBKDF2KeyStretcher()) //use PBKDF2 as user password kdf
                .secureRandom(SecureRandom()) //provide your own secure random for salt/iv generation
                .encryptionFingerprint(context)
                .supportVerifyPassword(true) //enables optional password validation support `.isValidPassword()`
                .enableKitKatSupport(true) //enable optional kitkat support
                .enableDerivedPasswordCache(true) //enable caching for derived password making consecutive getters faster
                .build()
        else
            throw IllegalStateException("Context was not initialized. Call PrefDelegate.init(context) before using it")
    }

    abstract operator fun getValue(thisRef: Any?, property: KProperty<*>): T
    abstract operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T)
}

fun stringPref(prefKey: String, defaultValue: String? = null) = StringPrefDelegate(null, prefKey, defaultValue)
fun stringPref(prefName: String, prefKey: String, defaultValue: String? = null) = StringPrefDelegate(prefName, prefKey, defaultValue)
class StringPrefDelegate(prefName: String?, prefKey: String, private val defaultValue: String?) : PrefDelegate<String?>(prefName, prefKey) {
    override fun getValue(thisRef: Any?, property: KProperty<*>) = prefs.getString(prefKey, defaultValue)
    override fun setValue(thisRef: Any?, property: KProperty<*>, value: String?) = prefs.edit().putString(prefKey, value).apply()
}

fun intPref(prefKey: String, defaultValue: Int = 0) = IntPrefDelegate(null, prefKey, defaultValue)
fun intPref(prefName: String, prefKey: String, defaultValue: Int = 0) = IntPrefDelegate(prefName, prefKey, defaultValue)
class IntPrefDelegate(prefName: String?, prefKey: String, private val defaultValue: Int) : PrefDelegate<Int>(prefName, prefKey) {
    override fun getValue(thisRef: Any?, property: KProperty<*>) = prefs.getInt(prefKey, defaultValue)
    override fun setValue(thisRef: Any?, property: KProperty<*>, value: Int) = prefs.edit().putInt(prefKey, value).apply()
}

fun floatPref(prefKey: String, defaultValue: Float = 0f) = FloatPrefDelegate(null, prefKey, defaultValue)
fun floatPref(prefName: String, prefKey: String, defaultValue: Float = 0f) = FloatPrefDelegate(prefName, prefKey, defaultValue)
class FloatPrefDelegate(prefName: String?, prefKey: String, private val defaultValue: Float) : PrefDelegate<Float>(prefName, prefKey) {
    override fun getValue(thisRef: Any?, property: KProperty<*>) = prefs.getFloat(prefKey, defaultValue)
    override fun setValue(thisRef: Any?, property: KProperty<*>, value: Float) = prefs.edit().putFloat(prefKey, value).apply()
}

fun booleanPref(prefKey: String, defaultValue: Boolean = false) = BooleanPrefDelegate(null, prefKey, defaultValue)
fun booleanPref(prefName: String, prefKey: String, defaultValue: Boolean = false) = BooleanPrefDelegate(prefName, prefKey, defaultValue)
class BooleanPrefDelegate(prefName: String?, prefKey: String, private val defaultValue: Boolean) : PrefDelegate<Boolean>(prefName, prefKey) {
    override fun getValue(thisRef: Any?, property: KProperty<*>) = prefs.getBoolean(prefKey, defaultValue)
    override fun setValue(thisRef: Any?, property: KProperty<*>, value: Boolean) = prefs.edit().putBoolean(prefKey, value).apply()
}

fun longPref(prefKey: String, defaultValue: Long = 0L) = LongPrefDelegate(null, prefKey, defaultValue)
fun longPref(prefName: String, prefKey: String, defaultValue: Long = 0L) = LongPrefDelegate(prefName, prefKey, defaultValue)
class LongPrefDelegate(prefName: String?, prefKey: String, private val defaultValue: Long) : PrefDelegate<Long>(prefName, prefKey) {
    override fun getValue(thisRef: Any?, property: KProperty<*>) = prefs.getLong(prefKey, defaultValue)
    override fun setValue(thisRef: Any?, property: KProperty<*>, value: Long) = prefs.edit().putLong(prefKey, value).apply()
}

fun stringSetPref(prefKey: String, defaultValue: Set<String> = HashSet<String>()) = StringSetPrefDelegate(null, prefKey, defaultValue)
fun stringSetPref(prefName: String, prefKey: String, defaultValue: Set<String> = HashSet<String>()) = StringSetPrefDelegate(prefName, prefKey, defaultValue)
class StringSetPrefDelegate(prefName: String?, prefKey: String, private val defaultValue: Set<String>) :
    PrefDelegate<MutableSet<String>?>(prefName, prefKey) {
    override fun getValue(thisRef: Any?, property: KProperty<*>): MutableSet<String>? = prefs.getStringSet(prefKey, defaultValue)
    override fun setValue(thisRef: Any?, property: KProperty<*>, value: MutableSet<String>?) = prefs.edit().putStringSet(prefKey, value).apply()
}