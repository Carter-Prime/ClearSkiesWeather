package fi.carterm.clearskiesweather.fragments

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import fi.carterm.clearskiesweather.R
import fi.carterm.clearskiesweather.databinding.FragmentSettingsBinding

class SettingsFragment: Fragment(R.layout.fragment_settings) {
    lateinit var binding: FragmentSettingsBinding


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSettingsBinding.bind(view)

        binding.btnResetOnBoarding.setOnClickListener{
            setSkipOnBoardingState()
            Toast.makeText(requireContext(), "Skip on boarding has been reset", Toast.LENGTH_LONG).show()
        }

    }

    private fun setSkipOnBoardingState(){
        val sharedPref = PreferenceManager.getDefaultSharedPreferences(requireContext())
        with(sharedPref.edit()) {
            putBoolean("skipOnBoarding", false)
            apply()
        }
    }

}



