package fi.tuni.weather_app.jackson_classes

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
//Data class for coordinates lat and lon values
@JsonIgnoreProperties(ignoreUnknown = true)
data class Coordinate(val lon: Double = 0.0, val lat: Double = 0.0) {

}
