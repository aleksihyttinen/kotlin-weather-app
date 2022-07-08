package fi.tuni.weather_app

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

//Custom adapter for recyclerView
//Made with help from these sources:
//https://www.geeksforgeeks.org/android-recyclerview-in-kotlin/
//https://developer.android.com/guide/topics/ui/layout/recyclerview
class CustomAdapter(private val dataSet: List<ItemsViewModel>) : RecyclerView.Adapter<CustomAdapter.ViewHolder>() {

    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the weather_item view
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.weather_item, parent, false)
        return ViewHolder(view)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val ItemsViewModel = dataSet[position]
        //Sets the variables from dataSet to their corresponding views
        Picasso.get().load("https://openweathermap.org/img/wn/${ItemsViewModel.icon}@2x.png").into(holder.imageView)
        holder.dateView.text = ItemsViewModel.date
        holder.descriptionView.text = ItemsViewModel.description
        holder.temperatureView.text = ItemsViewModel.temperature
        holder.windSpeedView.text = ItemsViewModel.windspeed

    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return dataSet.size
    }

    // Holds the views for adding it to image and text
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val imageView: ImageView = itemView.findViewById(R.id.image)
        val descriptionView: TextView = itemView.findViewById(R.id.description)
        val dateView: TextView = itemView.findViewById(R.id.date)
        val temperatureView: TextView = itemView.findViewById(R.id.temperature)
        val windSpeedView: TextView = itemView.findViewById(R.id.windSpeed)
    }
}