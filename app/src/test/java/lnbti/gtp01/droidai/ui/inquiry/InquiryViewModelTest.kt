package lnbti.gtp01.droidai.ui.inquiry

import android.net.Uri
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.setMain
import lnbti.gtp01.droidai.models.SubmitInquiryResponse
import lnbti.gtp01.droidai.models.UserInquiry
import lnbti.gtp01.droidai.repository.InquiryRepository
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

@ExperimentalCoroutinesApi
@Suppress("DEPRECATION")
@RunWith(MockitoJUnitRunner::class)
class InquiryViewModelTest {

    // Coroutine dispatcher for testing
    private val testDispatcher = TestCoroutineDispatcher()

    // Rule to make LiveData work synchronously
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    // Mocked repository
    @Mock
    private lateinit var inquiryRepository: InquiryRepository

    // ViewModel to be tested
    private lateinit var viewModel: InquiryViewModel

    @Before
    fun setup() {

        // Initialize Mockito annotations
        MockitoAnnotations.initMocks(this)
        // Set main dispatcher for coroutines
        Dispatchers.setMain(testDispatcher)
        // Initialize ViewModel with mocked repositories
        viewModel = InquiryViewModel(inquiryRepository)


    }

    /**
     * Unit test for the success scenario of submitting an inquiry.
     */
    @Test
    fun submitInquiry_success() {
        // Given a user inquiry and mocked image Uri
        val userInquiry = UserInquiry(
            "911111111v",
            "https://example.com/image1.jpg",
            "Test problem",
            "911111112v",
            "Maharagama",
            "2 months",
            "2024-01-27T07:54:55.730Z",
            "Pending",
            "null"
        )
        val imageUri = Mockito.mock(Uri::class.java)

        runBlocking {
            // Mock repository response
            val response = Response.success(SubmitInquiryResponse(true, "Success"))

            Mockito.`when`(inquiryRepository.submitInquiry(userInquiry, imageUri)).thenReturn(response)

            // When
            viewModel.submitInquiry(userInquiry, imageUri)

            // Then
            assertEquals(true, viewModel.submitInquiry.value)
        }
    }

    /**
     * Unit test for the error scenario of submitting an inquiry.
     */
    @Test
    fun submitInquiry_error() {
        // Given a user inquiry and mocked image Uri
        val userInquiry = UserInquiry(
            "911111111v",
            "https://example.com/image1.jpg",
            "Test problem",
            "911111112v",
            "Maharagama",
            "2 months",
            "2024-01-27T07:54:55.730Z",
            "Pending",
            "null"
        )
        val imageUri = Mockito.mock(Uri::class.java)

        runBlocking {
            // Mock repository to throw an exception
            val exception = RuntimeException("Error submitting inquiry")
            Mockito.`when`(inquiryRepository.submitInquiry(userInquiry, imageUri)).thenThrow(exception)

            // When
            viewModel.submitInquiry(userInquiry, imageUri)

            // Then
            assertEquals(false, viewModel.submitInquiry.value)

        }
    }

}