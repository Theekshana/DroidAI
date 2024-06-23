package lnbti.gtp01.droidai.utils

import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import javax.inject.Inject

/**
 * Kotlin object that manages the connectivity check.
 */
class NetworkUtils @Inject constructor() {
    companion object {
        private var connectivityManager: ConnectivityManager? = null

        /**
         * Call the NetworkUtils.init() method to set the ConnectivityManager
         */
        fun init(connectivityManager: ConnectivityManager) {
            this.connectivityManager = connectivityManager
        }

        /**
         * can use this method from anywhere in the app to check for network
         * connectivity without passing a Context
         */
        fun isNetworkAvailable(): Boolean {
            return connectivityManager?.let { manager ->
                manager.getNetworkCapabilities(manager.activeNetwork)?.run {
                    hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                            hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                            hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
                } ?: false
            } ?: false
        }
    }

}