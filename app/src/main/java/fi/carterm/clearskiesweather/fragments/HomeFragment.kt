package fi.carterm.clearskiesweather.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import fi.carterm.clearskiesweather.R
import fi.carterm.clearskiesweather.databinding.FragmentHomeBinding
import fi.carterm.clearskiesweather.viewmodels.HomeViewModel

class HomeFragment: Fragment(R.layout.fragment_home) {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var homeViewModel: HomeViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeBinding.bind(view)
        val viewModel : HomeViewModel by viewModels()
        homeViewModel = viewModel

        homeViewModel.weatherData.observe(viewLifecycleOwner){
            Log.d("dbApp", "Weather Data: $it")
        }
    }
}