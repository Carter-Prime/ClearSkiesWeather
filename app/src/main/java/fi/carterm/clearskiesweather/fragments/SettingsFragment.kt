package fi.carterm.clearskiesweather.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import fi.carterm.clearskiesweather.R
import fi.carterm.clearskiesweather.databinding.FragmentSettingsBinding

class SettingsFragment: Fragment(R.layout.fragment_settings) {
    lateinit var binding: FragmentSettingsBinding


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSettingsBinding.bind(view)

    }

}



