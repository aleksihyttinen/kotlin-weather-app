package fi.tuni.weather_app.jackson_classes

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.text.SimpleDateFormat
import java.util.*
//Data class for creating weatherItems. Also has get methods for getting formatted weather variables
@JsonIgnoreProperties(ignoreUnknown = true)
data class WeatherItem(var name: String = "", var dt: Long= 0, var main: WeatherTemperature? = null, var weather: MutableList<WeatherInfo>? = null, var wind: WeatherWindSpeed? = null){
    val dtFormat = SimpleDateFormat("EE dd/MM/yyyy")
    override fun toString(): String {
        return "$name\n${date}\n${main}\n${desc}\n$wind"
    }
    val date : String
        get() = dtFormat.format(Date(dt * 1000)).replaceFirstChar { it.uppercase() }
    val desc : String?
        get() = weather?.get(0)?.description?.replaceFirstChar { it.uppercase() }
    val temp : String
        get() = main.toString()
    val windSpeed : String
        get() = wind.toString()
    val icon : String
        get() = weather?.get(0)?.icon.toString()
}