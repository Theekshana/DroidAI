package lnbti.gtp01.droidai.models

/**
 * Data class representing the response received after saving a notification.
 *
 * @property success Indicates whether the save operation was successful.
 * @property data The notification data associated with the response.
 */
data class SaveNotificationResponse(
    val success: Boolean,
    val data: NotificationData,
)

