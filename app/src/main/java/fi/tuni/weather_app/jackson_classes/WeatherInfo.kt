package fi.tuni.weather_app.jackson_classes

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
//Data class for weather description and icon, also has a toString method for better formatting
@JsonIgnoreProperties(ignoreUnknown = true)
data class WeatherInfo(var description: String = "", var icon: String = "") {
    override fun toString(): String {
        return "$description $icon"
    }
}