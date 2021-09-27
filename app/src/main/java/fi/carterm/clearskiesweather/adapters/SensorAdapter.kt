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
import fi.carterm.clearskiesweather.models.SensorData


class SensorAdapter( private val onClick: ((position: Int) -> Unit)? = null) : RecyclerView.Adapter<SensorAdapter.SensorHolder>() {

    private val diffCallback = object : DiffUtil.ItemCallback<SensorData>() {

        override fun areItemsTheSame(oldItem: SensorData, newItem: SensorData): Boolean {
            return oldItem.sensorType == newItem.sensorType
        }

        override fun areContentsTheSame(oldItem: SensorData, newItem: SensorData): Boolean {
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