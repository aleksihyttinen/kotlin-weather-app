package fi.tuni.weather_app

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import androidx.core.app.ActivityCompat
import com.fasterxml.jackson.databind.ObjectMapper
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.squareup.picasso.Picasso
import fi.tuni.weather_app.jackson_classes.Coords
import fi.tuni.weather_app.jackson_classes.WeatherItem
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import kotlin.concurrent.thread


class MainActivity : AppCompatActivity() {
    //Declare everything needed
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var latitude: Double? = null
    private var longitude: Double? = null
    lateinit var locationView : TextView
    lateinit var dateView : TextView
    lateinit var temperatureView : TextView
    lateinit var windSpeedView : TextView
    lateinit var descriptionView : TextView
    lateinit var imageView : ImageView
    lateinit var forecastButton : Button
    private var cityName: String? = null
    private var newCityName: String? = null
    lateinit var editText: EditText
    lateinit var searchButton: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //Init locationClient and get last known location
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        val permissions = checkPermission()
        if(permissions) {
            fusedLocationClient.lastLocation
                .addOnSuccessListener { loc : Location? ->
                    latitude = loc?.latitude
                    longitude = loc?.longitude
                    getWeather(latitude, longitude)
                }
        }
        //init all views
        locationView = findViewById(R.id.location)
        dateView = findViewById(R.id.date)
        temperatureView = findViewById(R.id.temperature)
        windSpeedView = findViewById(R.id.windSpeed)
        descriptionView = findViewById(R.id.description)
        imageView = findViewById(R.id.image)
        forecastButton = findViewById(R.id.forecastButton)
        forecastButton.setOnClickListener {
            //If cityName isn't null (so the url request has gone through)
            //Set on click listener to switch to a second view
            //If it is null, make a toast that asks user to try again once request has loaded
            if(cityName != null) {
                val intent = Intent(this, ForecastActivity::class.java)
                intent.putExtra("latitude", latitude)
                intent.putExtra("longitude", longitude)
                intent.putExtra("city", cityName)
                startActivity(intent)
            } else {
                val toast = Toast.makeText(this, "Try again later", Toast.LENGTH_SHORT)
                toast.show();
            }
        }
        //Button and textfield for changing the city
        searchButton = findViewById(R.id.searchButton)
        searchButton.setOnClickListener {
            newCityName = editText.text.toString()
            if(newCityName?.lowercase() != cityName?.lowercase() && newCityName != null) {
                getWeatherByName(newCityName!!)
            } else {
                val toast = Toast.makeText(this, "Enter a different city", Toast.LENGTH_SHORT)
                toast.show();
            }
        }
        editText = findViewById(R.id.search)


    }
    //gets weather objects from url request to API
    private fun getWeather(latitude: Double?, longitude: Double?) {
        thread {
            var weather = getUrl(
                "https://api.openweathermap.org/data/2.5/weather?lat=${
                    latitude
                }&lon=${
                    longitude
                }&appid={INSERT API KEY HERE}&units=metric"
            )
            if (weather != null) {
                //Create object mapper and weatherItem object
                val mp = ObjectMapper()
                val weatherItem: WeatherItem = mp.readValue(weather, WeatherItem::class.java)
                var iconCode = weatherItem.icon
                runOnUiThread {
                    //Sets weather information to their corresponding views
                    cityName = weatherItem.name
                    locationView.text = cityName
                    Picasso.get().load("https://openweathermap.org/img/wn/$iconCode@2x.png").into(imageView)
                    dateView.text = weatherItem.date
                    descriptionView.text = weatherItem.desc
                    temperatureView.text = weatherItem.temp
                    windSpeedView.text = weatherItem.windSpeed
                }

            }

        }
    }
    //Same as getWeather but done with city name and added the ability to map coordinates with jackson
    private fun getWeatherByName(city: String) {
        thread {
            var weather = getUrl(
                    "https://api.openweathermap.org/data/2.5/weather?q=$city&appid={INSERT API KEY HERE}&units=metric"
            )
            if (weather != null) {
                //Create object mapper and weatherItem object
                val mp = ObjectMapper()
                val weatherItem: WeatherItem = mp.readValue(weather, WeatherItem::class.java)
                val coords: Coords = mp.readValue(weather, Coords::class.java)
                latitude = coords.latitude
                longitude = coords.longitude
                var iconCode = weatherItem.icon
                runOnUiThread {
                    //Sets weather information to their corresponding views
                    cityName = weatherItem.name
                    locationView.text = cityName
                    Picasso.get().load("https://openweathermap.org/img/wn/$iconCode@2x.png").into(imageView)
                    dateView.text = weatherItem.date
                    descriptionView.text = weatherItem.desc
                    temperatureView.text = weatherItem.temp
                    windSpeedView.text = weatherItem.windSpeed
                }

            }

        }
    }
    //Asks permission for location, if they don't exist
    //ask the user for permissions
    private fun checkPermission()  :Boolean{
        return if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                101
            )
            false
        } else {
            true
        }
    }
    //Makes a get request to url and returns the result as a string
    fun getUrl(userUrl: String): String?{
        var result: String? = null
        val sb = StringBuffer()
        val url = URL(userUrl)
        val conn: HttpURLConnection = url.openConnection()  as HttpURLConnection
        try {
            val reader = BufferedReader(InputStreamReader(conn.inputStream))
            reader.use {
                var line: String? = null
                do {
                    line = it.readLine()
                    sb.append(line)
                } while (line != null)
                result = sb.toString()
            }
            cityName = newCityName
        } catch(e: java.io.FileNotFoundException) {
            runOnUiThread{
                val toast = Toast.makeText(this, "Location not found", Toast.LENGTH_SHORT)
                toast.show()
            }
        }

        return result
    }
}