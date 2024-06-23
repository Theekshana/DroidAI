package lnbti.gtp01.droidai.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CategoryModel(
    val title: String?,
    val image: String?,
    val type: String?,
    val items: List<CategoryModel>? = null,
    val link: String? = null
) : Parcelable