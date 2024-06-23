package lnbti.gtp01.droidai.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import lnbti.gtp01.droidai.models.Inquiry
import lnbti.gtp01.droidai.repository.InquiryRepository
import javax.inject.Inject

/**
 * ViewModel class for handling home screen data and UI logic.
 * Uses Hilt for dependency injection.
 */
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: InquiryRepository,
) : ViewModel() {

    // LiveData to hold the list of inquiries
    private val _inquiryResult = MutableLiveData<List<Inquiry?>>()
    val inquiryResult: LiveData<List<Inquiry?>> get() = _inquiryResult

  /*  // LiveData to control the visibility of progress dialog
    private val _isProgressDialogVisible = MutableLiveData<Boolean>()
    val isProgressDialogVisible get() = _isProgressDialogVisible*/

    private val _phoneNumberToDial = MutableLiveData<String>()
    val phoneNumberToDial: LiveData<String> get() = _phoneNumberToDial

    private val _viewInquiryResult = MutableLiveData<Inquiry?>()
    val viewInquiryResult: LiveData<Inquiry?> get() = _viewInquiryResult

    /**
     * Fetches inquiries data from the repository for the specified user NIC.
     * Shows a progress dialog while fetching data.
     * Dismisses the progress dialog after data fetching is complete.
     * @param createdUserNIC the NIC of the user for whom inquiries are to be fetched
     */
    fun fetchData(createdUserNIC: String) {

        // Show progress dialog
        //isProgressDialogVisible.value = true

        viewModelScope.launch {
            try {

                val response = repository.getInquiriesByFarmer(createdUserNIC)

                if (response.isSuccessful) {

                    val inquiry = response.body()
                    _inquiryResult.value = inquiry?.data

                    delay(500)
                    // Dismiss progress dialog after data fetching is complete
                    //isProgressDialogVisible.value = false

                } else {

                    Log.e("HomeViewModel", "Error: ${response.code()}, ${response.message()}")
                }

            } catch (e: Exception) {

                // Handle exceptions
                e.printStackTrace()

            }

        }

    }

    /**
     * Sets the phone number to dial
     * @param phoneNumber The phone number to be set for dialing
     */
    fun setPhoneNumberToDial(phoneNumber: String) {

        _phoneNumberToDial.value = phoneNumber

    }

    fun viewInquiry(inquiry: Inquiry?) {

        _viewInquiryResult.value = inquiry

    }

}
