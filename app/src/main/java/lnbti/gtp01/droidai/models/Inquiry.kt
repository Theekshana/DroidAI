package lnbti.gtp01.droidai.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Represents an inquiry item.
 *
 * @property createdUserNIC The NIC of the user who created the inquiry.
 * @property imageUrl The URL of the image associated with the inquiry.
 * @property problem The description of the problem in the inquiry.
 * @property createdDate The date when the inquiry was created.
 * @property id The unique identifier of the inquiry.
 */
@Parcelize
data class Inquiry(
    val createdUserNIC: String?,
    val imageUrl: String?,
    val area: String?,
    val status: String?,
    val ageOfTheCrop: String?,
    var response: String?,
    val problem: String?,
    val createdDate: String?,
    val id: String?,
) : Parcelable