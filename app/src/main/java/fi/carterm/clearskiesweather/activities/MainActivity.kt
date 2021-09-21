package fi.carterm.clearskiesweather.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.add
import androidx.fragment.app.commit
import fi.carterm.clearskiesweather.R
import fi.carterm.clearskiesweather.fragments.HomeFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) {
            supportFragmentManager.commit {
                setReorderingAllowed(true)
                add<HomeFragment>(R.id.fragmentContainerView)
            }
        }

    }

    fun addingNumbers(num1: Int, num2: Int): Int{
        return num1 + num2
    }
}