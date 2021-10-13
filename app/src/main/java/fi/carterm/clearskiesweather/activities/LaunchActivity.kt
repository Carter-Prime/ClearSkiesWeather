package fi.carterm.clearskiesweather.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import fi.carterm.clearskiesweather.R
import fi.carterm.clearskiesweather.utilities.WeatherApplication
import kotlinx.coroutines.Runnable

/**
 * Activity for hosting the splash screen of the application. After a timeout will move either to on
 * boarding activity or main activity.
 *
 * @author Michael Carter
 * @version 1
 * */
class LaunchActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val view = findViewById<View>(R.id.launch_layout)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        WindowInsetsControllerCompat(window, view).let { controller ->
            controller.hide(WindowInsetsCompat.Type.systemBars())
            controller.systemBarsBehavior =
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }

        val app = application as WeatherApplication

        if (app.skipOnBoarding) {
            Handler(Looper.getMainLooper()).postDelayed(Runnable {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }, 2000)
        } else {
            Handler(Looper.getMainLooper()).postDelayed(Runnable {
                val intent = Intent(this, OnBoardingActivity::class.java)
                startActivity(intent)
                finish()
            }, 2000)
        }


    }

}