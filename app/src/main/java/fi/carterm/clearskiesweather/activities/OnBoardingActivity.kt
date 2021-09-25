package fi.carterm.clearskiesweather.activities

import android.app.Application
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.add
import androidx.fragment.app.commit
import androidx.lifecycle.AndroidViewModel
import fi.carterm.clearskiesweather.R
import fi.carterm.clearskiesweather.fragments.OnBoardingFragmentOne

class OnBoardingActivity() : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_on_boarding)

        if (savedInstanceState == null) {
            supportFragmentManager.commit {
                setReorderingAllowed(true)
                add<OnBoardingFragmentOne>(R.id.onBoardingFragmentContainerView)
            }
        }
    }
}