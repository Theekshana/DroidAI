package lnbti.gtp01.droidai.models

/**
 * Data class representing a daily forecast.
 * @property date The date of the forecast.
 * @property day The day of the forecast.
 * @property temperature2mMax The maximum temperature forecasted for the day.
 * @property temperature2mMin The minimum temperature forecasted for the day.
 * @property weatherCode The weather code indicating the type of weather.
 * @property dayDescription The description of the day's weather.
 * @property dayImageURL The URL to an image representing the day's weather.
 */
data class DailyForecastModel(
    val date: String?,
    val day: String?,
    val temperature2mMax: Double?,
    val temperature2mMin: Double?,
    val weatherCode: Int?,
    val dayDescription: Int?,
    val dayImageURL: String?
)
