package lnbti.gtp01.droidai.apiservices

import lnbti.gtp01.droidai.constants.EndpointConfig
import lnbti.gtp01.droidai.models.GetAllNotificationResponse
import lnbti.gtp01.droidai.models.InquiriesServerResponse
import lnbti.gtp01.droidai.models.NotificationData
import lnbti.gtp01.droidai.models.SaveNotificationResponse
import lnbti.gtp01.droidai.models.SubmitInquiryResponse
import lnbti.gtp01.droidai.models.SubmitResponseRequest
import lnbti.gtp01.droidai.models.UpdateInquiryServerResponse
import lnbti.gtp01.droidai.models.UserInquiry
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

/**
 * Interface defining the API service for inquiries.
 */
interface InquiryApiService {

    /**
     * Submits an inquiry.
     * @param userInquiry The user object containing the inquiry details.
     * @return A [Response] object containing the server's response to the submission.
     */
    @POST(EndpointConfig.SUBMIT_INQUIRY_ENDPOINT)
    suspend fun submitInquiry(@Body userInquiry: UserInquiry): Response<SubmitInquiryResponse>

    /**
     * Retrieves inquiries by farmer.
     * @param createdUserNIC The NIC of the farmer for whom inquiries are to be retrieved.
     * @return A [Response] object containing the server's response with the inquiries.
     */
    @GET(EndpointConfig.GET_INQUIRIES_BY_FARMER_ENDPOINT)
    suspend fun getInquiriesByFarmer(
        @Path("createdUserNIC")
        createdUserNIC: String,
    ): Response<InquiriesServerResponse>

    /**
     * Retrieves all inquiries.
     * @return A [Response] object containing the server's response with all inquiries.
     */
    @GET(EndpointConfig.GET_ALL_INQUIRY_ENDPOINT)
    suspend fun getAllInquiries(): Response<InquiriesServerResponse>

    /**
     * Retrofit interface for submitting AI response to the server.
     */
    @POST(EndpointConfig.SUBMIT_AI_RESPONSE_ENDPOINT)
    suspend fun submitResponse(
        @Body responseInquiry: SubmitResponseRequest,
    ): Response<UpdateInquiryServerResponse>

    /**
     * Retrofit interface for saving a notification to the server.
     */
    @POST(EndpointConfig.SAVE_NOTIFICATION_ENDPOINT)
    suspend fun saveNotification(
        @Body saveNotification: NotificationData,
    ): Response<SaveNotificationResponse>

    /**
     * Retrofit interface for fetching all notifications from the server.
     */
    @GET(EndpointConfig.LOAD_NOTIFICATION_ENDPOINT)
    suspend fun getAllNotification(): Response<GetAllNotificationResponse>

}