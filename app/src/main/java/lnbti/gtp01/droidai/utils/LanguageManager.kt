package lnbti.gtp01.droidai.utils

import android.content.Context
import android.content.res.Configuration
import lnbti.gtp01.droidai.utils.SharedPreferencesManager.Companion.getSelectedLanguage
import java.util.Locale

class LanguageManager(private val context: Context) {

    fun loadLanguage() {
        val language = getSelectedLanguage()
        setLocale(language)
    }

    private fun setLocale(language: String?) {
        language?.let {
            val locale = Locale(it)
            Locale.setDefault(locale)
            val config = Configuration()
            config.locale = locale
            context.resources.updateConfiguration(
                config,
                context.resources.displayMetrics
            )
        }
    }
}