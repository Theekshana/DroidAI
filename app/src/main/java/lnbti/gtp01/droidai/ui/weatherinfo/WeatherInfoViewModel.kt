package lnbti.gtp01.droidai.ui.weatherinfo

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import lnbti.gtp01.droidai.R
import lnbti.gtp01.droidai.models.DailyForecastModel
import lnbti.gtp01.droidai.models.WeatherApiResponse
import lnbti.gtp01.droidai.models.WeatherInfo
import lnbti.gtp01.droidai.repository.WeatherRepository
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale
import javax.inject.Inject


@HiltViewModel
class WeatherInfoViewModel @Inject constructor(
    private val weatherRepository: WeatherRepository
) : ViewModel() {

    private val _weatherResponse = MutableLiveData<WeatherApiResponse>()

    private val _dailyForecasts = MutableLiveData<List<DailyForecastModel>>()
    val dailyForecasts: LiveData<List<DailyForecastModel>> = _dailyForecasts

    @RequiresApi(Build.VERSION_CODES.O)
    fun fetchWeatherData(latitude: Double, longitude: Double) {
        viewModelScope.launch {
            weatherRepository.fetchWeatherData(latitude, longitude)
                .observeForever { weatherApiResponse ->
                    _weatherResponse.postValue(weatherApiResponse)
                    Log.d("+++ _weatherResponse", weatherApiResponse.toString())

                    // Directly pass the WeatherApiResponse object
                    val forecasts = transformWeatherResponseToDailyForecasts(weatherApiResponse)
                    Log.d("+++ forecasts", forecasts.toString())
                    _dailyForecasts.postValue(forecasts)
                }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun transformWeatherResponseToDailyForecasts(response: WeatherApiResponse):
            List<DailyForecastModel> {

        val weatherCodeToInfoMap = getWeatherCodeToInfoMap()

        return response.daily?.time?.indices?.map { index ->
            val date = response.daily.time[index]
            val weatherCode = response.daily.weatherCode?.get(index) ?: -1 //safe fallback
            val maxTemp = response.daily.temperature2mMax?.get(index) ?: 0.0
            val minTemp = response.daily.temperature2mMin?.get(index) ?: 0.0

            val dayInfo = weatherCodeToInfoMap[weatherCode] ?: WeatherInfo.default()

            DailyForecastModel(
                weatherCode = weatherCode,
                date = date,
                day = dateToDay(date),
                temperature2mMax = maxTemp,
                temperature2mMin = minTemp,
                dayDescription = dayInfo.description,
                dayImageURL = dayInfo.image
            )
        } ?: listOf() // Return an empty list if any of the safe calls before map result in null

    }

    private fun getWeatherCodeToInfoMap(): Map<Int, WeatherInfo> {

        return mapOf(
            0 to WeatherInfo(R.string.Sunny, "http://openweathermap.org/img/wn/01d@2x.png"),
            1 to WeatherInfo(R.string.MainlySunny, "http://openweathermap.org/img/wn/01d@2x.png"),
            2 to WeatherInfo(R.string.PartlyCloudy, "http://openweathermap.org/img/wn/02d@2x.png"),
            3 to WeatherInfo(R.string.Cloudy, "http://openweathermap.org/img/wn/03d@2x.png"),
            45 to WeatherInfo(R.string.Foggy, "http://openweathermap.org/img/wn/50d@2x.png"),
            48 to WeatherInfo(R.string.RimeFog, "http://openweathermap.org/img/wn/50d@2x.png"),
            51 to WeatherInfo(R.string.LightDrizzle, "http://openweathermap.org/img/wn/09d@2x.png"),
            53 to WeatherInfo(R.string.Drizzle, "http://openweathermap.org/img/wn/09d@2x.png"),
            55 to WeatherInfo(R.string.HeavyDrizzle, "http://openweathermap.org/img/wn/09d@2x.png"),
            56 to WeatherInfo(
                R.string.LightFreezingDrizzle,
                "http://openweathermap.org/img/wn/09d@2x.png"
            ),
            57 to WeatherInfo(
                R.string.FreezingDrizzle,
                "http://openweathermap.org/img/wn/09d@2x.png"
            ),
            61 to WeatherInfo(R.string.LightRain, "http://openweathermap.org/img/wn/10d@2x.png"),
            63 to WeatherInfo(R.string.Rain, "http://openweathermap.org/img/wn/10d@2x.png"),
            65 to WeatherInfo(R.string.HeavyRain, "http://openweathermap.org/img/wn/10d@2x.png"),
            66 to WeatherInfo(
                R.string.LightFreezingRain,
                "http://openweathermap.org/img/wn/10d@2x.png"
            ),
            67 to WeatherInfo(R.string.FreezingRain, "http://openweathermap.org/img/wn/10d@2x.png"),
            71 to WeatherInfo(R.string.LightSnow, "http://openweathermap.org/img/wn/13d@2x.png"),
            73 to WeatherInfo(R.string.Snow, "http://openweathermap.org/img/wn/13d@2x.png"),
            75 to WeatherInfo(R.string.HeavySnow, "http://openweathermap.org/img/wn/13d@2x.png"),
            77 to WeatherInfo(R.string.SnowGrains, "http://openweathermap.org/img/wn/13d@2x.png"),
            80 to WeatherInfo(R.string.LightShowers, "http://openweathermap.org/img/wn/09d@2x.png"),
            81 to WeatherInfo(R.string.Showers, "http://openweathermap.org/img/wn/09d@2x.png"),
            82 to WeatherInfo(R.string.HeavyShowers, "http://openweathermap.org/img/wn/09d@2x.png"),
            85 to WeatherInfo(
                R.string.LightSnowShowers,
                "http://openweathermap.org/img/wn/13d@2x.png"
            ),
            86 to WeatherInfo(R.string.SnowShowers, "http://openweathermap.org/img/wn/13d@2x.png"),
            95 to WeatherInfo(R.string.Thunderstorm, "http://openweathermap.org/img/wn/11d@2x.png"),
            96 to WeatherInfo(
                R.string.LightThunderstormsWithHail,
                "http://openweathermap.org/img/wn/11d@2x.png"
            ),
            99 to WeatherInfo(
                R.string.ThunderstormWithHail,
                "http://openweathermap.org/img/wn/11d@2x.png"
            )
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun dateToDay(date: String): String {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val localDate = LocalDate.parse(date, formatter)
        return localDate.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.getDefault())
    }
}