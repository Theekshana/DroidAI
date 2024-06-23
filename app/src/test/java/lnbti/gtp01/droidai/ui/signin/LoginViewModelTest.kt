package lnbti.gtp01.droidai.ui.signin

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
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class LoginViewModelTest {

    private val correctUserName = "Charith"
    private val correctPassword = "Charith@1234"

    private val inCorrectUserName = null
    private val inCorrectFormattedPassword = "Cha"

    private val inCorrectPassword = "Kalana@1234"

    // Run tasks synchronously
    @get:Rule
    val rule = InstantTaskExecutorRule()

    // Mocks
    @Mock
    private lateinit var userRepository: UserRepositoryImpl

    @Mock
    private lateinit var loginFormObserver: Observer<LoginFormState>

    @Mock
    private lateinit var loginResultObserver: Observer<LoginResponse?>

    // Subject under test
    private lateinit var viewModel: LoginViewModel

    // Main dispatcher for testing Coroutines
    private val mainDispatcher = Dispatchers.Unconfined

    private val testDispatcher = TestCoroutineDispatcher()

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        Dispatchers.setMain(mainDispatcher)
        viewModel = LoginViewModel(userRepository)
        viewModel.loginFormState.observeForever(loginFormObserver)
        viewModel.loginResult.observeForever(loginResultObserver)
    }

    @After
    fun tearDown() {
        viewModel.loginFormState.removeObserver(loginFormObserver)
        viewModel.loginResult.removeObserver(loginResultObserver)
        Dispatchers.resetMain()
    }

    @Test
    fun `test login success`() = testDispatcher.runBlockingTest {
        // Given
        val firebaseToken = "1234"

        viewModel.userName = correctUserName
        viewModel.password = correctPassword

        // When
        viewModel.login(firebaseToken)

        // Then
        verify(userRepository).login(firebaseToken, viewModel.userName, viewModel.password)
    }

    @Test
    fun `test login fail`() = testDispatcher.runBlockingTest {
        // Given
        val firebaseToken = "1234"

        viewModel.userName = correctUserName
        viewModel.password = inCorrectPassword

        // When
        viewModel.login(firebaseToken)

        // Then
        verify(userRepository).login(firebaseToken, viewModel.userName, viewModel.password)
    }

    @Test
    fun `test resetLiveData`() {
        // When
        viewModel.resetLiveData()

        // Then
        assert(viewModel.userName == null)
        assert(viewModel.password == null)
        assert(viewModel.loginFormState.value == null)
        assert(viewModel.loginResult.value == null)
    }

    @Test
    fun `test loginDataChanged with invalid username`() {
        // Given
        viewModel.userName = inCorrectUserName
        viewModel.password = correctPassword

        // When
        viewModel.loginDataChanged()

        // Then
        verify(loginFormObserver).onChanged(LoginFormState(userNameError = R.string.invalid_username))
    }

    @Test
    fun `test loginDataChanged with invalid password`() {
        // Given
        viewModel.userName = correctUserName
        viewModel.password = inCorrectFormattedPassword

        // When
        viewModel.loginDataChanged()

        // Then
        verify(loginFormObserver).onChanged(LoginFormState(passwordError = R.string.invalid_password))
    }

    @Test
    fun `test loginDataChanged with valid credentials`() {
        // Given
        viewModel.userName = correctUserName
        viewModel.password = correctPassword
        // When
        viewModel.loginDataChanged()

        // Then
        verify(loginFormObserver).onChanged(LoginFormState(isDataValid = true))
    }
}