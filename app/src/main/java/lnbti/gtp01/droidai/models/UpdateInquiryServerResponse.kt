package lnbti.gtp01.droidai.models

data class UpdateInquiryServerResponse(
    val success: Boolean,
    val message: String,
    val data: Inquiry
)
