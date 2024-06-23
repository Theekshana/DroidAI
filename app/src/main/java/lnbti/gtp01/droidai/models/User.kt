package lnbti.gtp01.droidai.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    val nic: String,
    val email: String,
    val firstName: String,
    val lastName: String,
    val gender: String,
    val userRole: String,
    val dob: String,
    val contactNo: String,
) : Parcelable{
    fun getName(): String {
        return "$firstName $lastName"
    }
}
