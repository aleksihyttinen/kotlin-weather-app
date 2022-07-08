package fi.tuni.weather_app.jackson_classes

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.text.SimpleDateFormat
import java.util.*
//Data class for creating forecastItems
//(basically same as WeatherItem but with different variable names because the API request is different)
//Also has get methods for getting formatted weather variables
@JsonIgnoreProperties(ignoreUnknown = true)
data class ForecastItem(var dt: Long= 0, var temp: ForecastTemperature? = null, var wind_speed: Double = 0.0, var weather: MutableList<WeatherInfo>? = null){
    val dtFormat = SimpleDateFormat("EEE dd/MM/yyyy")
    val date : String
        get() = dtFormat.format(Date(dt * 1000)).replaceFirstChar { it.uppercase() }
    val desc : String?
        get() = weather?.get(0)?.description?.replaceFirstChar { it.uppercase() }
    val icon : String
        get() = weather?.get(0)?.icon.toString()
    val windSpeed : String
        get() = "$wind_speed m/s"
}