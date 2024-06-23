package lnbti.gtp01.droidai.ui.signup

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import lnbti.gtp01.droidai.R
import lnbti.gtp01.droidai.models.LoginResponse
import lnbti.gtp01.droidai.repository.UserRepositoryImpl
import lnbti.gtp01.droidai.utils.Validations.Companion.isMobileNumberValid
import lnbti.gtp01.droidai.utils.Validations.Companion.isNICValid
import lnbti.gtp01.droidai.utils.Validations.Companion.isPasswordValid
import lnbti.gtp01.droidai.utils.Validations.Companion.isUserNameValid
import javax.inject.Inject

/**
 * ViewModel for handling login-related operations.
 *
 * @property userRepository The repository for managing user data.
 * @property loginFormState LiveData for tracking the state of the login form.
 * @property loginResult LiveData for observing the login result.
 * @property isDialogVisible LiveData indicating the visibility of a dialog.
 */
@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val userRepository: UserRepositoryImpl
) : ViewModel() {
    var nic: String? = null
    var firstName: String? = null
    var lastName: String? = null
    var contactNo: String? = null
    var userName: String? = null
    var password: String? = null
    private val _registerFormState = MutableLiveData<RegisterFormState>()
    val registerFormState = _registerFormState
    private val _registerResult = MutableLiveData<LoginResponse?>()
    val registerResult = _registerResult

    //Dialog Visibility Live Data
    private val _isDialogVisible = MutableLiveData<Boolean>()
    val isDialogVisible get() = _isDialogVisible

    /**
     * Initiates the login process.
     *
     * @param deviceID The unique identifier for the device.
     * @param email The user's email address.
     * @param password The user's password.
     */
    fun register() {

        // Handle the case when the device is connected to the network
        viewModelScope.launch {

            // can be launched in a separate asynchronous job
            val result =
                userRepository.register(
                    nic,
                    firstName, lastName, contactNo,
                    userName, password
                )
            _registerResult.value = result
        }
    }

    /**
     * Validates and updates the login form state based on the provided username and password.
     *
     * @param username The user's entered username.
     * @param password The user's entered password.
     */
    fun registerDataChanged() {
        _registerFormState.apply {
            value = when {
                !isNICValid(nic) -> RegisterFormState(nicError  = R.string.invalid_nic)
                firstName.isNullOrBlank() -> RegisterFormState(firstNameError = R.string.invalid_firstname)
                lastName.isNullOrBlank() -> RegisterFormState(lastNameError = R.string.invalid_lastname)
                !isMobileNumberValid(contactNo) -> RegisterFormState(contactNoError = R.string.invalid_contactno)
                !isUserNameValid(userName) -> RegisterFormState(userNameError = R.string.invalid_username)
                !isPasswordValid(password) -> RegisterFormState(passwordError = R.string.invalid_password)
                else -> RegisterFormState(isDataValid = true)
            }
        }
    }
}

data class RegisterFormState(
    val nicError: Int? = null,
    val firstNameError: Int? = null,
    val lastNameError: Int? = null,
    val contactNoError: Int? = null,
    val userNameError: Int? = null,
    val passwordError: Int? = null,
    val isDataValid: Boolean = false
)