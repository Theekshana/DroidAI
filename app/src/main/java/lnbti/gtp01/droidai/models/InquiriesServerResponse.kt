package lnbti.gtp01.droidai.models

/**
 * Represents a server response containing a list of inquiries.
 *
 * @property data The list of inquiries retrieved from the server.
 * @property message An optional message from the server, which may provide additional context or information about the response.
 * @property success A boolean indicating whether the server request was successful or not.
 */
data class InquiriesServerResponse(
    val data: List<Inquiry>,
    val message: String?,
    val success: Boolean,
)
