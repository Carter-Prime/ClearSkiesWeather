package fi.carterm.clearskiesweather.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import fi.carterm.clearskiesweather.R
import fi.carterm.clearskiesweather.databinding.FragmentForcastBinding

class ForecastFragment: Fragment(R.layout.fragment_forcast) {
    lateinit var binding: FragmentForcastBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentForcastBinding.bind(view)

        binding.button.setOnClickListener {
            it.findNavController().navigate(R.id.action_forecastFragment_to_sensorFragment)
        }

    }
}