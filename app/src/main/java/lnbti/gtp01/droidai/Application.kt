package lnbti.gtp01.droidai

import android.app.Application
import android.content.Context
import android.util.Log
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.HiltAndroidApp
import lnbti.gtp01.droidai.constants.PreferenceKeys
import lnbti.gtp01.droidai.utils.SharedPreferencesManager

/**
 * Custom [Application] class for Application.
 *
 * This class is annotated with [@HiltAndroidApp]
 * to enable Hilt for dependency injection throughout the application.
 */
@HiltAndroidApp
class Application : Application() {

    override fun onCreate() {
        super.onCreate()

        FirebaseMessaging.getInstance().token
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Token retrieval success
                    val token = task.result
                    SharedPreferencesManager.savePreference(PreferenceKeys.FIREBASE_TOKEN, token)
                    Log.d("FirebaseService", "Token: $token")
                } else {
                    // Token retrieval failed
                    Log.w(
                        "FirebaseService",
                        "Fetching FCM registration token failed",
                        task.exception
                    )
                }
            }

        // Initialize SharedPreferencesManager with the application's SharedPreferences.
        getSharedPreferences(
            getString(R.string.preference_file_key),
            Context.MODE_PRIVATE
        ).let { SharedPreferencesManager.init(it) }
    }

}