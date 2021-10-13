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

/**
 *
 * Forecast Fragment - displays a recyclerview list of the weeks weather forecast based on the
 * location set/displayed at the top of the view.
 *
 * @author Michael Carter
 * @version 1
 *
 */
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

        // Observer of location data and sets text view with address.
        forecastViewModel.getLocationLiveData().observe(viewLifecycleOwner) {
            binding.tvLocation.text = if (forecastViewModel.useCurrentLocation) {
                it.address
            } else {
                forecastViewModel.getOtherLocation().value?.address
            }
        }

        // Observer of new weather api call and then insets relevant data to forecast table
        forecastViewModel.weather.observe(viewLifecycleOwner) {
            forecastViewModel.insertForecast(it)
        }

        // Observer if another location is being used instead of current location insert data to
        // forecast table
        forecastViewModel.otherLocationWeather.observe(viewLifecycleOwner) {
            forecastViewModel.insertForecast(it)
        }

        // Observer of forecast table in database, on change submit new data to recyclerview
        forecastViewModel.getForecast().observe(viewLifecycleOwner) {
            forecastAdapter.submitList(it)
        }
    }

    }

    /**
     * Creates menu icons in the top toolbar
     * sets phone sensor toggle to invisible
     */
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.top_right_menu, menu)
        menu.findItem(R.id.btn_toggle_sensors).isVisible = false
    }

    /**
     * Navigate to settings fragment when menu item selected
     */
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
    }
}