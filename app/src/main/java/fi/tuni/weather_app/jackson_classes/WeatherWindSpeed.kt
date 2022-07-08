package fi.tuni.weather_app.jackson_classes

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

//Data class for wind speed, also has a toString method for better formatting
@JsonIgnoreProperties(ignoreUnknown = true)
data class WeatherWindSpeed(var speed: Double? = null) {
    override fun toString(): String {
        return "${speed} m/s"
    }
}