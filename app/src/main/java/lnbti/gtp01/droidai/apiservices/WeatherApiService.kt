package lnbti.gtp01.droidai.apiservices

import lnbti.gtp01.droidai.models.WeatherApiResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Service interface for fetching weather forecast data using Retrofit.
 *
 * This interface defines the necessary REST API endpoint(s) for obtaining weather information.
 * It uses Retrofit annotations to describe the HTTP request and parameters.
 */
interface WeatherApiService {

    /**
     * Fetches the weather forecast data from the API.
     *
     * This method is a suspend function compatible with Kotlin Coroutines, allowing for asynchronous
     * network requests. It queries the weather API endpoint and returns a response containing
     * weather data.
     *
     * @param latitude The latitude coordinate for which weather data is requested.
     * @param longitude The longitude coordinate for which weather data is requested.
     * @param current A string specifying the current weather data to be retrieved.
     * @param daily A string specifying the daily weather data to be retrieved.
     * @param timezone The timezone for which weather data is requested.
     * @param forecastDays The number of forecast days for which weather data is to be retrieved.
     * @return A [Response] object containing [WeatherApiResponse] data.
     */
    @GET("v1/forecast")
    suspend fun getWeatherForecast(
        @Query("latitude") latitude: Double?,
        @Query("longitude") longitude: Double?,
        @Query("current") current: String?,
        @Query("daily") daily: String?,
        @Query("timezone") timezone: String?,
        @Query("forecast_days") forecastDays: Int?
    ): Response<WeatherApiResponse>
}
