package lnbti.gtp01.droidai.ui.changepassword

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import lnbti.gtp01.droidai.R
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.jupiter.api.Assertions.*
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class ChangePasswordViewModelTest {

    private val correctOldPassword = "Charith@1234"
    private val correctNewPassword = "Charith@1234"
    private val correctConfirmPassword = "Charith@1234"

    private val inCorrectFormattedOldPassword = "Cha"
    private val inCorrectFormattedNewPassword = "Cha"
    private val invalidConfirmPassword = "Charith@123"

    // Run tasks synchronously
    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Mock
    private lateinit var changePasswordFormObserver: Observer<ChangePasswordFormState>

    // Subject under test
    private lateinit var viewModel: ChangePasswordViewModel

    // Main dispatcher for testing Coroutines
    private val mainDispatcher = Dispatchers.Unconfined

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(mainDispatcher)
        viewModel = ChangePasswordViewModel()
        viewModel.changePasswordForm.observeForever(changePasswordFormObserver)
    }

    @After
    fun tearDown() {
        viewModel.changePasswordForm.removeObserver(changePasswordFormObserver)
        Dispatchers.resetMain()
    }

    @Test
    fun `test changePasswordDataChanged with invalid old password`() {
        // Given
        viewModel.oldPassword = inCorrectFormattedOldPassword
        viewModel.newPassword = correctNewPassword
        viewModel.confirmPassword = correctConfirmPassword

        // When
        viewModel.changePasswordDataChanged()

        // Then
        Mockito.verify(changePasswordFormObserver)
            .onChanged(ChangePasswordFormState(oldPasswordError = R.string.invalid_password))
    }

    @Test
    fun `test changePasswordDataChanged with invalid new password`() {
        // Given
        viewModel.oldPassword = correctOldPassword
        viewModel.newPassword = inCorrectFormattedNewPassword
        viewModel.confirmPassword = correctConfirmPassword

        // When
        viewModel.changePasswordDataChanged()

        // Then
        Mockito.verify(changePasswordFormObserver)
            .onChanged(ChangePasswordFormState(newPasswordError = R.string.invalid_password))
    }

    @Test
    fun `test changePasswordDataChanged with invalid confirm password`() {
        // Given
        viewModel.oldPassword = correctOldPassword
        viewModel.newPassword = correctNewPassword
        viewModel.confirmPassword = invalidConfirmPassword

        // When
        viewModel.changePasswordDataChanged()

        // Then
        Mockito.verify(changePasswordFormObserver)
            .onChanged(ChangePasswordFormState(confirmPasswordError = R.string.invalid_confirm_password))

    }

    @Test
    fun `test loginDataChanged with valid credentials`() {
        // Given
        viewModel.oldPassword = correctOldPassword
        viewModel.newPassword = correctNewPassword
        viewModel.confirmPassword = correctConfirmPassword

        // When
        viewModel.changePasswordDataChanged()

        // Then
        Mockito.verify(changePasswordFormObserver).onChanged(ChangePasswordFormState(isDataValid = true))
    }
}