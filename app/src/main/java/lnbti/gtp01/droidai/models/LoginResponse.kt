package lnbti.gtp01.droidai.models

data class LoginResponse(
    val success: Boolean = false,
    var message: String? = null,
    var data: User? = null
)