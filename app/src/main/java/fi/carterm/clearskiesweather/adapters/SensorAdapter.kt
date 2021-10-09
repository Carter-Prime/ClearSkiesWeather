package fi.carterm.clearskiesweather.adapters


import android.app.Application
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import fi.carterm.clearskiesweather.R
import fi.carterm.clearskiesweather.models.apiRoomCache.CurrentWeatherModel
import fi.carterm.clearskiesweather.models.sensors.SensorData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.Instant
import java.time.ZoneId


class SensorAdapter(val application: Application, private val onClick: ((position: String) -> Unit)? = null):
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

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

    private val clipartImage = listOf(
        Pair(application.getString(R.string.sensor_temperature), R.drawable.clipart_temperature),
        Pair(application.getString(R.string.sensor_humidity), R.drawable.clipart_humidity),
        Pair(application.getString(R.string.sensor_light), R.drawable.clipart_light),
        Pair(application.getString(R.string.sensor_visibility), R.drawable.clipart_light),
        Pair(application.getString(R.string.sensor_pressure), R.drawable.clipart_barometer),
        Pair(application.getString(R.string.sensor_wind), R.drawable.clipart_windy),
        Pair(application.getString(R.string.sensor_uv_rating), R.drawable.clipart_uv_rating),
        Pair(application.getString(R.string.sensor_sunrise), R.drawable.clipart_sunrise),
        Pair(application.getString(R.string.sensor_sunset), R.drawable.clipart_sunset),
        Pair(application.getString(R.string.sensor_absolute_humidity), R.drawable.ic_cloud),
        Pair(application.getString(R.string.sensor_dew_point), R.drawable.ic_phone),

        )

    private companion object {
        const val ITEM_VIEW_TYPE_HEADER = 0
        const val ITEM_VIEW_TYPE_ITEM = 1
        val defaultWeather = CurrentWeatherModel("Default", "", "noIcon")

    }

    private val adapterScope = CoroutineScope(Dispatchers.Default)

    private val diffCallback = object : DiffUtil.ItemCallback<DataItem>() {

        override fun areItemsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
            return oldItem == newItem
        }

    }

    private val differ = AsyncListDiffer(this, diffCallback)

    fun addHeaderAndSubmitList(list: List<SensorData>?) {
        adapterScope.launch {
            val items = when (list) {
                null -> listOf(
                    DataItem.Header(
                        SensorData(
                            "Header", 0,
                            defaultWeather
                        )
                    )
                )
                else -> listOf(DataItem.Header(list[0])) + list.map { DataItem.SensorItem(it) }
            }
            withContext(Dispatchers.Main) {
                submitList(items)
            }
        }
    }

    private fun submitList(list: List<DataItem>) {
        differ.submitList(list)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ITEM_VIEW_TYPE_HEADER -> HeaderHolder.from(parent)
            ITEM_VIEW_TYPE_ITEM -> SensorHolder.from(parent)
            else -> throw ClassCastException("Unknown viewType $viewType")
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (differ.currentList[position]) {
            is DataItem.Header -> ITEM_VIEW_TYPE_HEADER
            is DataItem.SensorItem -> ITEM_VIEW_TYPE_ITEM
            else -> throw ClassCastException("Unknown Type ${differ.currentList[position]}")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        when (holder) {
            is SensorHolder -> {
                val item = differ.currentList[position] as DataItem.SensorItem
                holder.sensorTitle.text = item.sensorType
                holder.sensorReading.text = setSensorReading(item = item)
                holder.image.setImageResource(getClipartImage(item.sensorType))

                holder.itemView.setOnClickListener {
                    onClick?.invoke(item.sensorType)
                }

            }
            is HeaderHolder -> {
                val item = differ.currentList[position] as DataItem.Header
                holder.temperatureReading.text = setHeaderReading(item)
                holder.weatherCondition.text = item.weatherCondition?.description ?: ""
                item.weatherCondition?.let {
                    getWeatherImage(
                        it.main
                    )
                }?.let { holder.backgroundImage.setImageResource(it) }
            }
        }

    }

    private fun setSensorReading(item: DataItem.SensorItem): String {
        if(item.reading == -1000f){
            return "-"
        }else{
            return when (item.sensorType){
                application.getString(R.string.sensor_temperature) -> String.format("%.1f°C", item.reading)
                application.getString(R.string.sensor_pressure) -> String.format("%s hPa", item.reading.toString())
                application.getString(R.string.sensor_light) -> String.format("%.1f lux", item.reading)
                application.getString(R.string.sensor_visibility) -> String.format("%s m", item.reading.toString())
                application.getString(R.string.sensor_humidity) -> String.format("%s %%", item.reading.toString())
                application.getString(R.string.sensor_wind) -> String.format("%.1f m/s", item.reading)
                application.getString(R.string.sensor_absolute_humidity) -> String.format("%.2f %%", item.reading)
                application.getString(R.string.sensor_dew_point) -> String.format("%.1f°C", item.reading)
                application.getString(R.string.sensor_sunset) -> String.format("%s", convertToLocalTime(item.reading as Float))
                application.getString(R.string.sensor_sunrise) -> String.format("%s", convertToLocalTime(item.reading as Float))
                application.getString(R.string.sensor_uv_rating) -> String.format("%.2f UV Index", item.reading)
                else -> application.getString(R.string.error_no_weather_reading)
            }
        }

    }

    private fun setHeaderReading(item: DataItem.Header): String {
        if(item.reading == -1000f){
            return "-"
        }else{
            return when (item.sensorType){
                application.getString(R.string.sensor_temperature) -> String.format("%.1f°C", item.reading)
                application.getString(R.string.sensor_pressure) -> String.format("%s hPa", item.reading.toString())
                application.getString(R.string.sensor_light) -> String.format("%.1f lux", item.reading)
                application.getString(R.string.sensor_visibility) -> String.format("%s m", item.reading.toString())
                application.getString(R.string.sensor_humidity) -> String.format("%s %%", item.reading.toString())
                application.getString(R.string.sensor_wind) -> String.format("%.1f m/s", item.reading)
                application.getString(R.string.sensor_absolute_humidity) -> String.format("%.2f %%", item.reading)
                application.getString(R.string.sensor_dew_point) -> String.format("%.1f°C", item.reading)
                application.getString(R.string.sensor_sunset) -> String.format("%s", convertToLocalTime(item.reading as Float))
                application.getString(R.string.sensor_sunrise) -> String.format("%s", convertToLocalTime(item.reading as Float))
                application.getString(R.string.sensor_uv_rating) -> String.format("%.2f UV Index", item.reading)
                else -> application.getString(R.string.error_no_weather_reading)
            }
        }

    }

    private fun getWeatherImage(data: String): Int {
        val pair = backgroundImage.filter { item -> item.first == data }
        return if (pair.isEmpty()) {
            backgroundImage[7].second
        } else {
            pair[0].second
        }

    }

    private fun getClipartImage(data: String): Int {
        val pair = clipartImage.filter { item -> item.first == data }
        return if (pair.isEmpty()) {
            clipartImage[0].second
        } else {
            pair[0].second
        }
    }

    private fun convertToLocalTime(time: Float): String{
      return Instant.ofEpochSecond(time.toLong())
            .atZone(ZoneId.systemDefault())
            .toLocalTime().toString()
    }


    class SensorHolder(view: View) : RecyclerView.ViewHolder(view) {

        val sensorTitle: TextView = view.findViewById(R.id.tv_sensor_type)
        val sensorReading: TextView = view.findViewById(R.id.tv_sensor_reading)
        val image: ImageView = view.findViewById(R.id.img_sensorImage)


        companion object {
            fun from(parent: ViewGroup): SensorHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = layoutInflater.inflate(R.layout.sensor_card, parent, false)
                return SensorHolder(view)
            }
        }

    }

    class HeaderHolder(view: View) : RecyclerView.ViewHolder(view) {

        val temperatureReading: TextView = view.findViewById(R.id.tv_temperature)
        val weatherCondition: TextView = view.findViewById(R.id.tv_weatherDescription)
        val backgroundImage: ImageView = view.findViewById(R.id.img_background)

        companion object {
            fun from(parent: ViewGroup): HeaderHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = layoutInflater.inflate(R.layout.home_fragment_header, parent, false)
                return HeaderHolder(view)
            }
        }
    }

    sealed class DataItem {
        data class SensorItem(val sensor: SensorData) : DataItem() {
            override val id = sensor.sensorReading as Float
            val reading = sensor.sensorReading
            val sensorType = sensor.sensorType
            val weather = sensor.condition

        }

        data class Header(val sensor: SensorData) : DataItem() {
            override val id = sensor.sensorReading as Float
            val sensorType = sensor.sensorType
            val reading = sensor.sensorReading
            val weatherCondition = sensor.condition

        }

        abstract val id: Float
    }


}