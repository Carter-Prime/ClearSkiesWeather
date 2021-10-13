package fi.carterm.clearskiesweather.fragments

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import fi.carterm.clearskiesweather.R
import fi.carterm.clearskiesweather.activities.MainActivity
import fi.carterm.clearskiesweather.databinding.FragmentOnboardingThreeBinding

/**
 *
 * On boarding Fragment Three - last screen of on boarding.
 *
 * @author Michael Carter
 * @version 1
 *
 */

class OnBoardingFragmentThree : Fragment(R.layout.fragment_onboarding_three) {
    lateinit var binding: FragmentOnboardingThreeBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentOnboardingThreeBinding.bind(view)

        // Move straight to main activity
        binding.btnContinue.setOnClickListener {
            setSkipOnBoardingState()
            val intent = Intent (activity, MainActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }
    }

    /**
     * Sets skip on Boarding to true - ensures that on boarding is not seen when the app is launched
     * again
     */
    private fun setSkipOnBoardingState(){
        val sharedPref = PreferenceManager.getDefaultSharedPreferences(requireContext())
        with(sharedPref.edit()) {
            putBoolean("skipOnBoarding", true)
            apply()
        }
    }
}