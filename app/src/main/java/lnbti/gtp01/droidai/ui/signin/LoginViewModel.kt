package lnbti.gtp01.droidai.ui.signin

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import lnbti.gtp01.droidai.R
import lnbti.gtp01.droidai.models.LoginResponse
import lnbti.gtp01.droidai.repository.UserRepositoryImpl
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
class LoginViewModel @Inject constructor(
    private val userRepository: UserRepositoryImpl
) : ViewModel() {
    var userName :String? = null
    var password :String? = null
    private val _loginForm = MutableLiveData<LoginFormState>()
    val loginFormState = _loginForm
    private val _loginResult = MutableLiveData<LoginResponse?>()
    val loginResult = _loginResult

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
    fun login(firebaseToken: String?) {

        // Handle the case when the device is connected to the network
        viewModelScope.launch {

            // can be launched in a separate asynchronous job
            val result =
                userRepository.login(
                    firebaseToken, userName, password

                )
            _loginResult.value = result
        }
    }

    fun resetLiveData() {
        userName = null
        password = null
        loginFormState.value = null // Or an equivalent default state
        loginResult.value = null
    }

    /**
     * Validates and updates the login form state based on the provided username and password.
     *
     * @param username The user's entered username.
     * @param password The user's entered password.
     */
    fun loginDataChanged() {
        _loginForm.apply {
            value = when {
                !isUserNameValid(userName) -> LoginFormState(userNameError = R.string.invalid_username)
                !isPasswordValid(password) -> LoginFormState(passwordError = R.string.invalid_password)
                else -> LoginFormState(isDataValid = true)
            }
        }
    }
}

/**
 * Represents the state of a login form.
 *
 * @property userNameError An optional error code associated with the Username input.
 *                     If `null`, there is no error; otherwise, it indicates an issue with the NIC input.
 * @property passwordError An optional error code associated with the password input.
 *                          If `null`, there is no error; otherwise, it indicates an issue with the password input.
 * @property isDataValid A boolean indicating whether the overall form data is considered valid.
 *                       It is `true` if both NIC and password inputs are valid; otherwise, it is `false`.
 *
 * @constructor Creates a [LoginFormState] with optional error codes for NIC and password and an overall
 *               validity status.
 *
 * @param userNameError The error code associated with the Username input, or `null` if there is no error.
 * @param passwordError The error code associated with the password input, or `null` if there is no error.
 * @param isDataValid The overall validity status of the form data. Default is `false`.
 */
data class LoginFormState(
    val userNameError: Int? = null,
    val passwordError: Int? = null,
    val isDataValid: Boolean = false
)