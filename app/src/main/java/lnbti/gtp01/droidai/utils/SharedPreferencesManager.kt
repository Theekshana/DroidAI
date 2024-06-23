package lnbti.gtp01.droidai.utils

import android.content.SharedPreferences
import lnbti.gtp01.droidai.constants.PreferenceKeys
import lnbti.gtp01.droidai.constants.StringConstants.SINHALA
import lnbti.gtp01.droidai.interfaces.SuccessListener
import lnbti.gtp01.droidai.utils.SharedPreferencesManager.Companion.sharedPreferences

/**
 * A utility class for managing shared preferences.
 *
 * @property sharedPreferences The shared preferences instance.
 */
class SharedPreferencesManager {

    companion object {
        private var sharedPreferences: SharedPreferences? = null

        /**
         * Initialize the SharedPreferencesManager with the provided SharedPreferences instance.
         *
         * @param sharedPreferences The SharedPreferences instance to be used for storing preferences.
         */
        fun init(sharedPreferences: SharedPreferences) {
            this.sharedPreferences =
                sharedPreferences

        }

        /**
         * Get the selected language value from the shared preferences.
         *
         * Default language is English.
         *
         * @return The selected language code. Returns [StringConstants.ENGLISH] if not set.
         */
        fun getSelectedLanguage() =
            sharedPreferences?.getString(PreferenceKeys.LANGUAGE, SINHALA)

        /**
         * Update the language preference value in the shared preferences.
         *
         * @param jsonString The new language code to be stored.
         */
        fun updateSelectedLanguage(
            jsonString: String?
        ) {
            sharedPreferences?.edit()?.run {
                putString(PreferenceKeys.LANGUAGE, jsonString)
                apply()
            }
        }

        fun savePreference(
            key: String?,
            jsonString: String?, list: SuccessListener? = null
        ) {
            sharedPreferences?.edit()?.run {
                putString(key, jsonString)
                apply()
                list?.onSuccess()
            }
        }

        fun getPreference(key: String): String? =
            sharedPreferences?.getString(key, null)

        fun getPreferenceBool(key: String): Boolean? =
            sharedPreferences?.getBoolean(key, false)

        fun savePreferenceBool(
            key: String?,
            value: Boolean, list: SuccessListener? = null
        ) {
            sharedPreferences?.edit()?.run {
                putBoolean(key, value)
                apply()
                list?.onSuccess()
            }
        }

        fun clearAllPref(list: SuccessListener? = null) {
            sharedPreferences?.edit()?.run {
                clear()
                apply()
                list?.onSuccess()
            }
        }

    }
}
