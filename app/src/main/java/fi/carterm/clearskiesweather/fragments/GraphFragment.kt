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
import com.anychart.AnyChartView
import com.anychart.chart.common.dataentry.DataEntry
import com.anychart.chart.common.dataentry.ValueDataEntry
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import java.time.LocalDateTime
import com.anychart.AnyChart

import com.anychart.charts.Pie





class GraphFragment : Fragment(R.layout.fragment_graph) {

    private lateinit var b: FragmentGraphBinding
    private lateinit var graphViewModel: GraphViewModel

    //graph query variables
    private val defaultStartDate = LocalDateTime.now().minusMonths(1).toString()
    private val defaultEndDate = LocalDateTime.now().toString()
    private val defaultInterval = 4
    private val intervalMap =
        mapOf("minute" to 0, "hour" to 1, "day" to 2, "month" to 3, "year" to 4)
    private val intervalMapRev =
        mapOf(0 to "minute", 1 to "hour", 2 to "day", 3 to "month", 4 to "year")

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
            if (selectedInterval < 4) {
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

        // get data
        val anyChartView = view.findViewById(R.id.graph) as AnyChartView
        val chart = AnyChart.line()

        graphViewModel.sensorLightReadings.observe(viewLifecycleOwner) {
            val lightDataArray = it
            val data: MutableList<DataEntry> = ArrayList()

            for (s in lightDataArray) {
                data.add(
                    ValueDataEntry(
                        lightDataArray[0].timestamp,
                        lightDataArray[0].sensorReading
                    )
                )

            }
            Log.d("testingRoom in graph", "Sensor Data transformed: $data")
            chart.data(data)
            anyChartView.setChart(chart)
        }


        // display in graph



    }
}
