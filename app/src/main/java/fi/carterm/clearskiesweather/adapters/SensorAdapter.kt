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
import fi.carterm.clearskiesweather.models.sensors.SensorData

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class SensorAdapter(private val onClick: ((position: String) -> Unit)? = null) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val ITEM_VIEW_TYPE_HEADER = 0
    private val ITEM_VIEW_TYPE_ITEM = 1

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
                null -> listOf(DataItem.Header)
                else -> listOf(DataItem.Header) + list.map { DataItem.SensorItem(it) }
            }
            withContext(Dispatchers.Main) {
                submitList(items)
            }
        }
    }

    fun submitList(list: List<DataItem>) {
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
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        when(holder){
            is SensorHolder -> {
                val item = differ.currentList[position] as DataItem.SensorItem
                holder.sensorTitle.text = item.sensorType
                holder.sensorReading.text = item.reading.toString()
                holder.image.setImageResource(item.imageType)
                holder.itemView.setOnClickListener {
                    onClick?.invoke(item.sensorType)
                }
            }
            is HeaderHolder -> {
                val item = differ.currentList[position + 1] as DataItem.SensorItem
                holder.temperatureReading.text = item.reading.toString()
                holder.weatherCondition.text = item.sensorType
            }
        }



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
            override val id = Float.MAX_VALUE
            val reading = sensor.sensorReading
            val sensorType = sensor.sensorType
            val imageType = sensor.sensorImgResourceId

        }

        object Header: DataItem() {
            override val id = Float.MIN_VALUE

        }

        abstract val id: Float
    }


}