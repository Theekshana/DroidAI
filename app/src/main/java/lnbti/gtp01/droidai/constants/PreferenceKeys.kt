package lnbti.gtp01.droidai.constants

/**
 * Enum class defining keys used for storing and retrieving preferences.
 *
 * This enum provides a set of keys to access specific preferences in the application's settings.
 * Each key is associated with a corresponding preference value and should be used for consistency
 * when accessing or modifying preferences.
 *
 */
object PreferenceKeys {
    /**
     * Key for storing and retrieving the selected language preference.
     * The value associated with this key represents the user's chosen language.
     */
    const val LANGUAGE = "language"
    const val LOGGED_IN_USER = "logged_in_user"
    const val APP_LAUNCHED_STATUS = "launch_status"
    const val EVENTS = "events"
    const val FIREBASE_TOKEN = "firebase_token"
    const val HEADER_ICON = "Header Icon"

}