package fi.carterm.clearskiesweather.fragments

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import fi.carterm.clearskiesweather.R
import fi.carterm.clearskiesweather.activities.MainActivity
import fi.carterm.clearskiesweather.databinding.FragmentOnboardingThreeBinding
import fi.carterm.clearskiesweather.databinding.FragmentOnboardingTwoBinding

class OnBoardingFragmentThree : Fragment(R.layout.fragment_onboarding_three) {
    lateinit var binding: FragmentOnboardingThreeBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentOnboardingThreeBinding.bind(view)

        binding.btnContinue.setOnClickListener {
            val intent = Intent (activity, MainActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }
    }
}