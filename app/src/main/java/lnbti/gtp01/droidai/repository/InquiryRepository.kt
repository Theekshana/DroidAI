package lnbti.gtp01.droidai.repository

import android.net.Uri
import lnbti.gtp01.droidai.models.GetAllNotificationResponse
import lnbti.gtp01.droidai.models.InquiriesServerResponse
import lnbti.gtp01.droidai.models.NotificationData
import lnbti.gtp01.droidai.models.SaveNotificationResponse
import lnbti.gtp01.droidai.models.SubmitInquiryResponse
import lnbti.gtp01.droidai.models.SubmitResponseRequest
import lnbti.gtp01.droidai.models.UpdateInquiryServerResponse
import lnbti.gtp01.droidai.models.UserInquiry
import retrofit2.Response

/**
 * Interface defining operations for handling inquiries data.
 * This interface provides methods for submitting inquiries, retrieving inquiries by farmer,
 * and retrieving all inquiries.
 */
interface InquiryRepository {

    /**
     * Submits an inquiry.
     * @param userInquiry The user object containing the inquiry details.
     * @return A [Response] object containing the server's response to the submission.
     */
    suspend fun submitInquiry(
        userInquiry: UserInquiry,
        imageUri: Uri,
    ): Response<SubmitInquiryResponse>

    /**
     * Retrieves inquiries by farmer.
     * @param createdUserNIC The NIC of the farmer for whom inquiries are to be retrieved.
     * @return A [Response] object containing the server's response with the inquiries.
     */
    suspend fun getInquiriesByFarmer(
        createdUserNIC: String,
    ): Response<InquiriesServerResponse>

    /**
     * Retrieves all inquiries.
     * @return A [Response] object containing the server's response with all inquiries.
     */
    suspend fun getAllInquiries(): Response<InquiriesServerResponse>

    /**
     * Submits a response to an inquiry.
     * @param responseInquiry The response object containing the inquiry ID and response details.
     * @return A [Response] object containing the server's response to the submission.
     */
    suspend fun submitResponse(
        responseInquiry: SubmitResponseRequest,
    ): Response<UpdateInquiryServerResponse>

    /**
     * Sends a request to the server to save a notification.
     *
     * @param saveNotification The notification data to be saved.
     * @return A response containing the result of the save operation.
     */
    suspend fun saveNotification(
        saveNotification: NotificationData,
    ): Response<SaveNotificationResponse>

    /**
     * Sends a request to the server to fetch all notifications.
     *
     * @return A response containing the list of all notifications fetched from the server.
     */
    suspend fun getAllNotification(): Response<GetAllNotificationResponse>

}