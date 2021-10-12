package fi.carterm.clearskiesweather.fragments

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import fi.carterm.clearskiesweather.R
import fi.carterm.clearskiesweather.activities.MainActivity
import fi.carterm.clearskiesweather.databinding.FragmentOnboardingThreeBinding

class OnBoardingFragmentThree : Fragment(R.layout.fragment_onboarding_three) {
    lateinit var binding: FragmentOnboardingThreeBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentOnboardingThreeBinding.bind(view)

        binding.btnContinue.setOnClickListener {
            setSkipOnBoardingState()
            val intent = Intent (activity, MainActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }
    }

    private fun setSkipOnBoardingState(){
        val sharedPref = PreferenceManager.getDefaultSharedPreferences(requireContext())
        with(sharedPref.edit()) {
            putBoolean("skipOnBoarding", true)
            apply()
        }
    }
}