package fi.carterm.clearskiesweather.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import fi.carterm.clearskiesweather.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


    }

    fun addingNumbers(num1: Int, num2: Int): Int{
        return num1 + num2
    }
}