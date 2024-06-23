package lnbti.gtp01.droidai.constants

/**
 * Object containing endpoint configurations for API calls.
 * Provides base URLs and endpoint paths for different APIs.
 */
object EndpointConfig {

    /**
     * Base URL for the weather API.
     */
    const val WEATHER_API_BASE_URL = "https://api.open-meteo.com/"

    /**
     * Base URL for the inquiry API.
     */
    const val INQUIRY_API_BASE_URL =
        "https://ob297ofh21.execute-api.us-east-1.amazonaws.com/droidai/"

    /**
     * Endpoint path for submitting an inquiry.
     */
    const val SUBMIT_INQUIRY_ENDPOINT = "submitinquiry"

    /**
     * Endpoint path for retrieving all inquiries.
     */
    const val GET_ALL_INQUIRY_ENDPOINT = "get-inquiries"

    /**
     * Endpoint path for submitting an AI response to an inquiry.
     */
    const val SUBMIT_AI_RESPONSE_ENDPOINT = "responseinquiry"

    /**
     * Endpoint for saving a notification.
     */
    const val SAVE_NOTIFICATION_ENDPOINT = "savenotification"

    /**
     * Endpoint for loading notifications.
     */
    const val LOAD_NOTIFICATION_ENDPOINT = "notifications"

    /**
     * Endpoint path for retrieving inquiries by farmer, with a parameter for the farmer's NIC.
     */
    const val GET_INQUIRIES_BY_FARMER_ENDPOINT =
        "get-inquiries-by-farmer/{createdUserNIC}"

    const val LOGIN_ENDPOINT = "login"
    const val REGISTER_ENDPOINT = "register"


}