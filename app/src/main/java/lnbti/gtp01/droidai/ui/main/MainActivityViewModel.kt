package lnbti.gtp01.droidai.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import lnbti.gtp01.droidai.constants.StringConstants.WELCOME_FRAGMENT
import lnbti.gtp01.droidai.models.User
import javax.inject.Inject

/**
 * ViewModel for the main activity of your application.
 *
 * This ViewModel is responsible for managing the state and data related to the main activity,
 * including the currently displayed fragment, the status of the bottom menu, expanded states,
 * and whether search results are empty.
 *
 * @property _fragment The MutableLiveData that stores the currently displayed fragment.
 * @property fragment The LiveData property for accessing the currently displayed fragment.
 * @property _fragmentName The MutableLiveData that stores the currently page header.
 * @property fragmentName The LiveData property for accessing the currently displayed page header.
 */

@HiltViewModel
class MainActivityViewModel @Inject constructor() :
    ViewModel() {
    private val _loggedInUser = MutableLiveData<User>(null)
    val loggedInUser get() = _loggedInUser

    private val _showHamburgerMenu = MutableLiveData(true)
    val showHamburgerMenu get() = _showHamburgerMenu

    private val _progressAmount = MutableLiveData(0)
    val progressAmount get() = _progressAmount

    private val _fragment = MutableLiveData(WELCOME_FRAGMENT)
    val fragment get() = _fragment

    private val _fragmentName = MutableLiveData<String?>(null)
    val fragmentName get() = _fragmentName

    //Progress Dialog Visibility Live Data
    private val _isProgressDialogVisible = MutableLiveData<Boolean>()
    val isProgressDialogVisible get() = _isProgressDialogVisible

    private val _updateLabels = MutableLiveData<Boolean?>(true)
    val updateLabels: LiveData<Boolean?> get() = _updateLabels
    fun setProgressDialogVisible(showStatus: Boolean) {
        _isProgressDialogVisible.value = showStatus
    }

    fun setLoggedInUser(user: User) {
        _loggedInUser.value = user
    }

    //Success Message Dialog Visibility Live Data
    private val _successMessage = MutableLiveData<String?>()
    val successMessage get() = _successMessage

    fun showSuccessDialog(successMessage: String?) {
        _successMessage.value = successMessage
    }

    fun showHamburgerMenu(showStatus: Boolean) {
        _showHamburgerMenu.value = showStatus
    }

    //Warn Message Dialog Visibility Live Data
    private val _warnMessage = MutableLiveData<String?>()
    val warnMessage get() = _warnMessage

    fun showWarnDialog(warnMessage: String?) {
        _warnMessage.value = warnMessage
    }

    //Progress Dialog Visibility Live Data
    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage get() = _errorMessage

    fun showErrorDialog(errorMessage: String?) {
        _errorMessage.value = errorMessage
    }

    /**
     * Sets the currently displayed fragment.
     *
     * @param fragment The string identifier of the fragment.
     */
    fun setFragment(fragment: String) {
        _fragment.value = fragment
    }

    /**
     * Sets the update status of the bottom menu.
     *
     * @param isUpdateStatus A boolean indicating whether the bottom menu status should be updated.
     */
    fun setUpdateLabels(isUpdateStatus: Boolean) {
        _updateLabels.value = isUpdateStatus
    }

    /**
     * Progress percentage of the progress bar.
     */
    fun setProgressAmount(amount: Int) {
        _progressAmount.value = amount
    }
}