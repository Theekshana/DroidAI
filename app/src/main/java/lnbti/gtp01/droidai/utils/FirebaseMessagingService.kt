package lnbti.gtp01.droidai.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import lnbti.gtp01.droidai.R
import lnbti.gtp01.droidai.constants.StringConstants.ACTION_NOTIFICATION_RECEIVED
import lnbti.gtp01.droidai.models.NotificationData
import lnbti.gtp01.droidai.ui.main.MainActivity
import kotlin.random.Random


/**
 * Service class for handling Firebase Cloud Messaging (FCM) messages.
 * Extends [FirebaseMessagingService] to receive and process incoming FCM messages.
 */

const val channelId = "notification_channel"
const val channelName = "lnbti.gtp01.droidai.service"

class FirebaseMessagingService : FirebaseMessagingService() {

    companion object {
        // Flag to track whether a notification has been received
        private var notificationReceived: Boolean = false

    }

    /**
     * Overrides the [onMessageReceived] method to handle incoming FCM messages.
     * If the message contains a notification, it triggers the display of a notification.
     * Additionally, it checks for a data payload to handle background or terminated states.
     *
     * @param remoteMessage The FCM message received from the server.
     */

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        if (remoteMessage.notification != null) {
            notificationReceived = true
            notifyActivity()
            remoteMessage.notification?.body?.let { body ->
                remoteMessage.notification!!.title?.let { title ->
                    val intent = Intent(this, MainActivity::class.java).apply {
                        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or FLAG_ACTIVITY_CLEAR_TASK)
                        putExtra("title", title)
                        putExtra("message", body)

                    }

                    // Check if the message contains a data payload
                    remoteMessage.data.isNotEmpty().let { hasDataPayload ->
                        if (hasDataPayload) {
                            // Extract data from the payload
                            val title = remoteMessage.data["title"]
                            val message = remoteMessage.data["message"]

                            // Create a NotificationData object
                            val notificationData = NotificationData(
                                title,
                                message,
                            )

                            // Put the Parcelable data into the Intent
                            intent.putExtra("data", notificationData)
                        }

                        sendNotification(intent)

                    }

                    // Log the device token
                    val deviceToken = getDeviceToken()
                    Log.d("FirebaseService", "Device Token: $deviceToken")

                }
            }
        }
    }

    /**
     * Retrieves the FCM token and logs it.
     */
    private fun getDeviceToken() {
        FirebaseMessaging.getInstance().token
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Token retrieval success
                    val token = task.result
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
    }

    /**
     * Shows a notification based on the title and message received from an FCM message.
     * Creates an intent to launch when the notification is clicked.
     *
     * @param title The title of the notification.
     * @param message The message body of the notification.
     */
    private fun sendNotification(intent: Intent) {
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE

        )

        val builder: NotificationCompat.Builder =
            NotificationCompat.Builder(applicationContext, channelId)
                .setContentTitle(intent.getStringExtra("title"))
                .setContentText(intent.getStringExtra("message"))
                .setSmallIcon(R.drawable.notification)
                .setAutoCancel(true)
                .setVibrate(longArrayOf(1000, 1000, 1000, 1000))
                .setOnlyAlertOnce(true)
                .setContentIntent(pendingIntent)

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notificationID = Random.nextInt()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel =
                NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(notificationChannel)
        }

        notificationManager.notify(notificationID, builder.build())
    }

    /**
     * Notifies the activity about the received notification by sending a local broadcast.
     */
    private fun notifyActivity() {
        val intent = Intent(ACTION_NOTIFICATION_RECEIVED)
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
    }
}