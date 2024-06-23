package lnbti.gtp01.droidai.models

import com.google.gson.annotations.SerializedName

/**
 * Represents the response from the weather API containing both current and daily weather information.
 *
 * @property latitude The latitude of the location.
 * @property longitude The longitude of the location.
 * @property generationTimeMs The time taken by the server to generate the data.
 * @property utcOffsetSeconds The UTC offset in seconds.
 * @property timezone The timezone of the location.
 * @property timezoneAbbreviation The abbreviation of the timezone.
 * @property elevation The elevation of the location in meters.
 * @property currentUnits Units for the current weather data.
 * @property current Current weather data.
 * @property dailyUnits Units for the daily weather data.
 * @property daily Daily weather data.
 */
data class WeatherApiResponse(
    val latitude: Double?,
    val longitude: Double?,
    @SerializedName("generationtime_ms") val generationTimeMs: Double?,
    @SerializedName("utc_offset_seconds") val utcOffsetSeconds: Int?,
    val timezone: String?,
    @SerializedName("timezone_abbreviation") val timezoneAbbreviation: String?,
    val elevation: Double?,
    @SerializedName("current_units") val currentUnits: CurrentUnits?,
    val current: Current?,
    @SerializedName("daily_units") val dailyUnits: DailyUnits?,
    val daily: Daily?
)

/**
 * Represents the units of measurement for the current weather data.
 *
 * @property time The format of time used in the data.
 * @property interval The interval at which the data is measured.
 * @property temperature2m The unit of measurement for temperature at 2 meters above ground.
 * @property weatherCode The code representing the weather condition.
 */
data class CurrentUnits(
    val time: String?,
    val interval: String?,
    @SerializedName("temperature_2m") val temperature2m: String?,
    val rain: String?,
    @SerializedName("weather_code") val weatherCode: String?
)

/**
 * Represents the current weather conditions.
 *
 * @property time The timestamp of the current weather data.
 * @property interval The data interval in seconds.
 * @property temperature2m The current temperature in degrees Celsius at 2 meters above ground.
 * @property rain The amount of rain (optional, as it may not be present in all responses).
 * @property weatherCode The code representing the current weather condition.
 */
data class Current(
    val time: String?,
    val interval: Int?,
    @SerializedName("temperature_2m") val temperature2m: Double?,
    val rain: Double?,
    @SerializedName("weather_code") val weatherCode: Int?
)

/**
 * Represents the units of measurement for the daily weather data.
 *
 * @property time The format of time used in the data.
 * @property weatherCode The code representing the weather condition.
 * @property temperature2mMax The unit of measurement for maximum daily temperature at 2 meters above ground.
 * @property temperature2mMin The unit of measurement for minimum daily temperature at 2 meters above ground.
 */
data class DailyUnits(
    val time: String?,
    @SerializedName("weather_code") val weatherCode: String?,
    @SerializedName("temperature_2m_max") val temperature2mMax: String?,
    @SerializedName("temperature_2m_min") val temperature2mMin: String?
)

/**
 * Represents the daily weather forecast data.
 *
 * @property time The list of dates for the daily forecast.
 * @property weatherCode The list of weather condition codes for each day.
 * @property temperature2mMax The list of maximum temperatures for each day at 2 meters above ground.
 * @property temperature2mMin The list of minimum temperatures for each day at 2 meters above ground.
 */
data class Daily(
    val time: List<String>?,
    @SerializedName("weather_code") val weatherCode: List<Int>?,
    @SerializedName("temperature_2m_max") val temperature2mMax: List<Double>?,
    @SerializedName("temperature_2m_min") val temperature2mMin: List<Double>?
)