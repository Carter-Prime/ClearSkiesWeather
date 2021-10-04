package fi.carterm.clearskiesweather.fragments

import android.graphics.Color.DKGRAY
import android.os.Bundle
import android.transition.AutoTransition
import android.transition.TransitionManager
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.cardview.widget.CardView
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
import java.util.*


class GraphFragment : Fragment(R.layout.fragment_graph) {

    private lateinit var binding: FragmentGraphBinding
    private lateinit var graphViewModel: GraphViewModel

    //graph query variables
    val defaultStartDate = LocalDateTime.now().minusDays(31).toString()
    val defaultEndDate = LocalDateTime.now().toString()
    val defaultInterval = 4
    val intervalMap = mapOf("minute" to 0, "hour" to 1, "day" to 2, "month" to 3, "year" to 4)

    var selectedTemperature = false
    var selectedPressure = false
    var selectedLight = false
    var selectedHumidity = false
    var selectedAbsHumidity = false
    var selectedDewPoint = false

    var selectedInterval = defaultInterval
    var selectedStartDate = defaultStartDate
    var selectedEndDate = defaultEndDate


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
        binding.arrowButton.setOnClickListener(View.OnClickListener {
            // If the CardView is already expanded, set its visibility to gone and change the expand less icon to expand more.
            if (binding.hiddenView.getVisibility() == View.VISIBLE) {

                // The transition of the hiddenView is carried out by the TransitionManager class.
                // Here we use an object of the AutoTransition Class to create a default transition.
                TransitionManager.beginDelayedTransition(
                    binding.cvCardView,
                    AutoTransition()
                )
                binding.hiddenView.setVisibility(View.GONE)
                binding.arrowButton.setImageResource(R.drawable.ic_baseline_expand_more_24)
            } else {
                TransitionManager.beginDelayedTransition(
                    binding.cvCardView,
                    AutoTransition()
                )
                binding.hiddenView.setVisibility(View.VISIBLE)
                binding.arrowButton.setImageResource(R.drawable.ic_baseline_expand_less_24)
                // read light button value
                binding.lightBtn.setOnClickListener {
                    if (selectedLight == false) {
                        selectedLight = true
                        binding.lightBtn.setBackgroundColor(R.color.purple_200)
                    } else {
                        selectedLight = false
                        binding.lightBtn.setBackgroundColor(R.color.black_transparent)
                    }

                }
                // read temp button value
                binding.temperatureBtn.setOnClickListener {
                    if (selectedLight == false) {
                        selectedLight = true
                        binding.temperatureBtn.setBackgroundColor(R.color.purple_200)
                    } else {
                        selectedTemperature = false
                        binding.temperatureBtn.setBackgroundColor(R.color.black_transparent)
                    }
                }
                // read humidity button value
                binding.humidityBtn.setOnClickListener {
                    if (selectedHumidity == false) {
                        selectedHumidity = true
                        binding.humidityBtn.setBackgroundColor(R.color.purple_200)
                    } else {
                        selectedHumidity = false
                        binding.humidityBtn.setBackgroundColor(R.color.black_transparent)
                    }
                }
// read absolute humidity button value
                binding.absHumidityBtn.setOnClickListener {
                    if (selectedAbsHumidity == false) {
                        selectedAbsHumidity = true
                        binding.absHumidityBtn.setBackgroundColor(R.color.purple_200)
                    } else {
                        selectedAbsHumidity = false
                        binding.absHumidityBtn.setBackgroundColor(R.color.black_transparent)
                    }
                }
// read pressure button value
                binding.pressureBtn.setOnClickListener {
                    if (selectedPressure == false) {
                        selectedPressure = true
                        binding.pressureBtn.setBackgroundColor(R.color.purple_200)
                    } else {
                        selectedPressure = false
                        binding.pressureBtn.setBackgroundColor(R.color.black_transparent)
                    }
                }
                // read dew point button value
                binding.dewPointBtn.setOnClickListener {
                    if (selectedDewPoint == false) {
                        selectedDewPoint = true
                        binding.dewPointBtn.setBackgroundColor(R.color.purple_200)
                    } else {
                        selectedDewPoint = false
                        binding.dewPointBtn.setBackgroundColor(R.color.black_transparent)
                    }
                }


            }
        })

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
