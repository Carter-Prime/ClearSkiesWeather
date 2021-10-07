package fi.carterm.clearskiesweather.adapters


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


class SensorAdapter(private val onClick: ((position: String) -> Unit)? = null) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {



    private companion object {
        const val ITEM_VIEW_TYPE_HEADER = 0
        const val ITEM_VIEW_TYPE_ITEM = 1
        val defaultWeather = CurrentWeatherModel( "test", "No Weather Found", "noIcon")

        val backgroundImage = listOf(
            Pair("Thunderstorm", R.drawable.weather_thunderstorm),
            Pair("Drizzle", R.drawable.weather_light_rain),
            Pair("Rain", R.drawable.weather_heavy_rain),
            Pair("Snow", R.drawable.weather_snow),
            Pair("Mist", R.drawable.weather_misty),
            Pair("Clear", R.drawable.weather_sunny_clear_sky),
            Pair("Clouds", R.drawable.weather_slightly_cloudy),
            //Pair("Clouds_Heavy", R.drawable.weather_cloudy),
            Pair("test", R.drawable.weather_cloudy)

        )

        val clipartImage = listOf(
            Pair("Temperature", R.drawable.clipart_temperature),
            Pair("Humidity", R.drawable.clipart_humidity),
            Pair("Light", R.drawable.clipart_light),
            Pair("Pressure", R.drawable.clipart_barometer),
            Pair("Wind", R.drawable.clipart_windy),
            Pair("UV Rating", R.drawable.clipart_uv_rating),
            Pair("Sunrise", R.drawable.clipart_sunrise),
            Pair("Sunset", R.drawable.clipart_sunset),
            Pair("Absolute Humidity", R.drawable.ic_cloud),
            Pair("Dew Point", R.drawable.ic_phone),

        )

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
                null -> listOf(DataItem.Header(SensorData("Header", 0,
                    defaultWeather)))
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

        when(holder){
            is SensorHolder -> {
                val item = differ.currentList[position] as DataItem.SensorItem
                holder.sensorTitle.text = item.sensorType
                holder.sensorReading.text = item.reading.toString()
                holder.image.setImageResource(getClipartImage(item.sensorType))
                holder.itemView.setOnClickListener {
                    onClick?.invoke(item.sensorType)
                }
            }
            is HeaderHolder -> {
                val item = differ.currentList[position] as DataItem.Header
                holder.temperatureReading.text = item.reading.toString()
                holder.weatherCondition.text = item.weatherCondition?.description ?: "No Weather Found"
                item.weatherCondition?.let {
                    getWeatherImage(
                        it.main)
                }?.let { holder.backgroundImage.setImageResource(it) }
            }
        }



    }

    private fun getWeatherImage(data: String): Int {
        val pair = backgroundImage.filter { item -> item.first == data }
        return pair[0].second
    }

    private fun getClipartImage(data: String): Int {
        val pair = clipartImage.filter { item -> item.first == data }
        return pair[0].second
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
        data class SensorItem(val sensor: SensorData): DataItem() {
            override val id = sensor.sensorReading as Float
            val reading = sensor.sensorReading
            val sensorType = sensor.sensorType
            val weather = sensor.condition

        }

        data class Header(val sensor: SensorData): DataItem() {
            override val id = sensor.sensorReading as Float
            val reading = sensor.sensorReading
            val weatherCondition = sensor.condition

        }

        abstract val id: Float
    }


}