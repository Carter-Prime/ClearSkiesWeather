package fi.carterm.clearskiesweather.fragments

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
import fi.carterm.clearskiesweather.R
import fi.carterm.clearskiesweather.adapters.SensorAdapter
import fi.carterm.clearskiesweather.databinding.FragmentHomeBinding
import fi.carterm.clearskiesweather.services.background.SensorService
import fi.carterm.clearskiesweather.utilities.WeatherApplication
import fi.carterm.clearskiesweather.utilities.managers.PermissionsManager
import fi.carterm.clearskiesweather.viewmodels.WeatherViewModel


class HomeFragment : Fragment(R.layout.fragment_home) {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var sensorAdapter: SensorAdapter
    private lateinit var weatherViewModel: WeatherViewModel
    private lateinit var app: WeatherApplication


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        binding = FragmentHomeBinding.bind(view)
        val viewModel: WeatherViewModel by activityViewModels()
        weatherViewModel = viewModel
        app = activity?.application as WeatherApplication

        PermissionsManager.hasLocationPermissions(requireContext(), requireActivity())
        checkSensors()

        binding.tvCurrentLocation.text = "Loading..."


        if (app.onPhoneSensors) {
            LocalBroadcastManager.getInstance(requireContext()).registerReceiver(
                broadcastReceiver, IntentFilter(
                    SensorService.KEY_ON_SENSOR_CHANGED_ACTION
                )
            )
            binding.tvUsingPhoneSensors.visibility = VISIBLE
        }

        val mLayoutManager = GridLayoutManager(context, 2)
        mLayoutManager.spanSizeLookup = object : SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return when (sensorAdapter.getItemViewType(position)) {
                    0 -> 2
                    1 -> 1
                    else -> 1
                }
            }
        }

        binding.rvSensorDataCards.layoutManager = mLayoutManager
        sensorAdapter = SensorAdapter(requireActivity().application) {
            onClick(it)
        }
        binding.rvSensorDataCards.adapter = sensorAdapter
        binding.rvSensorDataCards.itemAnimator = null

        weatherViewModel.isLoading.observe(viewLifecycleOwner){ isLoading ->
            if(isLoading){
                binding.tvCurrentLocation.text = getString(R.string.placeholder_is_loading)
            }
        }

        weatherViewModel.getLocationLiveData().observe(viewLifecycleOwner) {
            binding.tvCurrentLocation.text = if (weatherViewModel.useCurrentLocation) {
                weatherViewModel.isLoading.value = false
                it.address
            } else {
                weatherViewModel.isLoading.value = false
                weatherViewModel.getOtherLocation().value?.address
            }
        }

        weatherViewModel.otherLocationWeather.observe(viewLifecycleOwner) {
            weatherViewModel.insertWeather(it)
        }

        weatherViewModel.getLocationError().observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
        }

        weatherViewModel.getLatestPhoneSensorData().observe(viewLifecycleOwner) {
            if (it != null && app.onPhoneSensors) {
                sensorAdapter.addHeaderAndSubmitList(
                    weatherViewModel.createListOfPhoneSensorData(it),
                    app.onPhoneSensors
                )
            }
        }

        weatherViewModel.getLatestWeather().observe(viewLifecycleOwner) {
            if (it != null && !app.onPhoneSensors) {
                sensorAdapter.addHeaderAndSubmitList(
                    weatherViewModel.createListOfCurrentWeatherData(it),
                    app.onPhoneSensors
                )
            }
        }

        weatherViewModel.weather.observe(viewLifecycleOwner) {
            weatherViewModel.insertWeather(it)
        }

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.top_right_menu, menu)
        menu.findItem(R.id.btn_toggle_sensors).tooltipText =
            getString(R.string.tooltip_text_toggle_phone_sensors)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.btn_toggle_sensors -> {
                if (app.onPhoneSensors) {
                    stopService()
                    sensorAdapter.addHeaderAndSubmitList(weatherViewModel.getLatestWeather().value?.let {
                        weatherViewModel.createListOfCurrentWeatherData(it)
                    }, !app.onPhoneSensors)
                    binding.tvUsingPhoneSensors.visibility = GONE
                } else {
                    startService()
                    binding.tvUsingPhoneSensors.visibility = VISIBLE
                }
                saveOnSensorState()
                true
            }
            R.id.btn_settings_menu -> {
                findNavController().navigate(R.id.action_homeFragment_to_settingsFragment)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun stopService() {
        LocalBroadcastManager.getInstance(requireContext()).unregisterReceiver(broadcastReceiver)
        requireContext().stopService(Intent(activity, SensorService::class.java))


    }

    private fun startService() {
        LocalBroadcastManager.getInstance(requireContext()).registerReceiver(
            broadcastReceiver, IntentFilter(
                SensorService.KEY_ON_SENSOR_CHANGED_ACTION
            )
        )
        startForegroundServiceForSensors(false)
    }

    private fun saveOnSensorState() {
        val sharedPref = PreferenceManager.getDefaultSharedPreferences(requireContext())
        with(sharedPref.edit()) {
            putBoolean("toggleSensorOn", !app.onPhoneSensors)
            apply()
        }
    }

    override fun onResume() {
        super.onResume()
        if (app.onPhoneSensors) {
            startForegroundServiceForSensors(false)
        }
        checkSensors()
    }

    private fun startForegroundServiceForSensors(background: Boolean) {
        val sensorServiceIntent = Intent(activity, SensorService::class.java)
        sensorServiceIntent.putExtra(SensorService.KEY_BACKGROUND, background)
        requireActivity().startForegroundService(sensorServiceIntent)
    }

    override fun onPause() {
        super.onPause()
        if (app.onPhoneSensors) {
            startForegroundServiceForSensors(true)
        }
        checkSensors()
    }

    override fun onDestroy() {
        if (app.onPhoneSensors) {
            LocalBroadcastManager.getInstance(requireContext())
                .unregisterReceiver(broadcastReceiver)
        }
        checkSensors()
        super.onDestroy()
    }

    private val broadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            weatherViewModel.lightOnChangeSaveToDatabase(
                intent.getFloatExtra(
                    SensorService.KEY_LIGHT,
                    -1000f
                )
            )
            weatherViewModel.temperatureOnChangeSaveToDatabase(
                intent.getFloatExtra(
                    SensorService.KEY_TEMP,
                    -1000f
                )
            )
            weatherViewModel.pressureOnChangeSaveToDatabase(
                intent.getFloatExtra(
                    SensorService.KEY_PRESSURE,
                    -1000f
                )
            )
            weatherViewModel.humidityOnChangeSaveToDatabase(
                intent.getFloatExtra(
                    SensorService.KEY_HUMIDITY,
                    -1000f
                )
            )
        }
    }

    private fun onClick(sensorType: String) {
        val bundle = Bundle()
        bundle.putString("sensorType", sensorType)
        findNavController().navigate(R.id.action_homeFragment_to_graphFragment, bundle)
    }

    private fun checkSensors() {

        val pm: PackageManager = requireActivity().packageManager
        if (!pm.hasSystemFeature(PackageManager.FEATURE_LOCATION_GPS)) {
            weatherViewModel.gpsSensor = false
        }
        if (!pm.hasSystemFeature(PackageManager.FEATURE_SENSOR_LIGHT)) {
            weatherViewModel.lightSensor = false
        }
        if (!pm.hasSystemFeature(PackageManager.FEATURE_SENSOR_AMBIENT_TEMPERATURE)) {
            weatherViewModel.tempSensor = false
        }
        if (!pm.hasSystemFeature(PackageManager.FEATURE_SENSOR_RELATIVE_HUMIDITY)) {
            weatherViewModel.humiditySensor = false
        }
        if (!pm.hasSystemFeature(PackageManager.FEATURE_SENSOR_BAROMETER)) {
            weatherViewModel.pressureSensor = false
        }
    }

}


