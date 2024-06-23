package lnbti.gtp01.droidai.ui.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.setMain
import lnbti.gtp01.droidai.models.InquiriesServerResponse
import lnbti.gtp01.droidai.models.Inquiry
import lnbti.gtp01.droidai.repository.InquiryRepository
import okhttp3.MediaType
import okhttp3.ResponseBody
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.Response

/**
 * Unit tests for [HomeViewModel].
 */
@ExperimentalCoroutinesApi
@Suppress("DEPRECATION")
@RunWith(MockitoJUnitRunner::class)
class HomeViewModelTest {

    // Coroutine dispatcher for testing
    private val testDispatcher = TestCoroutineDispatcher()

    // Rule to make LiveData work synchronously
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    // Mocked repository
    @Mock
    private lateinit var inquiryRepository: InquiryRepository

    // ViewModel to be tested
    private lateinit var viewModel: HomeViewModel

    /**
     * Setup method to initialize necessary components before each test.
     */
    @Before
    fun setup() {

        // Initialize Mockito annotations
        MockitoAnnotations.initMocks(this)
        // Set main dispatcher for coroutines
        Dispatchers.setMain(testDispatcher)
        // Initialize ViewModel with mocked repositories
        viewModel = HomeViewModel(inquiryRepository)

    }

    /**
     * Test case to verify successful fetching of data.
     */
    @Test
    fun fetchData_shouldHandleSuccessfulDataFetch() {

        val dummyNIC = "911111111v"
        val mockInquiries = listOf(
            Inquiry(
                "userNIC1",
                "https://example.com/image1.jpg",
                "Maharagama",
                "2024-02-07",
                "2 months",
                "true",
                "problem one",
                "2024-01-27T07:54:55.730Z",
                "1"
            )
            // Add more mock inquiries if needed
        )
        runBlocking {

            val inquiriesServerResponse = Response.success(
                InquiriesServerResponse(
                    data = mockInquiries, "Data fetched Successfully", true
                )
            )

            Mockito.`when`(inquiryRepository.getInquiriesByFarmer(dummyNIC)).thenReturn(
                inquiriesServerResponse
            )

            viewModel.fetchData(dummyNIC)

            // Advance time by 2 seconds to simulate delay
            testDispatcher.scheduler.apply { advanceTimeBy(2000); runCurrent() }

            // Verify that inquiryResult LiveData is updated with expected data
            assertEquals(mockInquiries, viewModel.inquiryResult.value)

        }
    }

    /**
     * Test case to verify error handling while fetching data.
     */
    @Test
    fun testFetchDataError_shouldHandleErrorFetchingData() {

        val dummyNIC = "911111111v"

        runBlocking {
            val errorMessage = "Error fetching data"
            // Mock error response from the repository
            val inquiriesServerResponse = Response.error<InquiriesServerResponse>(
                500, // Error status code
                ResponseBody.create(MediaType.parse("application/json"), errorMessage)
            )

            // Mock repository behavior
            Mockito.`when`(inquiryRepository.getInquiriesByFarmer(dummyNIC)).thenReturn(
                inquiriesServerResponse
            )

            // Call the method to fetch data
            viewModel.fetchData(dummyNIC)

            // Assert that the inquiryResult LiveData is null or empty
            assertNull(viewModel.inquiryResult.value)

        }
    }

    /**
     * Test case to verify that the [setPhoneNumberToDial] function correctly sets the phone number
     */
    @Test
    fun setPhoneNumberToDial_shouldSetPhoneNumber() {

        val phoneNumber = "0123456789"

        // Call the method to set phone number
        viewModel.setPhoneNumberToDial(phoneNumber)

        // Verify that the phone number is set correctly
        assertEquals(phoneNumber, viewModel.phoneNumberToDial.value)

    }

    /**
     * Unit test for the [HomeViewModel.viewInquiry] function, to verify the successful setting of inquiry data.
     */
    @Test
    fun testViewInquiry_success() {

        // Arrange: Create a mock inquiry object
        val mockInquiry = Inquiry(
            "userNIC1",
            "https://example.com/image1.jpg",
            "Maharagama",
            "2024-02-07",
            "2 months",
            "true",
            "problem one",
            "2024-01-27T07:54:55.730Z",
            "1"
        )

        // Call the function with the mock inquiry
        viewModel.viewInquiry(mockInquiry)

        // Verify that the inquiry data is set correctly
        assertEquals(mockInquiry, viewModel.viewInquiryResult.value)

    }

    /**
     * Unit test for the [HomeViewModel.viewInquiry] function, to verify the behavior when null input is provided.
     */
    @Test
    fun testViewInquiry_nullInput() {
        // Arrange - no input provided

        // Call the function with null input
        viewModel.viewInquiry(null)

        // Verify that the inquiry data is null after calling the function with null input
        assertNull(viewModel.viewInquiryResult.value)
    }

}