package lnbti.gtp01.droidai.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
@Parcelize
data class SubmitInquiryRequest(
   val createdUserNIC: String? = null,
   val problem: String? = null,
   val respondedUserNIC: String? = null,
   val area: String? = null,
   val ageOfTheCrop: String? = null,
   val status: String? = null,
   val response: String? = null,
) : Parcelable