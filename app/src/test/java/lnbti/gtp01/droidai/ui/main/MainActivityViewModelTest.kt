package lnbti.gtp01.droidai.ui.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import lnbti.gtp01.droidai.models.User
import lnbti.gtp01.droidai.utils.getOrAwaitValue
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.jupiter.api.Assertions.*
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class MainActivityViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = TestCoroutineDispatcher()

    private lateinit var viewModel: MainActivityViewModel

    @Mock
    private lateinit var isProgressDialogVisibleObserver: Observer<Boolean>

    @Mock
    private lateinit var isShowHamburgerMenuObserver: Observer<Boolean>

    @Mock
    private lateinit var loggedInUserObserver: Observer<User>

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        Dispatchers.setMain(testDispatcher)
        viewModel = MainActivityViewModel()
        viewModel.isProgressDialogVisible.observeForever(isProgressDialogVisibleObserver)
        viewModel.loggedInUser.observeForever(loggedInUserObserver)
        viewModel.showHamburgerMenu.observeForever(isShowHamburgerMenuObserver)
    }

    @Test
    fun `setProgressDialogVisible updates LiveData correctly`() {
        viewModel.setProgressDialogVisible(true)
        assertThat(viewModel.isProgressDialogVisible.getOrAwaitValue(), `is`(true))

        viewModel.setProgressDialogVisible(false)
        assertThat(viewModel.isProgressDialogVisible.getOrAwaitValue(), `is`(false))
    }

    @Test
    fun `showHamburgerMenu updates LiveData correctly`() {
        viewModel.showHamburgerMenu(true)
        assertThat(viewModel.showHamburgerMenu.getOrAwaitValue(), `is`(true))

        viewModel.showHamburgerMenu(false)
        assertThat(viewModel.showHamburgerMenu.getOrAwaitValue(), `is`(false))
    }

    @Test
    fun `test success message live data updates`() {
        val testSuccessMessage = "Test Success"
        // If your ViewModel can post null success messages, test that scenario
        viewModel.showSuccessDialog(testSuccessMessage)

        // Assert that _successMessage LiveData is updated to null as expected
        viewModel.successMessage.observeForever { newValue ->
            assertEquals(testSuccessMessage, newValue)
        }
    }

    @Test
    fun `test error message live data updates`() {
        val testErrorMessage = "Test Error"
        viewModel.showErrorDialog(testErrorMessage)

        viewModel.errorMessage.observeForever { newValue ->
            assertEquals(testErrorMessage, newValue)
        }
    }

    @Test
    fun `test warn message live data updates`() {
        val testWarnMessage = "Test Warn"
        viewModel.showWarnDialog(testWarnMessage)

        viewModel.warnMessage.observeForever { newValue ->
            assertEquals(testWarnMessage, newValue)
        }
    }

    @Test
    fun `setFragmentName updates LiveData value`() {
        val viewModel = MainActivityViewModel()
        val testName = "TestFragment"
        val expectedFragment = "TestFragment"

        viewModel.setFragment(testName)

        assertEquals(expectedFragment, viewModel.fragment.getOrAwaitValue())
    }

    @Test
    fun `when setLoggedInUser is called, loggedInUser LiveData is updated`() {
        val inputUser =
            User(
                "911240800V", "charithvin@gmail.com", "Charith", "Vithanage",
                "Male", "Farmer", "1991-05-03", "0712345678"
            )

        val expectedUser =
            User(
                "911240800V", "charithvin@gmail.com", "Charith", "Vithanage",
                "Male", "Farmer", "1991-05-03", "0712345678"
            )


        viewModel.setLoggedInUser(inputUser)

        assertEquals(expectedUser, viewModel.loggedInUser.value)
    }


    @After
    fun tearDown() {
        viewModel.isProgressDialogVisible.removeObserver(isProgressDialogVisibleObserver)
        viewModel.loggedInUser.removeObserver(loggedInUserObserver)
        viewModel.showHamburgerMenu.removeObserver(isShowHamburgerMenuObserver)
        Dispatchers.resetMain()
    }
}