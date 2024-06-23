package lnbti.gtp01.droidai.ui.inquiry

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import lnbti.gtp01.droidai.models.UserInquiry
import lnbti.gtp01.droidai.repository.InquiryRepository
import javax.inject.Inject

/**
 * ViewModel class for managing inquiries, responsible for interacting with the [InquiryRepository]
 * to submit inquiries and handling the corresponding UI-related data.
 *
 * @property[repository] The [InquiryRepository] instance used for submitting inquiries.
 */
@HiltViewModel
class InquiryViewModel @Inject constructor(
    private val repository: InquiryRepository,
) : ViewModel() {

    // LiveData to hold the submission status of an inquiry
    val _submitInquiry = MutableLiveData<Boolean>()
    val submitInquiry: LiveData<Boolean> get() = _submitInquiry

    /**
     * Submits an inquiry along with an image URI.
     * @param userInquiry The inquiry to be submitted.
     * @param imageUri The URI of the image associated with the inquiry.
     */
    fun submitInquiry(
        userInquiry: UserInquiry,
        imageUri: Uri,
    ) {
        viewModelScope.launch {
            try {
                //Submit inquiry
                val response = repository.submitInquiry(userInquiry, imageUri)
                //Check if the submission was successful
                _submitInquiry.value = response.isSuccessful

            } catch (e: Exception) {
                // Handle network or other errors
                _submitInquiry.value = false
                e.printStackTrace()
            }
        }
    }

}
