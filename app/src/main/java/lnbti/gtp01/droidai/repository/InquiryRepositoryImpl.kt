package lnbti.gtp01.droidai.repository

import android.net.Uri
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import lnbti.gtp01.droidai.apiservices.InquiryApiService
import lnbti.gtp01.droidai.models.GetAllNotificationResponse
import lnbti.gtp01.droidai.models.InquiriesServerResponse
import lnbti.gtp01.droidai.models.NotificationData
import lnbti.gtp01.droidai.models.SaveNotificationResponse
import lnbti.gtp01.droidai.models.SubmitInquiryResponse
import lnbti.gtp01.droidai.models.SubmitResponseRequest
import lnbti.gtp01.droidai.models.UpdateInquiryServerResponse
import lnbti.gtp01.droidai.models.UserInquiry
import retrofit2.Response
import java.util.UUID
import javax.inject.Inject

/**
 * Implementation of [InquiryRepository] interface.
 * Handles operations related to inquiries data by interacting with the API service.
 * @param inquiryApi The API service for inquiries.
 */
class InquiryRepositoryImpl @Inject constructor(
    private val inquiryApi: InquiryApiService,
    private val firebaseStorage: FirebaseStorage,
) : InquiryRepository {
    /**
     * Submits an inquiry.
     * @param userInquiry The user object containing the inquiry details.
     * @return A [Response] object containing the server's response to the submission.
     */
    override suspend fun submitInquiry(
        userInquiry: UserInquiry,
        imageUri: Uri,
    ): Response<SubmitInquiryResponse> {
        return withContext(Dispatchers.IO) {
            try {

                // Upload the image to Firebase Storage
                val imageUrl = uploadImageToFirebaseStorage(imageUri)

                // Update the userInquiry object with the image URL
                val updatedUserInquiry = userInquiry.copy(
                    imageUrl = imageUrl
                )

                val response = inquiryApi.submitInquiry(updatedUserInquiry)
                response

            } catch (e: Exception) {
                throw e
            }
        }
    }

    /**
     * Uploads an image to Firebase Storage and returns its download URL.
     *
     * @param imageUri The URI of the image to upload.
     * @return The download URL of the uploaded image, or null if an error occurs.
     */
    private suspend fun uploadImageToFirebaseStorage(imageUri: Uri): String? {
        return try {
            val imageUrl = firebaseStorage.reference
                .child("images/${UUID.randomUUID()}")
                .apply { putFile(imageUri).await() }
                .downloadUrl.await()
                .toString()
            imageUrl
        } catch (e: Exception) {
            null
        }
    }

    /**
     * Retrieves inquiries by farmer.
     * @param createdUserNIC The NIC of the farmer for whom inquiries are to be retrieved.
     * @return A [Response] object containing the server's response with the inquiries.
     */
    override suspend fun getInquiriesByFarmer(
        createdUserNIC: String,
    ): Response<InquiriesServerResponse> {
        return withContext(Dispatchers.IO) {
            try {


                val response = inquiryApi.getInquiriesByFarmer(createdUserNIC)
                response

            } catch (e: Exception) {
                throw e  // Re-throw the exception after logging
            }
        }
    }

    /**
     * Retrieves all inquiries.
     * @return A [Response] object containing the server's response with all inquiries.
     */
    override suspend fun getAllInquiries(): Response<InquiriesServerResponse> {
        return withContext(Dispatchers.IO) {
            try {
                // Call the API to get all inquiries
                val response = inquiryApi.getAllInquiries()
                response

            } catch (e: Exception) {
                throw e
            }
        }
    }

    /**
     * Submits a response to the server.
     *
     * @param responseInquiry The [SubmitResponseRequest] object containing the response details.
     * @return A [Response] object containing the server's response to the submitted response.
     * @throws Exception if an error occurs during the API call.
     */
    override suspend fun submitResponse(
        responseInquiry: SubmitResponseRequest,
    ): Response<UpdateInquiryServerResponse> {
        return withContext(Dispatchers.IO) {
            try {
                // Make the API call to submit the response
                val response = inquiryApi.submitResponse(responseInquiry)
                response

            } catch (e: Exception) {
                throw e  // Re-throw the exception after logging
            }

        }
    }

    /**
     * Sends a request to the server to save a notification.
     *
     * @param saveNotification The notification data to be saved.
     * @return A response containing the result of the save operation.
     */
    override suspend fun saveNotification(
        saveNotification: NotificationData,
    ): Response<SaveNotificationResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val response = inquiryApi.saveNotification(saveNotification)
                response

            } catch (e: Exception) {
                throw e
            }
        }
    }

    /**
     * Sends a request to the server to fetch all notifications.
     *
     * @return A response containing the list of all notifications fetched from the server.
     */
    override suspend fun getAllNotification(): Response<GetAllNotificationResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val response = inquiryApi.getAllNotification()
                response

            } catch (e: Exception) {
                throw e
            }
        }
    }

}

