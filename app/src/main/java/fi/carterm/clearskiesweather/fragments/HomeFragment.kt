package fi.carterm.clearskiesweather.fragments


import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import fi.carterm.clearskiesweather.R
import fi.carterm.clearskiesweather.adapters.SensorAdapter
import fi.carterm.clearskiesweather.databinding.FragmentHomeBinding
import fi.carterm.clearskiesweather.models.SensorData


class HomeFragment: Fragment(R.layout.fragment_home) {
    lateinit var binding: FragmentHomeBinding
    private lateinit var sensorAdapter: SensorAdapter
    private var sensorArray = listOf(
        SensorData("Temperature", 23.5f, R.drawable.slightly_cloudy),
        SensorData("Humidity", 50.5f, R.drawable.cloudy),
        SensorData("Light", 230f, R.drawable.heavy_rain),
        SensorData("Pressure", 30f, R.drawable.light_rain),
        SensorData("Wind", 5.5f, R.drawable.windy),
        SensorData("UV Rating", 10f, R.drawable.snow),
        SensorData("Sunrise", 0630f, R.drawable.thunderstorm),
        SensorData("Sunset", 1830f, R.drawable.sunny_clear_sky)
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeBinding.bind(view)

        binding.rvSensorDataCards.layoutManager = GridLayoutManager(context, 2)
        sensorAdapter = SensorAdapter{
            onClick(it)
        }

        binding.rvSensorDataCards.adapter = sensorAdapter
        sensorAdapter.submitList(sensorArray)

    }

    private fun onClick(position: Int) {
        Log.d("weatherTest", "Position $position")
    }

    }


