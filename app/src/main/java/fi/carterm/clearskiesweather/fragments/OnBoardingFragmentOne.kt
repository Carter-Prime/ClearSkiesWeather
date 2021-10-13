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
import fi.carterm.clearskiesweather.databinding.FragmentOnboardingOneBinding

class OnBoardingFragmentOne: Fragment(R.layout.fragment_onboarding_one) {
    private lateinit var binding: FragmentOnboardingOneBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentOnboardingOneBinding.bind(view)


        binding.btnSkip.setOnClickListener {
            setSkipOnBoardingState()
            val intent = Intent (activity, MainActivity::class.java)
                startActivity(intent)
            requireActivity().finish()
        }

        binding.btnContinue.setOnClickListener {
                requireActivity().supportFragmentManager.commit {
                    setReorderingAllowed(true)
                    replace<OnBoardingFragmentTwo>(R.id.onBoardingFragmentContainerView)
                    addToBackStack(null)
                }
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
