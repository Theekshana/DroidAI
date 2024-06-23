package lnbti.gtp01.droidai.ui.notifications

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import lnbti.gtp01.droidai.models.NotificationData
import lnbti.gtp01.droidai.repository.InquiryRepository
import javax.inject.Inject

/**
 * ViewModel class for managing notifications, responsible for interacting with the [InquiryRepository]
 * to save notifications and retrieving all existing notifications.
 *
 * @property repository The [InquiryRepository] instance used for saving and retrieving notifications.
 */
@HiltViewModel
class NotificationViewModel @Inject constructor(
    private val repository: InquiryRepository,
) : ViewModel() {

    // LiveData to indicate whether the notification was saved successfully
    val _saveNotification = MutableLiveData<Boolean>()
    val saveNotification: LiveData<Boolean> get() = _saveNotification

    // LiveData to hold the list of notifications
    private val _notificationResult = MutableLiveData<List<NotificationData?>>()
    val notificationResult: LiveData<List<NotificationData?>> get() = _notificationResult

    // LiveData to control the visibility of progress dialog
    private val _isProgressDialogVisible = MutableLiveData<Boolean>()
    val isProgressDialogVisible get() = _isProgressDialogVisible

    /**
     * Saves the given notification data.
     *
     * @param notification The notification data to be saved.
     */
    fun saveNotification(
        notification: NotificationData,
    ) {
        isProgressDialogVisible.value = true
        viewModelScope.launch {
            try {
                // Submit the notification to the repository
                val response = repository.saveNotification(notification)
                // Set the value of saveNotification LiveData based on the response
                _saveNotification.value = response.isSuccessful
                isProgressDialogVisible.value = false

            } catch (e: Exception) {
                // Handle network or other errors
                _saveNotification.value = false
                e.printStackTrace()
            }
        }
    }

    /**
     * Retrieves all existing notifications from the repository.
     */
    fun getAllNotification() {

        isProgressDialogVisible.value = true
        viewModelScope.launch {
            try {
                // Fetch notifications data from the repository
                val response = repository.getAllNotification()

                if (response.isSuccessful) {
                    val notificationResponse = response.body()
                    // Update the value of notificationResult LiveData
                    _notificationResult.value = notificationResponse?.data
                    isProgressDialogVisible.value = false

                } else {
                    Log.e(
                        "InspectorHomeViewModel",
                        "Error: ${response.code()}, ${response.message()}"
                    )
                }

            } catch (e: Exception) {
                // Handle exceptions
                e.printStackTrace()
            }
        }
    }

}
