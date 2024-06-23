package lnbti.gtp01.droidai.models

data class GetAllNotificationResponse(
    val success: Boolean,
    val data: List<NotificationData>
)

