package lnbti.gtp01.droidai.models

/**
 * Data class representing a request to submit a response to an inquiry.
 *
 * @property id The ID of the inquiry.
 * @property respondedUserNIC The NIC of the user responding to the inquiry.
 * @property response The response to the inquiry.
 * @property status The status of the response (e.g., "Completed").
 */
data class SubmitResponseRequest(
    val id: String?,
    val respondedUserNIC: String?,
    val response: String?,
    val status: String?
)
