package fi.carterm.clearskiesweather.fragments

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import fi.carterm.clearskiesweather.R
import fi.carterm.clearskiesweather.adapters.ForecastAdapter
import fi.carterm.clearskiesweather.databinding.FragmentForcastBinding
import fi.carterm.clearskiesweather.viewmodels.WeatherViewModel

class ForecastFragment : Fragment(R.layout.fragment_forcast) {
    lateinit var binding: FragmentForcastBinding
    private lateinit var forecastViewModel: WeatherViewModel
    private lateinit var forecastAdapter: ForecastAdapter


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        binding = FragmentForcastBinding.bind(view)
        val viewModel: WeatherViewModel by activityViewModels()
        forecastViewModel = viewModel

        binding.rvForecastList.layoutManager = LinearLayoutManager(context)
        forecastAdapter = ForecastAdapter(requireActivity().application) {
            onClick(it)
        }
        binding.rvForecastList.adapter = forecastAdapter


        forecastViewModel.getLocationLiveData().observe(viewLifecycleOwner) {
            binding.tvLocation.text = if (forecastViewModel.useCurrentLocation) {
                it.address
            } else {
                forecastViewModel.getOtherLocation().value?.address
            }
        }

        forecastViewModel.weather.observe(viewLifecycleOwner) {
            forecastViewModel.insertForecast(it)
        }
        forecastViewModel.otherLocationWeather.observe(viewLifecycleOwner) {
            forecastViewModel.insertForecast(it)
        }

        forecastViewModel.getForecast().observe(viewLifecycleOwner) {
            forecastAdapter.submitList(it)
        }

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.top_right_menu, menu)
        menu.findItem(R.id.btn_toggle_sensors).isVisible = false
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.btn_settings_menu -> {
                findNavController().navigate(R.id.action_forecastFragment_to_settingsFragment)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun onClick(position: Int) {
        Log.d("tag", "data $position")
        TODO()
    }
}