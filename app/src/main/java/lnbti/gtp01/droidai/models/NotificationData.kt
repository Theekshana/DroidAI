package lnbti.gtp01.droidai.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Parcelable data class representing notification data.
 *
 * @property title The title of the notification.
 * @property message The message content of the notification.
 */
@Parcelize
data class NotificationData(
    val title: String? = null,
    val message: String? = null,
) : Parcelable
