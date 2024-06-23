package lnbti.gtp01.droidai.ui.airesponse

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.installations.Utils
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import lnbti.gtp01.droidai.models.Inquiry
import lnbti.gtp01.droidai.models.SubmitResponseRequest
import lnbti.gtp01.droidai.repository.InquiryRepositoryImpl
import java.util.UUID
import javax.inject.Inject

/**
 * ViewModel for the home screen of the inspector.
 * This ViewModel fetches inquiries data and updates inquiries with AI responses.
 * It also holds data related to progress dialogs and inspector responses.
 * @param repository The repository for fetching inquiries data.
 * @param aiRepository The repository for submitting AI responses.
 */
@HiltViewModel
class InspectorHomeViewModel @Inject constructor(
    private val repository: InquiryRepositoryImpl,

    ) : ViewModel() {

    // LiveData to hold the list of inquiries
    private val _inquiryResult = MutableLiveData<List<Inquiry?>>()
    val inquiryResult: LiveData<List<Inquiry?>> get() = _inquiryResult

    // LiveData to hold the inquiry result
     val _viewInquiryResult = MutableLiveData<Inquiry>()
    val viewInquiryResult: LiveData<Inquiry> get() = _viewInquiryResult


    /**
     * Fetches inquiries data from the repository.
     * Shows a progress dialog while fetching data.
     * Dismisses the progress dialog after data fetching is complete.
     */
    fun fetchDataForInspectors() {

        //isProgressDialogVisible.value = true
        viewModelScope.launch {
            try {
                // Fetch inquiries data from the repository
                val response = repository.getAllInquiries()

                if (response.isSuccessful) {
                    val inquiry = response.body()
                    _inquiryResult.value = inquiry?.data

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

    /**
     * Updates the viewed inquiry with an AI response.
     * @param aiResponse The AI response to be submitted.
     */
    fun updateWithAiResponse(aiResponse: SubmitResponseRequest) {
        viewModelScope.launch {
            try {
                val inquiryToUpdate = _viewInquiryResult.value

                inquiryToUpdate?.let { existingInquiry ->
                    // Update the response field of the existing inquiry
                    existingInquiry.response = aiResponse.response

                    // Submit the updated inquiry to the repository
                    val response = repository.submitResponse(aiResponse)

                    if (response.isSuccessful) {
                        // Update successful, update the LiveData with the modified inquiry
                        _viewInquiryResult.value = existingInquiry

                    } else {
                        // Update failed, handle the error
                    }
                }
            } catch (e: Exception) {
                // Handle exceptions
                e.printStackTrace()
            }
        }
    }

    /**
     * Sets the inquiry to be viewed in the ViewModel.
     *
     * @param inquiry The inquiry to be viewed.
     */
    fun viewInquiry(inquiry: Inquiry) {
        _viewInquiryResult.value = inquiry
    }

}
