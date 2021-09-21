package fi.carterm.clearskiesweather.activities

import android.util.Log
import androidx.test.ext.junit.rules.activityScenarioRule
import androidx.test.filters.LargeTest
import org.junit.Assert.*
import org.junit.Rule

import org.junit.Test


@LargeTest
class MainActivityInstrumentedTest {

    @get:Rule
    var activityScenarioRule = activityScenarioRule<MainActivity>()

    @Test
    fun addingNumbers() {

        var result: Int? = null
        activityScenarioRule.scenario.onActivity {
            result = it.addingNumbers(5,5)
        }
        Log.d("Test", "result: $result")
        assertEquals(result, 10)
    }
}