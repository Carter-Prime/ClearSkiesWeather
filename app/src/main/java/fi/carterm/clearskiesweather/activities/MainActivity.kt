package fi.carterm.clearskiesweather.activities

<<<<<<< HEAD
=======

>>>>>>> master
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
<<<<<<< HEAD
=======
import fi.carterm.clearskiesweather.fragments.InputLocationDialogFragment
>>>>>>> master


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

        configureNavigationBar(binding)

        bottomNavigationView.setupWithNavController(navController)
        val appBarConfiguration = AppBarConfiguration(
            topLevelDestinationIds = setOf(
                fi.carterm.clearskiesweather.R.id.homeFragment,
                fi.carterm.clearskiesweather.R.id.forecastFragment,
                fi.carterm.clearskiesweather.R.id.settingsFragment
            )
        )

        binding.fab.setOnClickListener {
            Log.d("tag", "fab pressed")
            InputLocationDialogFragment().show(supportFragmentManager, "MyCustomerDialog")
        }

        setupActionBarWithNavController(navController, appBarConfiguration)
        setContentView(binding.root)
    }

    private fun configureNavigationBar(binding: ActivityMainBinding){
        bottomNavigationView = binding.bottomNavView
        bottomNavigationView.background = null
        bottomNavigationView.menu.getItem(1).isEnabled = false

    }



    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    fun addingNumbers(num1: Int, num2: Int): Int {
        return num1 + num2
    }
}