package fi.carterm.clearskiesweather.activities

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import fi.carterm.clearskiesweather.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var navController: NavController
    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("WeatherTest", "Main activity")
        binding = ActivityMainBinding.inflate(layoutInflater)

        val navHostFragment = supportFragmentManager.findFragmentById(binding.navHostFragment.id) as NavHostFragment
        navController = navHostFragment.navController


        bottomNavigationView = binding.bottomNavView
        bottomNavigationView.setupWithNavController(navController)
        val appBarConfiguration = AppBarConfiguration(
            topLevelDestinationIds = setOf(
                fi.carterm.clearskiesweather.R.id.homeFragment,
                fi.carterm.clearskiesweather.R.id.forecastFragment,
                fi.carterm.clearskiesweather.R.id.settingsFragment
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        setContentView(binding.root)
    }




    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    fun addingNumbers(num1: Int, num2: Int): Int {
        return num1 + num2
    }
}