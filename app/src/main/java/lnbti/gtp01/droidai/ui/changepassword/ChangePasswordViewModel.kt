package lnbti.gtp01.droidai.ui.changepassword

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import lnbti.gtp01.droidai.R
import lnbti.gtp01.droidai.utils.Validations.Companion.isPasswordValid
import javax.inject.Inject

/**
 * ViewModel for handling change-password operations.
 *
 */
@HiltViewModel
class ChangePasswordViewModel @Inject constructor() : ViewModel() {
    var oldPassword: String? = null
    var newPassword: String? = null
    var confirmPassword: String? = null
    private val _changePasswordForm = MutableLiveData<ChangePasswordFormState>()
    val changePasswordForm = _changePasswordForm

    fun changePasswordDataChanged() {
        _changePasswordForm.apply {
            value = when {
                !isPasswordValid(oldPassword) -> ChangePasswordFormState(oldPasswordError = R.string.invalid_password)
                !isPasswordValid(newPassword) -> ChangePasswordFormState(newPasswordError = R.string.invalid_password)
                !confirmPassword.equals(newPassword) -> ChangePasswordFormState(confirmPasswordError = R.string.invalid_confirm_password)
                else -> ChangePasswordFormState(isDataValid = true)
            }
        }
    }
}

data class ChangePasswordFormState(
    val oldPasswordError: Int? = null,
    val newPasswordError: Int? = null,
    val confirmPasswordError: Int? = null,
    val isDataValid: Boolean = false,
)