package fi.tuni.weather_app.jackson_classes

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
//Data class for forecast temperature (day + night), also has a toString method for better formatting
@JsonIgnoreProperties(ignoreUnknown = true)
data class ForecastTemperature(var day: Double = 0.0, var night: Double = 0.0) {
    override fun toString(): String {
        return "Day: $day°C, Night: $night°C"
    }
}
