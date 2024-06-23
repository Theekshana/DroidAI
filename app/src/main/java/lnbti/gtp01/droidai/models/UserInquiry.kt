package lnbti.gtp01.droidai.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Data class representing user information related to crop inquiries.
 *
 * @property createdUserNIC The NIC of the user who created the inquiry. Defaults to "911111111v".
 * @property problem The description of the crop problem. Can be null if not provided.
 * @property respondedUserNIC The NIC of the user who responded to the inquiry. Can be null if not responded yet.
 * @property area The area associated with the crop inquiry. Can be null if not provided.
 * @property ageOfTheCrop The age of the crop in months. Can be null if not provided.
 * @property status The status of the crop inquiry. Can be null if not provided.
 * @property response The response to the crop inquiry. Can be null if not responded yet.
 */
@Parcelize
data class UserInquiry(

    val createdUserNIC: String? = null,
    val imageUrl: String? = null,
    val problem: String? = null,
    val respondedUserNIC: String? = null,
    val area: String? = null,
    val ageOfTheCrop: String? = null,
    val createdDate: String? = null,
    val status: String? = null,
    val response: String? = null,

    ) : Parcelable
