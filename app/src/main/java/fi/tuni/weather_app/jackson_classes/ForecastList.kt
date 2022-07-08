package fi.tuni.weather_app.jackson_classes

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

//Data class for creating a list from API response which contains ForecastItems
@JsonIgnoreProperties(ignoreUnknown = true)
data class ForecastList(var daily: MutableList<ForecastItem>? = null) {
}