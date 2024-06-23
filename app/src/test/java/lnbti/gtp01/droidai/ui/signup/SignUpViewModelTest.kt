package lnbti.gtp01.droidai.ui.signup

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import lnbti.gtp01.droidai.R
import lnbti.gtp01.droidai.models.LoginResponse
import lnbti.gtp01.droidai.repository.UserRepositoryImpl
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
class SignUpViewModelTest {
    private val validNIC = "911240807v"
    private val validFirstName = "Charith"
    private val validLastName = "Vithanage"
    private val validContactNo = "0712919248"
    private val validUserName = "Charith"
    private val validPassword = "Charith@1234"

    private val duplicateUserName = "Charith"

    private val invalidFirstName = null
    private val invalidLastName = null
    private val invalidUserName = null
    private val invalidFormattedPassword = "Cha"
    private val invalidContactNo = "071234"
    private val invalidNIC = "912345d"

    // Run tasks synchronously
    @get:Rule
    val rule = InstantTaskExecutorRule()

    // Mocks
    @Mock
    private lateinit var userRepository: UserRepositoryImpl

    @Mock
    private lateinit var registerFormObserver: Observer<RegisterFormState>

    @Mock
    private lateinit var registerResultObserver: Observer<LoginResponse?>

    // Subject under test
    private lateinit var viewModel: SignUpViewModel

    // Main dispatcher for testing Coroutines
    private val mainDispatcher = Dispatchers.Unconfined

    private val testDispatcher = TestCoroutineDispatcher()

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        Dispatchers.setMain(mainDispatcher)
        viewModel = SignUpViewModel(userRepository)
        viewModel.registerFormState.observeForever(registerFormObserver)
        viewModel.registerResult.observeForever(registerResultObserver)
    }

    @After
    fun tearDown() {
        viewModel.registerFormState.removeObserver(registerFormObserver)
        viewModel.registerResult.removeObserver(registerResultObserver)
        Dispatchers.resetMain()
    }

    @Test
    fun `test register success`() = testDispatcher.runBlockingTest {

        viewModel.userName = validUserName
        viewModel.password = validPassword
        viewModel.contactNo = validContactNo
        viewModel.firstName = validFirstName
        viewModel.lastName = validLastName
        viewModel.nic = validNIC

        // When
        viewModel.register()

        // Then
        Mockito.verify(userRepository)
            .register(
                viewModel.nic,
                viewModel.firstName,
                viewModel.lastName,
                viewModel.contactNo,
                viewModel.userName,
                viewModel.password
            )
    }

    @Test
    fun `test register fail`() = testDispatcher.runBlockingTest {
        viewModel.userName = duplicateUserName
        viewModel.password = validPassword
        viewModel.contactNo = validContactNo
        viewModel.firstName = validFirstName
        viewModel.lastName = validLastName
        viewModel.nic = validNIC


        // When
        viewModel.register()

        // Then
        Mockito.verify(userRepository)
            .register(
                viewModel.nic,
                viewModel.firstName,
                viewModel.lastName,
                viewModel.contactNo,
                viewModel.userName,
                viewModel.password
            )
    }

    @Test
    fun `test registerDataChanged with invalid username`() {
        // Given
        viewModel.userName = invalidUserName
        viewModel.password = validPassword
        viewModel.contactNo = validContactNo
        viewModel.firstName = validFirstName
        viewModel.lastName = validLastName
        viewModel.nic = validNIC
        // When
        viewModel.registerDataChanged()

        // Then
        Mockito.verify(registerFormObserver)
            .onChanged(RegisterFormState(userNameError = R.string.invalid_username))
    }

    @Test
    fun `test registerDataChanged with invalid password`() {
        // Given
        viewModel.password = invalidFormattedPassword
        viewModel.userName = validUserName
        viewModel.contactNo = validContactNo
        viewModel.firstName = validFirstName
        viewModel.lastName = validLastName
        viewModel.nic = validNIC
        // When
        viewModel.registerDataChanged()

        // Then
        Mockito.verify(registerFormObserver)
            .onChanged(RegisterFormState(passwordError = R.string.invalid_password))
    }

    @Test
    fun `test registerDataChanged with invalid nic`() {
        // Given
        viewModel.nic = invalidNIC
        viewModel.userName = validUserName
        viewModel.password = validPassword
        viewModel.contactNo = validContactNo
        viewModel.firstName = validFirstName
        viewModel.lastName = validLastName
        // When
        viewModel.registerDataChanged()

        // Then
        Mockito.verify(registerFormObserver)
            .onChanged(RegisterFormState(nicError  = R.string.invalid_nic))
    }

    @Test
    fun `test registerDataChanged with invalid contact no`() {
        // Given
        viewModel.contactNo = invalidContactNo
        viewModel.userName = validUserName
        viewModel.password = validPassword
        viewModel.firstName = validFirstName
        viewModel.lastName = validLastName
        viewModel.nic = validNIC
        // When
        viewModel.registerDataChanged()

        // Then
        Mockito.verify(registerFormObserver)
            .onChanged(RegisterFormState(contactNoError = R.string.invalid_contactno))
    }

    @Test
    fun `test registerDataChanged with invalid first name`() {
        // Given
        viewModel.firstName = invalidFirstName
        viewModel.userName = validUserName
        viewModel.password = validPassword
        viewModel.contactNo = validContactNo
        viewModel.lastName = validLastName
        viewModel.nic = validNIC
        // When
        viewModel.registerDataChanged()

        // Then
        Mockito.verify(registerFormObserver)
            .onChanged(RegisterFormState(firstNameError = R.string.invalid_firstname))
    }

    @Test
    fun `test registerDataChanged with invalid last name`() {
        // Given
        viewModel.lastName = invalidLastName
        viewModel.userName = validUserName
        viewModel.password = validPassword
        viewModel.contactNo = validContactNo
        viewModel.firstName = validFirstName
        viewModel.nic = validNIC
        // When
        viewModel.registerDataChanged()

        // Then
        Mockito.verify(registerFormObserver)
            .onChanged(RegisterFormState(lastNameError = R.string.invalid_lastname))
    }

    @Test
    fun `test registerDataChanged with valid credentials`() {
        // Given
        viewModel.userName = validUserName
        viewModel.password = validPassword
        viewModel.contactNo = validContactNo
        viewModel.firstName = validFirstName
        viewModel.lastName = validLastName
        viewModel.nic = validNIC
        // When
        viewModel.registerDataChanged()

        // Then
        Mockito.verify(registerFormObserver).onChanged(RegisterFormState(isDataValid = true))
    }
}