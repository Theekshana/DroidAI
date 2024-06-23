package lnbti.gtp01.droidai.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Data class representing an agricultural inspector.
 *
 * This data class stores information about an agricultural inspector, including their unique identifier,
 * first name, last name, district of assignment, contact number, and email address.
 *
 * @property id The unique identifier of the agricultural inspector.
 * @property firstName The first name of the agricultural inspector.
 * @property lastName The last name of the agricultural inspector.
 * @property district The district of assignment for the agricultural inspector.
 * @property contactNo The contact number of the agricultural inspector.
 * @property email The email address of the agricultural inspector.
 */
@Parcelize
data class AgricInspector(
    val id: String,
    val firstName: String,
    val lastName: String,
    val district: String,
    val contactNo: String,
    val email: String,
) : Parcelable {
    fun getName(): String {
        return "$firstName $lastName"
    }
}