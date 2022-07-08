package fi.tuni.weather_app

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fasterxml.jackson.databind.ObjectMapper
import fi.tuni.weather_app.jackson_classes.ForecastItem
import fi.tuni.weather_app.jackson_classes.ForecastList
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import kotlin.concurrent.thread

class ForecastActivity : AppCompatActivity() {
    //Declare everything needed
    lateinit var data: MutableList<ItemsViewModel>
    lateinit var recyclerView: RecyclerView
    lateinit var adapter: CustomAdapter
    lateinit var locationView: TextView
    private var latitude: Double? = null
    private var longitude: Double? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forecast)
        //Create back button in title bar
        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
        //Get location and city name from intent
        val intent = intent
        latitude = intent.getDoubleExtra("latitude", 0.0)
        longitude = intent.getDoubleExtra("longitude", 0.0)
        //Set city name to it's view
        locationView = findViewById(R.id.location)
        locationView.text = intent.getStringExtra("city")
        //init recyclerView
        recyclerView = findViewById(R.id.recyclerview)
        // Create a LayoutManager for the recyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)
        // init data and pass it to the adapter
        data = ArrayList()
        adapter = CustomAdapter(data)
        // Setting the Adapter to the recyclerView
        recyclerView.adapter = adapter
        if (latitude != null && longitude != null) {
            getWeather(latitude, longitude)
        }
    }
    //Functionality for the back button
    //https://stackoverflow.com/a/27212978
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
    //Functionality for the back button
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        return true
    }
    //gets weather objects from url request to API
    private fun getWeather(latitude: Double?, longitude: Double?) {
        thread {
            var weather = getUrl(
                "https://api.openweathermap.org/data/2.5/onecall?lat=${
                    latitude
                }&lon=${
                    longitude
                }&exclude=minutely,hourly,alerts,current&appid={INSERT API KEY HERE}&units=metric"
            )
            if (weather != null) {
                val mp = ObjectMapper()
                val forecastList: ForecastList = mp.readValue(weather, ForecastList::class.java)
                val iterableList = forecastList.daily
                //Goes through the forecast list and sends each forecast to the recyclerView adapter
                if(iterableList != null) {
                    runOnUiThread {
                        for(item: ForecastItem? in iterableList) {
                            data.add(ItemsViewModel(icon = item?.icon, date = item?.date, description = item?.desc, temperature = item?.temp.toString(), windspeed = item?.windSpeed.toString()))
                        }
                        //Tells the adapter it needs to update the view
                        adapter.notifyDataSetChanged()
                    }
                }
            }

        }
    }
    //Makes a get request to url and returns the result as a string
    fun getUrl(userUrl: String): String?{
        var result: String? = null
        val sb = StringBuffer()
        val url = URL(userUrl)
        val conn: HttpURLConnection = url.openConnection()  as HttpURLConnection
        val reader = BufferedReader(InputStreamReader(conn.inputStream))
        reader.use {
            var line: String? = null
            do {
                line = it.readLine()
                sb.append(line)
            } while (line != null)
            result = sb.toString()
        }
        return result
    }
}