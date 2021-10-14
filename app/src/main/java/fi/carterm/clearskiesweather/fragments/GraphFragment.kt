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
import com.google.android.material.datepicker.MaterialDatePicker
import android.text.Editable
import androidx.fragment.app.FragmentActivity
import com.anychart.chart.common.dataentry.DataEntry
import com.anychart.chart.common.dataentry.ValueDataEntry
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import java.time.LocalDateTime
import com.anychart.AnyChart
import com.anychart.enums.TooltipPositionMode
import com.anychart.graphics.vector.Stroke
import java.util.*
import kotlin.collections.ArrayList
import com.anychart.charts.Cartesian
import com.anychart.core.cartesian.series.Line
import com.google.android.material.datepicker.DateValidatorPointBackward


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

    private val selectedSensor =
        mutableMapOf(

            "selectedTemperature" to false,
            "selectedPressure" to false,
            "selectedLight" to false,
            "selectedHumidity" to false,
            "selectedAbsHumidity" to false,
            "selectedDewPoint" to false,
            "selectedUVRating" to false,
            "selectedWind" to false,
            "selectedVisibility" to false,
            "selectedSunrise" to false,
            "selectedSunset" to false,
        )
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

        // Receive sensor type from home fragment
        when (arguments?.getString("sensorType").orEmpty()) {
            getString(R.string.sensor_temperature) -> {
                selectedSensor["selectedTemperature"] = true
                observeSensorTemp()
            }
            getString(R.string.sensor_humidity) -> {
                selectedSensor["selectedHumidity"] = true
                observeSensorHumidity()
            }
            getString(R.string.sensor_light) -> {
                selectedSensor["selectedLight"] = true
                observeSensorLight()
            }
            getString(R.string.sensor_pressure) -> {
                selectedSensor["selectedPressure"] = true
                observeSensorPressure()
            }
            getString(R.string.sensor_absolute_humidity) -> selectedSensor["selectedAbsHumidity"] =
                true
            getString(R.string.sensor_dew_point) -> selectedSensor["selectedDewPoint"] = true
            getString(R.string.sensor_uv_rating) -> selectedSensor["selectedUVRating"] = true
            getString(R.string.sensor_wind) -> selectedSensor["selectedWind"] = true
            getString(R.string.sensor_visibility) -> selectedSensor["selectedVisibility"] = true
            getString(R.string.sensor_sunrise) -> selectedSensor["selectedSunrise"] = true
            getString(R.string.sensor_sunset) -> selectedSensor["selectedSunset"] = true
            else -> {
                //TODO: Toast
            }
        }


        //startDatePicker
        val startDatePicker =
            MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select Start Date")
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .setCalendarConstraints(
                    CalendarConstraints.Builder()
                        .setValidator(DateValidatorPointBackward.now()).build()
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
                        .setValidator(DateValidatorPointBackward.now()).build()
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
                b.intervalButton.text = intervalMapRev.getValue(selectedInterval)
            } else {
                selectedInterval = 0
                b.intervalButton.text = intervalMapRev.getValue(selectedInterval)
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

                // read button value
                b.lightBtn.setOnClickListener { toggleSensors("selectedLight")

                    observeSensorLight()}
                b.temperatureBtn.setOnClickListener { toggleSensors("selectedTemperature")
                    observeSensorTemp()}
                b.humidityBtn.setOnClickListener { toggleSensors("selectedHumidity")
                    observeSensorHumidity()}
                b.absHumidityBtn.setOnClickListener { toggleSensors("selectedAbsHumidity") }
                b.pressureBtn.setOnClickListener { toggleSensors("selectedPressure")
                    observeSensorPressure()}
                b.dewPointBtn.setOnClickListener {toggleSensors("selectedDewPoint") }
            }
        }
    }

    private fun toggleSensors(s: String) {
        if (!selectedSensor[s]!!) {
            selectedSensor[s] = true
            setOtherToFalse(s)
            observeSensorLight()
        } else {
            selectedSensor[s] = false

        }
    }

    // deselects all previously selected sensors
    private fun setOtherToFalse(s: String) {
        for ((k, v) in selectedSensor) {
            if (k != s) {
                selectedSensor[s] = false
            }
        }
    }

    private fun observeSensorTemp() {
        val data: MutableList<DataEntry> = ArrayList()
        val chart = AnyChart.line()
        setChart(chart)

        graphViewModel.sensorTemperatureReadings.observe(viewLifecycleOwner) {
            val dataArray = it

            for (i in dataArray.indices) {
                data.add(
                    ValueDataEntry(
                        Date(dataArray[i].timestamp).toString(),
                        dataArray[i].sensorReading
                    )
                )
            }
            val line: Line? = chart?.line(data)  // re-creates
            line?.stroke("blue")
            displayInChart(chart, "°C", "time")
        }

    }

    private fun observeSensorLight() {
        val data: MutableList<DataEntry> = ArrayList()
        val chart = AnyChart.line()
        setChart(chart)

        graphViewModel.sensorLightReadings.observe(viewLifecycleOwner) {
            val dataArray = it

            for (i in dataArray.indices) {
                data.add(
                    ValueDataEntry(
                        Date(dataArray[i].timestamp).toString(),
                        dataArray[i].sensorReading
                    )
                )
            }
            val line: Line? = chart?.line(data)  // re-creates
            line?.stroke("blue")
            displayInChart(chart, "lux", "time")
        }
    }

    private fun observeSensorPressure() {

        val data: MutableList<DataEntry> = ArrayList()
        val chart = AnyChart.line()
        setChart(chart)

        graphViewModel.sensorPressureReadings.observe(viewLifecycleOwner) {
            val dataArray = it

            for (i in dataArray.indices) {
                data.add(
                    ValueDataEntry(
                        Date(dataArray[i].timestamp).toString(),
                        dataArray[i].sensorReading
                    )
                )
            }
            val line: Line? = chart?.line(data)  // re-creates
            line?.stroke("blue")
            displayInChart(chart, "hPa", "time")
        }
    }


    private fun observeSensorHumidity() {
        val data: MutableList<DataEntry> = ArrayList()
        val chart = AnyChart.line()
        setChart(chart)

        graphViewModel.sensorHumidityReadings.observe(viewLifecycleOwner) {
            val dataArray = it

            for (i in dataArray.indices) {
                data.add(
                    ValueDataEntry(
                        Date(dataArray[i].timestamp).toString(),
                        dataArray[i].sensorReading
                    )
                )
            }
            val line: Line? = chart?.line(data)  // re-creates
            line?.stroke("blue")
            displayInChart(chart, "%", "time")
        }
    }

    private fun setChart(chart: Cartesian?) {
        b.graph.setChart(chart)
        if (chart != null) {
            chart.animation(true)
            chart.padding(10.0, 20.0, 5.0, 20.0)
            chart.crosshair().enabled(true)
            chart.crosshair()
                .yLabel(true)
                .yStroke(null as Stroke?, null, null, null as String?, null as String?)
            chart.tooltip().positionMode(TooltipPositionMode.POINT)
            chart.title("Sensor reading")
        }
    }

    private fun displayInChart(chart: Cartesian?, s1: String, s2: String) {
        if (chart != null) {
            chart.yAxis(0).title(s1)
            chart.xAxis(0).title(s2)
        }
    }
}
