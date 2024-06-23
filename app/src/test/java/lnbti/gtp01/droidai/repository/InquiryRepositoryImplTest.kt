package lnbti.gtp01.droidai.repository

import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.runBlocking
import lnbti.gtp01.droidai.apiservices.InquiryApiService
import lnbti.gtp01.droidai.models.InquiriesServerResponse
import lnbti.gtp01.droidai.models.Inquiry
import lnbti.gtp01.droidai.models.SubmitResponseRequest
import lnbti.gtp01.droidai.models.UpdateInquiryServerResponse
import okhttp3.ResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Assert.fail
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.doAnswer
import org.mockito.Mockito.doReturn
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.Response


/**
 * Unit tests for [InquiryRepositoryImpl].
 */
    @RunWith(MockitoJUnitRunner::class)
class InquiryRepositoryImplTest {

    // Mocked InquiryApiService
    @Mock
    private lateinit var mockInquiryApiService: InquiryApiService

    @Mock
    private lateinit var mockFirebaseStorage: FirebaseStorage

    // Class under test
    private lateinit var inquiryRepositoryImpl: InquiryRepositoryImpl

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        inquiryRepositoryImpl = InquiryRepositoryImpl(
            mockInquiryApiService, mockFirebaseStorage
        )
    }

    /**
     * Test for retrieving inquiries by farmer - successful case.
     */
    @Test
    fun testGetInquiriesByFarmer_success() = runBlocking {
        val dummyNIC = "911111111v"
        val dummyResponse = Response.success(
            InquiriesServerResponse(
                data = listOf(
                    Inquiry(
                        "911111111v",
                        "null",
                        "Colombo",
                        "Pending",
                        "six months",
                        "null",
                        "Test problem",
                        "2024-01-27T07:54:55.730Z",
                        "1",

                        ),
                    Inquiry(
                        "911111111v",
                        "null",
                        "Maharagama",
                        "Pending",
                        "One month",
                        "null",
                        "Test problem",
                        "2024-01-27T07:54:55.730Z",
                        "2"
                    )

                ),
                message = "Success",
                success = true
            )
        )

        `when`(mockInquiryApiService.getInquiriesByFarmer(dummyNIC)).thenReturn(dummyResponse)

        // When
        val result = inquiryRepositoryImpl.getInquiriesByFarmer(dummyNIC)

        // Then
        assertEquals(dummyResponse.code(), result.code())
        assertEquals(dummyResponse.body(), result.body())

    }

    /**
     * Test for retrieving inquiries by farmer - error case.
     */
    @Test
    fun testGetInquiriesByFarmer_error() = runBlocking {
        // Given
        val dummyNIC = "911111111v"
        `when`(mockInquiryApiService.getInquiriesByFarmer(dummyNIC)).thenReturn(
            Response.error(
                404,
                ResponseBody.create(null, "")
            )
        )

        // When
        val result = inquiryRepositoryImpl.getInquiriesByFarmer(dummyNIC)

        // Then
        assertEquals(404, result.code())
        assertNull(result.body())
    }

    /**
     * Test for getting all inquiries - successful case.
     */
    @Test
    fun getAllInquiries_success() {
        // Given
        runBlocking {
            val dummyInquiries = listOf(
                Inquiry(
                    "911111111v",
                    "https://example.com/image1.jpg",
                    "Maharagama",
                    "Null",
                    "2 months",
                    "Null",
                    "Test Problem",
                    "2024-01-27T07:54:55.730Z",
                    "1"
                ),

                Inquiry(
                    "911111113v",
                    "https://example.com/image1.jpg",
                    "Maharagama",
                    "Null",
                    "2 months",
                    "Null",
                    "Test Problem",
                    "2024-01-27T07:54:55.730Z",
                    "2"
                )
            )

            val dummyResponse = Response.success(
                InquiriesServerResponse(
                    dummyInquiries,
                    "Success",
                    true
                )
            )
            `when`(mockInquiryApiService.getAllInquiries()).thenReturn(dummyResponse)

            // When
            val result = inquiryRepositoryImpl.getAllInquiries()

            // Then
            assertEquals(dummyResponse.code(), result.code())
            assertEquals(dummyResponse.body(), result.body())
        }
    }

    /**
     * Test for getting all inquiries - error case.
     */
    @Test(expected = Exception::class)
    fun getAllInquiries_error() {

        runBlocking {

            `when`(mockInquiryApiService.getAllInquiries())
                .thenThrow(Exception("Error occurred"))

            try {
                inquiryRepositoryImpl.getAllInquiries()
                fail("Expected an exception to be thrown")
            } catch (e: Exception) {
                // Assertion to verify that an exception was thrown
                assertNotNull(e.message)
                assertEquals(
                    "Error occurred", e.message
                )

            }
        }
    }

    /**
     * Unit test for the success scenario of the [submitResponse] function.
     */
    @Test
    fun submitResponse_success() {
        runBlocking {
            // Given a dummy inquiry, request, and response
            val dummyInquiry = Inquiry(
                "911111112v",
                "https://example.com/image.jpg",
                "Colombo",
                "Pending",
                "One month",
                "null",
                "Problem",
                "2024-01-27T07:54:55.730Z",
                "1"
            )

            val dummyRequest = SubmitResponseRequest(
                "1",
                "911111112v",
                "Updated response",
                "Completed"
            )

            val dummyResponse = Response.success(
                UpdateInquiryServerResponse(
                    true,
                    "Success",
                    dummyInquiry
                )

            )

            // Stub the method call to return the dummy response
            doReturn(dummyResponse).`when`(mockInquiryApiService).submitResponse(dummyRequest)

            // When
            val result = inquiryRepositoryImpl.submitResponse(dummyRequest)

            // Then
            assertEquals(dummyResponse.code(), result.code())
            assertEquals(dummyResponse.body(), result.body())
        }

    }

    /**
     * Unit test for the error scenario of the [submitResponse] function.
     */
    @Test
    fun submitResponse_error() {
        runBlocking {
            val dummyRequest = SubmitResponseRequest(
                "1",
                "911111112v",
                "Updated response",
                "Completed"
            )

            doAnswer {
                throw Exception("Error occurred")
            }.`when`(mockInquiryApiService).submitResponse(dummyRequest)

            // Call the method under test within a try-catch block
            val result = try {
                inquiryRepositoryImpl.submitResponse(dummyRequest)
            } catch (e: Exception) {
                // Assert that the caught exception matches the expected type and message
                assertEquals("Error occurred", e.message)
                assertTrue(e is Exception)
                null
            }

            assertNull(result)
        }
    }

}
