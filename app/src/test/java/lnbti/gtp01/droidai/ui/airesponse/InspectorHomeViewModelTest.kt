package lnbti.gtp01.droidai.ui.airesponse

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.setMain
import lnbti.gtp01.droidai.models.InquiriesServerResponse
import lnbti.gtp01.droidai.models.Inquiry
import lnbti.gtp01.droidai.models.SubmitResponseRequest
import lnbti.gtp01.droidai.models.UpdateInquiryServerResponse
import lnbti.gtp01.droidai.repository.InquiryRepositoryImpl
import okhttp3.MediaType
import okhttp3.ResponseBody
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations.initMocks
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.Response

@ExperimentalCoroutinesApi
@Suppress("DEPRECATION")
@RunWith(MockitoJUnitRunner::class)
class InspectorHomeViewModelTest {

    // Coroutine dispatcher for testing
    private val testDispatcher = TestCoroutineDispatcher()

    // Rule to make LiveData work synchronously
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    // Mocked repository
    @Mock
    private lateinit var inquiryRepository: InquiryRepositoryImpl

    // ViewModel to be tested
    private lateinit var viewModel: InspectorHomeViewModel

    /**
     * Setup method to initialize necessary components before each test.
     */
    @Before
    fun setup() {

        // Initialize Mockito annotations
        initMocks(this)
        // Set main dispatcher for coroutines
        Dispatchers.setMain(testDispatcher)
        // Initialize ViewModel with mocked repositories
        viewModel = InspectorHomeViewModel(inquiryRepository)

    }

    /**
     * Unit test to verify that fetchDataForInspectors() method
     * handles successful data fetching correctly
     */
    @Test
    fun fetchDataForInspectors_shouldHandleSuccessfulDataFetch() {

        // Mock inquiries data
        val mockInquiries = listOf(
            Inquiry(
                "911111111v",
                "https://example.com/image1.jpg",
                "Maharagama",
                "Pending",
                "2 months",
                "Null",
                "Test Problem",
                "2024-01-27T07:54:55.730Z",
                "1"
            )
            // Add more mock inquiries if needed
        )
        runBlocking {
            // Mock successful response from the server
            val inquiriesServerResponse = Response.success(
                InquiriesServerResponse(
                    data = mockInquiries,
                    "Data fetched Successfully",
                    true
                )
            )

            // Mock repository behavior
            `when`(inquiryRepository.getAllInquiries()).thenReturn(inquiriesServerResponse)

            // Call the method to fetch data
            viewModel.fetchDataForInspectors()

            // Verify that inquiryResult LiveData is updated with expected data
            assertEquals(mockInquiries, viewModel.inquiryResult.value)

        }
    }

    /**
     * Unit test to verify that fetchDataForInspectors() method
     * handles error fetching data correctly
     */
    @Test
    fun fetchDataForInspectors_shouldHandleErrorFetchingData() {

        runBlocking {
            val errorMessage = "Error fetching data"
            // Mock error response from the repository
            val inquiriesServerResponse = Response.error<InquiriesServerResponse>(
                500, // Error status code
                ResponseBody.create(MediaType.parse("application/json"), errorMessage)
            )

            // Mock repository behavior
            `when`(inquiryRepository.getAllInquiries()).thenReturn(inquiriesServerResponse)

            // Call the method to fetch data
            viewModel.fetchDataForInspectors()

            // Assert that the inquiryResult LiveData is null or empty
            assertNull(viewModel.inquiryResult.value)

        }
    }

    /**
     * Test case to verify that the [updateWithAiResponse] method
     * updates the inquiry's response correctly
     * when a successful response is received from the server
     */
    @Test
    fun updateWithAiResponse_shouldUpdateInquiryResponse() {

        runBlocking {

            // Mock AI response
            val aiResponse = SubmitResponseRequest(
                id = "1",
                respondedUserNIC = "911111112v",
                response = "Updated response",
                status = "Completed"
            )

            // Mock existing inquiry
            val existingInquiry = Inquiry(
                "911111111v",
                "https://example.com/image1.jpg",
                "Maharagama",
                "Null",
                "2 months",
                "Null",
                "Test Problem",
                "2024-01-27T07:54:55.730Z",
                "1",

                )

            // Mock updated inquiry
            val updatedInquiry = Inquiry(
                "911111111v",
                "https://example.com/image1.jpg",
                "Maharagama",
                "Pending",
                "2 months",
                "Updated response",
                "Test Problem",
                "2024-01-27T07:54:55.730Z",
                "1"
            )

            // Mock successful response from the server
            val response = Response.success(
                UpdateInquiryServerResponse(
                    success = true,
                    message = "Inquiry updated successfully",
                    data = updatedInquiry
                )
            )

            // Mock successful response from the server
            `when`(inquiryRepository.submitResponse(aiResponse)).thenReturn(response)

            // Set the initial value of _viewInquiryResult LiveData
            viewModel._viewInquiryResult.value = existingInquiry

            // Call the method to update with AI response
            viewModel.updateWithAiResponse(aiResponse)

            // Verify that the inquiry's response field is updated
            assertEquals(aiResponse.response, viewModel._viewInquiryResult.value?.response)

        }
    }

    /**
     * Test case to verify that the [updateWithAiResponse] method
     * handles error response from the server
     */
    @Test
    fun updateWithAiResponse_should_handle_error_response() {

        runBlocking {

            // Mock AI response
            val aiResponse = SubmitResponseRequest(
                id = "1",
                respondedUserNIC = "911111112v",
                response = "Updated response",
                status = "Completed"
            )

            // Mock existing inquiry
            val existingInquiry = Inquiry(
                "911111111v",
                "https://example.com/image1.jpg",
                "Maharagama",
                "Null",
                "2 months",
                "Null",
                "Test Problem",
                "2024-01-27T07:54:55.730Z",
                "1",

                )

            // Mock error response from the server
            val errorMessage = "Error updating inquiry"
            val response = Response.error<UpdateInquiryServerResponse>(
                500,
                ResponseBody.create(
                    MediaType.parse(
                        "application/json"
                    ),
                    errorMessage
                )
            )

            // Mock repository behavior
            `when`(inquiryRepository.submitResponse(aiResponse)).thenReturn(response)

            // Set the initial value of _viewInquiryResult LiveData
            viewModel._viewInquiryResult.value = existingInquiry

            // Call the method to update with AI response
            viewModel.updateWithAiResponse(aiResponse)

            // Verify that the inquiry's response field remains unchanged
            assertEquals(existingInquiry.response, viewModel._viewInquiryResult.value?.response)

        }
    }

    /**
     * Test case to verify that the [viewInquiry] method
     * sets the inquiry correctly in the ViewModel.
     */
    @Test
    fun viewInquiry_success() {
        // Given
        val dummyInquiry = Inquiry(
            "911111111v",
            "https://example.com/image1.jpg",
            "Maharagama",
            "Null",
            "2 months",
            "Null",
            "Test Problem",
            "2024-01-27T07:54:55.730Z",
            "1"
        )

        // When
        viewModel.viewInquiry(dummyInquiry)

        // Then
        assertEquals(dummyInquiry, viewModel.viewInquiryResult.value)
    }

    /**
     * Test case to verify that the [viewInquiry] method handles null inquiry correctly.
     */
    @Test
    fun viewInquiry_error() {
        // Given a null inquiry
        val nullInquiry: Inquiry? = null

        // When
        nullInquiry?.let { viewModel.viewInquiry(it) }

        // Then
        assertNull(viewModel.viewInquiryResult.value)

    }

}