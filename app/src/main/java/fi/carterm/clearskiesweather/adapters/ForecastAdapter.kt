package fi.carterm.clearskiesweather.adapters

import android.app.Application
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import fi.carterm.clearskiesweather.R
import fi.carterm.clearskiesweather.models.apiRoomCache.DailyModel
import java.time.Instant
import java.time.ZoneId

class ForecastAdapter(val application: Application, private val onClick: ((position: Int) -> Unit)? = null):
    RecyclerView.Adapter<ForecastAdapter.ForecastHolder>() {

    private val backgroundImage = listOf(
        Pair(application.getString(R.string.weather_condition_thunderstorm), R.drawable.weather_thunderstorm),
        Pair(application.getString(R.string.weather_condition_drizzle), R.drawable.weather_light_rain),
        Pair(application.getString(R.string.weather_condition_rain), R.drawable.weather_heavy_rain),
        Pair(application.getString(R.string.weather_condition_snow), R.drawable.weather_snow),
        Pair(application.getString(R.string.weather_condition_mist), R.drawable.weather_misty),
        Pair(application.getString(R.string.weather_condition_clear), R.drawable.weather_sunny_clear_sky),
        Pair(application.getString(R.string.weather_condition_clouds), R.drawable.weather_cloudy),
        Pair(application.getString(R.string.weather_condition_default), R.drawable.weather_slightly_cloudy)

    )

    private val diffCallback = object : DiffUtil.ItemCallback<DailyModel>() {
        override fun areItemsTheSame(oldItem: DailyModel, newItem: DailyModel): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: DailyModel, newItem: DailyModel): Boolean {
            return oldItem == newItem
        }


    }

    private val differ = AsyncListDiffer(this, diffCallback)

    fun submitList(list: List<DailyModel>) {
        differ.submitList(list)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ForecastHolder {
        return ForecastHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ForecastHolder, position: Int) {
        val item = differ.currentList[position]
        getWeatherImage(
            item.weather.main
        ).let { holder.backgroundImage.setImageResource(it) }
        holder.dayName.text = when(position){
            0 -> "TODAY"
            1 -> "TOMORROW"
            else -> getDay(item.timestamp)
        }
        holder.weatherCondition.text = item.weather.description

        holder.itemView.setOnClickListener {
            onClick?.invoke(position)
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private fun getDay(time: Double): String {
        return Instant.ofEpochSecond(time.toLong())
            .atZone(ZoneId.systemDefault())
            .toLocalDate().dayOfWeek.toString()
    }

    private fun getWeatherImage(data: String): Int {
        val pair = backgroundImage.filter { item -> item.first == data }
        return if (pair.isEmpty()) {
            backgroundImage[7].second
        } else {
            pair[0].second
        }

    }

    class ForecastHolder(view: View): RecyclerView.ViewHolder(view){

        val dayName: TextView = view.findViewById(R.id.tv_dayName)
        val weatherCondition: TextView = view.findViewById(R.id.tv_weatherDescription)
        val backgroundImage: ImageView = view.findViewById(R.id.img_background)

        companion object {
            fun from(parent: ViewGroup): ForecastHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = layoutInflater.inflate(R.layout.forecast_item_layout, parent, false)
                return ForecastHolder(view)
            }
        }
    }

}