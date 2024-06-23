package lnbti.gtp01.droidai.models

import lnbti.gtp01.droidai.R

/**
 * Represents weather information including description and image.
 *
 * @property description The resource ID of the weather description.
 * @property image The image URL representing the weather condition.
 */
data class WeatherInfo(
    val description: Int?,
    val image: String?
) {
    companion object {
        fun default() = WeatherInfo(R.string.unknown_conditions, null)
    }
}
