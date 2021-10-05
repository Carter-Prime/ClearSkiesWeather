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

    private lateinit var binding: FragmentGraphBinding
    private lateinit var graphViewModel: GraphViewModel

    //graph query variables
    private val defaultStartDate = LocalDateTime.now().minusDays(31).toString()
    private val defaultEndDate = LocalDateTime.now().toString()
    private val defaultInterval = 4
    private val intervalMap = mapOf("minute" to 0, "hour" to 1, "day" to 2, "month" to 3, "year" to 4)

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
        binding = FragmentGraphBinding.bind(view)
        val viewModel: GraphViewModel by viewModels()
        graphViewModel = viewModel
        val fragmentManager = (activity as FragmentActivity).supportFragmentManager


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
        binding.etEventStartDate.setOnClickListener {
            startDatePicker.show(fragmentManager, "tag")
        }
        startDatePicker.addOnPositiveButtonClickListener {
            val date = startDatePicker.headerText
            selectedStartDate = date
            binding.etEventStartDate.text = Editable.Factory.getInstance().newEditable(date)
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
        binding.etEventEndDate.setOnClickListener {
            endDatePicker.show(fragmentManager, "tag")
        }
        endDatePicker.addOnPositiveButtonClickListener {
            val date = endDatePicker.headerText
            selectedEndDate = date
            binding.etEventEndDate.text = Editable.Factory.getInstance().newEditable(date)
        }
/*
        graphViewModel.weatherData.observe(viewLifecycleOwner) {
            Log.d("dbApp", "Weather Data: $it")
        }
*/
        // SELECT SENSOR BUTTONS
        binding.arrowButton.setOnClickListener {
            // If the CardView is already expanded, set its visibility to gone and change the expand less icon to expand more.
            if (binding.hiddenView.visibility == View.VISIBLE) {

                // The transition of the hiddenView is carried out by the TransitionManager class.
                // Here we use an object of the AutoTransition Class to create a default transition.
                TransitionManager.beginDelayedTransition(
                    binding.cvCardView,
                    AutoTransition()
                )
                binding.hiddenView.visibility = View.GONE
                binding.arrowButton.setImageResource(R.drawable.ic_baseline_expand_more_24)
            } else {
                TransitionManager.beginDelayedTransition(
                    binding.cvCardView,
                    AutoTransition()
                )
                binding.hiddenView.visibility = View.VISIBLE
                binding.arrowButton.setImageResource(R.drawable.ic_baseline_expand_less_24)

                // read light button value
                binding.lightBtn.setOnClickListener {
                    if (!selectedLight) {
                        selectedLight = true
                        binding.lightBtn.setBackgroundColor(Color.parseColor("#FFBB86FC"))
                    } else {
                        selectedLight = false
                        binding.lightBtn.setBackgroundColor(Color.parseColor("#34000000"))
                    }

                }
                // read temp button value
                binding.temperatureBtn.setOnClickListener {
                    if (!selectedTemperature) {
                        selectedTemperature = true
                        binding.temperatureBtn.setBackgroundColor(Color.parseColor("#FFBB86FC"))
                    } else {
                        selectedTemperature = false
                        binding.temperatureBtn.setBackgroundColor(Color.parseColor("#34000000"))
                    }
                }
                // read humidity button value
                binding.humidityBtn.setOnClickListener {
                    if (!selectedHumidity) {
                        selectedHumidity = true
                        binding.humidityBtn.setBackgroundColor(Color.parseColor("#FFBB86FC"))
                    } else {
                        selectedHumidity = false
                        binding.humidityBtn.setBackgroundColor(Color.parseColor("#34000000"))
                    }
                }
// read absolute humidity button value
                binding.absHumidityBtn.setOnClickListener {
                    if (!selectedAbsHumidity) {
                        selectedAbsHumidity = true
                        binding.absHumidityBtn.setBackgroundColor(Color.parseColor("#FFBB86FC"))
                    } else {
                        selectedAbsHumidity = false
                        binding.absHumidityBtn.setBackgroundColor(Color.parseColor("#34000000"))
                    }
                }
// read pressure button value
                binding.pressureBtn.setOnClickListener {
                    if (!selectedPressure) {
                        selectedPressure = true
                        binding.pressureBtn.setBackgroundColor(Color.parseColor("#FFBB86FC"))
                    } else {
                        selectedPressure = false
                        binding.pressureBtn.setBackgroundColor(Color.parseColor("#34000000"))
                    }
                }
                // read dew point button value
                binding.dewPointBtn.setOnClickListener {
                    if (!selectedDewPoint) {
                        selectedDewPoint = true
                        binding.dewPointBtn.setBackgroundColor(Color.parseColor("#FFBB86FC"))
                    } else {
                        selectedDewPoint = false
                        binding.dewPointBtn.setBackgroundColor(Color.parseColor("#34000000"))
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
