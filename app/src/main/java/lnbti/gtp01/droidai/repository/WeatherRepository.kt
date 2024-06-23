package lnbti.gtp01.droidai.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import lnbti.gtp01.droidai.apiservices.WeatherApiService
import lnbti.gtp01.droidai.models.WeatherApiResponse
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repository class responsible for fetching weather data from the weather API.
 *
 * This class communicates with the WeatherApiService to fetch weather data based on the provided latitude and longitude coordinates.
 * It provides a LiveData object containing the fetched weather data.
 *
 * @property weatherApiService The WeatherApiService used to fetch weather data.
 */
@Singleton
class WeatherRepository @Inject constructor(
    private val weatherApiService: WeatherApiService
) {
    /**
     * Fetches weather data from the weather API.
     *
     * This method makes a network request to the weather API using the provided latitude and longitude coordinates.
     * It retrieves current and daily weather forecast data for the specified location.
     *
     * @param latitude The latitude coordinate of the location for which weather data is requested.
     * @param longitude The longitude coordinate of the location for which weather data is requested.
     * @return A MutableLiveData object containing the fetched weather data.
     */
    fun fetchWeatherData(latitude: Double, longitude: Double): MutableLiveData<WeatherApiResponse> {

        val result = MutableLiveData<WeatherApiResponse>()

        Log.d("+++ weather api resp", "fetchWeatherData")

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = weatherApiService.getWeatherForecast(
                    latitude = latitude,
                    longitude = longitude,
                    current = "temperature_2m,weather_code",
                    daily = "weather_code,temperature_2m_max,temperature_2m_min",
                    timezone = "auto",
                    forecastDays = 7
                )

                if (response.isSuccessful && response.body() != null) {
                    Log.d("+++ weather api resp", response.toString())
                    Log.d("+++ weather api resp", response.body().toString())

                    // Directly post the response body if it's not null
                    result.postValue(response.body())
                } else {
                    Log.e("WeatherRepository", "Error:${response.errorBody()?.string()}")
                    // Optionally, handle the error case, maybe post a specific error state
                }
            } catch (e: Exception) {
                Log.e("WeatherRepository", "Exception while fetching weather data", e)
                // Optionally, handle the exception, maybe post a specific error state
            }
        }
        return result
    }
}