package fi.carterm.clearskiesweather.adapters

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
import fi.carterm.clearskiesweather.models.SensorData


class SensorAdapter( private val onClick: ((position: Int) -> Unit)? = null) : RecyclerView.Adapter<SensorAdapter.SensorHolder>() {

    companion object{
        var tempImage = R.drawable.clipart_temperature
        var humidityImage = R.drawable.clipart_humidity
        var lightImage = R.drawable.clipart_light
        var pressureImage = R.drawable.clipart_barometer
        var uvImage = R.drawable.clipart_uv_rating
        var sunriseImage = R.drawable.clipart_sunrise
        var sunsetImage = R.drawable.clipart_sunset
    }

    private val diffCallback = object : DiffUtil.ItemCallback<SensorData>() {

        override fun areItemsTheSame(oldItem: SensorData, newItem: SensorData): Boolean {
            Log.d("differCall", "Are Items the same: ${oldItem.sensorReading} ${newItem.sensorReading}")
            return oldItem.sensorReading == newItem.sensorReading
        }

        override fun areContentsTheSame(oldItem: SensorData, newItem: SensorData): Boolean {
            Log.d("differCall", "Are contents the same: ${oldItem == newItem}")
            return oldItem == newItem
        }

    }

    private val differ = AsyncListDiffer(this, diffCallback)

    fun submitList(list: List<SensorData>) {
        differ.submitList(list)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SensorHolder {
        return SensorHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.sensor_card, parent, false)
            )
    }

    override fun onBindViewHolder(holder: SensorHolder, position: Int) {
        holder.sensorType.text = differ.currentList[position].sensorType
        holder.sensorValue.text = differ.currentList[position].sensorReading.toString()
        holder.sensorImage.setImageResource(differ.currentList[position].sensorImgResourceId)


                holder.itemView.setOnClickListener{
                    onClick?.invoke(position)
                }

            }


    inner class SensorHolder(view: View): RecyclerView.ViewHolder(view){

        val sensorType: TextView = view.findViewById(R.id.tv_sensor_type)
        val sensorValue: TextView = view.findViewById(R.id.tv_sensor_reading)
        val sensorImage: ImageView = view.findViewById(R.id.img_sensorImage)

    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }


}