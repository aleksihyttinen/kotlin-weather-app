package fi.tuni.weather_app.jackson_classes

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
//Data class for coordinates, also toString and get methods for lat and lon
@JsonIgnoreProperties(ignoreUnknown = true)
data class Coords(var coord: Coordinate? = null){
    override fun toString(): String {
        return "$latitude $longitude"
    }
    val longitude: Double?
        get() = coord?.lon
    val latitude: Double?
        get() = coord?.lat
}
