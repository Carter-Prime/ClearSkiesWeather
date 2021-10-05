package fi.carterm.clearskiesweather.fragments

import android.graphics.Color
import android.os.Bundle
import android.transition.AutoTransition
import android.transition.TransitionManager
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import fi.carterm.clearskiesweather.R
import fi.carterm.clearskiesweather.databinding.FragmentGraphBinding
import fi.carterm.clearskiesweather.viewmodels.GraphViewModel
import com.jjoe64.graphview.series.LineGraphSeries
import com.jjoe64.graphview.GraphView
import com.jjoe64.graphview.series.DataPoint
import com.google.android.material.datepicker.MaterialDatePicker
import android.text.Editable
import androidx.fragment.app.FragmentActivity
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import java.time.LocalDateTime


class GraphFragment : Fragment(R.layout.fragment_graph) {

    private lateinit var b: FragmentGraphBinding
    private lateinit var graphViewModel: GraphViewModel

    //graph query variables
    private val defaultStartDate = LocalDateTime.now().minusMonths(1).toString()
    private val defaultEndDate = LocalDateTime.now().toString()
    private val defaultInterval = 4
    private val intervalMap = mapOf("minute" to 0, "hour" to 1, "day" to 2, "month" to 3, "year" to 4)
    private val intervalMapRev = mapOf(0 to "minute", 1 to "hour",2 to "day",3 to "month",4 to "year")

    private var selectedTemperature = false
    private var selectedPressure = false
    private var selectedLight = false
    private var selectedHumidity = false
    private var selectedAbsHumidity = false
    private var selectedDewPoint = false

    private var selectedInterval = defaultInterval
    private var selectedStartDate = defaultStartDate
    private var selectedEndDate = defaultEndDate


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("WeatherTest", "Fragment")
        b = FragmentGraphBinding.bind(view)
        val viewModel: GraphViewModel by viewModels()
        graphViewModel = viewModel
        val fragmentManager = (activity as FragmentActivity).supportFragmentManager

        /*
        graphViewModel.weatherData.observe(viewLifecycleOwner) {
            Log.d("dbApp", "Weather Data: $it")
        }
*/

        //startDatePicker
        val startDatePicker =
            MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select Start Date")
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .setCalendarConstraints(
                    CalendarConstraints.Builder()
                        .setValidator(DateValidatorPointForward.now()).build()
                )
                .build()
        b.etEventStartDate.setOnClickListener {
            startDatePicker.show(fragmentManager, "tag")
        }
        startDatePicker.addOnPositiveButtonClickListener {
            val date = startDatePicker.headerText
            selectedStartDate = date
            b.etEventStartDate.text = Editable.Factory.getInstance().newEditable(date)
        }

        //endDatePicker
        val endDatePicker =
            MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select End Date")
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .setCalendarConstraints(
                    CalendarConstraints.Builder()
                        .setValidator(DateValidatorPointForward.now()).build()
                )
                .build()
        b.etEventEndDate.setOnClickListener {
            endDatePicker.show(fragmentManager, "tag")
        }
        endDatePicker.addOnPositiveButtonClickListener {
            val date = endDatePicker.headerText
            selectedEndDate = date
            b.etEventEndDate.text = Editable.Factory.getInstance().newEditable(date)
        }

        // read time interval button value
        b.intervalButton.setOnClickListener {
            if (selectedInterval<4) {
                selectedInterval += 1
                b.intervalButton.setText(intervalMapRev.getValue(selectedInterval))
            } else {
                selectedInterval = 0
                b.intervalButton.setText(intervalMapRev.getValue(selectedInterval))
            }
        }
        // SELECT SENSOR BUTTONS
        b.arrowButton.setOnClickListener {
            // If the CardView is already expanded, set its visibility to gone and change the expand less icon to expand more.
            if (b.hiddenView.visibility == View.VISIBLE) {

                // The transition of the hiddenView is carried out by the TransitionManager class.
                // Here we use an object of the AutoTransition Class to create a default transition.
                TransitionManager.beginDelayedTransition(
                    b.cvCardView,
                    AutoTransition()
                )
                b.hiddenView.visibility = View.GONE
                b.arrowButton.setImageResource(R.drawable.ic_baseline_expand_more_24)
            } else {
                TransitionManager.beginDelayedTransition(
                    b.cvCardView,
                    AutoTransition()
                )
                b.hiddenView.visibility = View.VISIBLE
                b.arrowButton.setImageResource(R.drawable.ic_baseline_expand_less_24)

                // read light button value
                b.lightBtn.setOnClickListener {
                    if (!selectedLight) {
                        selectedLight = true
                        b.lightBtn.setBackgroundColor(Color.parseColor("#FFBB86FC"))
                    } else {
                        selectedLight = false
                        b.lightBtn.setBackgroundColor(Color.parseColor("#34000000"))
                    }

                }
                // read temp button value
                b.temperatureBtn.setOnClickListener {
                    if (!selectedTemperature) {
                        selectedTemperature = true
                        b.temperatureBtn.setBackgroundColor(Color.parseColor("#FFBB86FC"))
                    } else {
                        selectedTemperature = false
                        b.temperatureBtn.setBackgroundColor(Color.parseColor("#34000000"))
                    }
                }
                // read humidity button value
                b.humidityBtn.setOnClickListener {
                    if (!selectedHumidity) {
                        selectedHumidity = true
                        b.humidityBtn.setBackgroundColor(Color.parseColor("#FFBB86FC"))
                    } else {
                        selectedHumidity = false
                        b.humidityBtn.setBackgroundColor(Color.parseColor("#34000000"))
                    }
                }
// read absolute humidity button value
                b.absHumidityBtn.setOnClickListener {
                    if (!selectedAbsHumidity) {
                        selectedAbsHumidity = true
                        b.absHumidityBtn.setBackgroundColor(Color.parseColor("#FFBB86FC"))
                    } else {
                        selectedAbsHumidity = false
                        b.absHumidityBtn.setBackgroundColor(Color.parseColor("#34000000"))
                    }
                }
// read pressure button value
                b.pressureBtn.setOnClickListener {
                    if (!selectedPressure) {
                        selectedPressure = true
                        b.pressureBtn.setBackgroundColor(Color.parseColor("#FFBB86FC"))
                    } else {
                        selectedPressure = false
                        b.pressureBtn.setBackgroundColor(Color.parseColor("#34000000"))
                    }
                }
                // read dew point button value
                b.dewPointBtn.setOnClickListener {
                    if (!selectedDewPoint) {
                        selectedDewPoint = true
                        b.dewPointBtn.setBackgroundColor(Color.parseColor("#FFBB86FC"))
                    } else {
                        selectedDewPoint = false
                        b.dewPointBtn.setBackgroundColor(Color.parseColor("#34000000"))
                    }
                }
             }
        }

        val graph = view.findViewById(R.id.graph) as GraphView
        val series: LineGraphSeries<DataPoint> = LineGraphSeries(
            arrayOf(
                DataPoint(0.0, 1.0),
                DataPoint(1.0, 5.0),
                DataPoint(2.0, 3.0),
                DataPoint(3.0, 2.0),
                DataPoint(4.0, 6.0)
            )
        )
        graph.addSeries(series)

    }


    private fun getMonthFormat(month: Int): String {
        if (month == 1) return "JAN"
        if (month == 2) return "FEB"
        if (month == 3) return "MAR"
        if (month == 4) return "APR"
        if (month == 5) return "MAY"
        if (month == 6) return "JUN"
        if (month == 7) return "JUL"
        if (month == 8) return "AUG"
        if (month == 9) return "SEP"
        if (month == 10) return "OCT"
        if (month == 11) return "NOV"
        return if (month == 12) "DEC" else "JAN"

        //default should never happen
    }
}
