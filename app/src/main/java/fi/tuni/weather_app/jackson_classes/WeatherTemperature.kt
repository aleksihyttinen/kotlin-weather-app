package fi.tuni.weather_app.jackson_classes

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
//Data class for temperature, also has a toString method for better formatting
@JsonIgnoreProperties(ignoreUnknown = true)
data class WeatherTemperature(var temp: Double? = null) {
    override fun toString(): String {
        return "$temp°C"
    }
}