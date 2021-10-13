package fi.carterm.clearskiesweather.fragments

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.preference.PreferenceManager
import fi.carterm.clearskiesweather.R
import fi.carterm.clearskiesweather.activities.MainActivity
import fi.carterm.clearskiesweather.databinding.FragmentOnboardingTwoBinding

/**
 *
 * On boarding Fragment Two - second screen of on boarding.
 *
 * @author Michael Carter
 * @version 1
 *
 */

class OnBoardingFragmentTwo : Fragment(R.layout.fragment_onboarding_two) {
    lateinit var binding: FragmentOnboardingTwoBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentOnboardingTwoBinding.bind(view)

        // Move straight to main activity
        binding.btnSkip.setOnClickListener {
            setSkipOnBoardingState()
            val intent = Intent (activity, MainActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }

        // Move to next screen
        binding.btnContinue.setOnClickListener {
            requireActivity().supportFragmentManager.commit {
                setReorderingAllowed(true)
                replace<OnBoardingFragmentThree>(R.id.onBoardingFragmentContainerView)
                addToBackStack(null)
            }
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