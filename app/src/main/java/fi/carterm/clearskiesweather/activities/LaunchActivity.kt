package fi.carterm.clearskiesweather.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import fi.carterm.clearskiesweather.R
import kotlinx.coroutines.*

class LaunchActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("WeatherTest", "Launch activity one")
        val view = findViewById<View>(R.id.launch_layout)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        WindowInsetsControllerCompat(window, view).let { controller ->
            controller.hide(WindowInsetsCompat.Type.systemBars())
            controller.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }

        val intent = Intent (this, OnBoardingActivity::class.java)
        CoroutineScope(Dispatchers.Main).launch {
            Log.d("WeatherTest", "Launch activity two")
            delay(2000)
            startActivity(intent)
            finish()
        }


    }

}