package fi.carterm.clearskiesweather.fragments

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


class GraphFragment : Fragment(R.layout.fragment_graph) {

    private lateinit var binding: FragmentGraphBinding
    private lateinit var graphViewModel: GraphViewModel

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
                .setTitleText("Select Event Start Date")
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .setCalendarConstraints(CalendarConstraints.Builder()
                    .setValidator(DateValidatorPointForward.now()).build())
                .build()
        binding.etEventStartDate.setOnClickListener {
            startDatePicker.show(fragmentManager, "tag")
        }
        startDatePicker.addOnPositiveButtonClickListener {
            val date = startDatePicker.headerText
            binding.etEventStartDate.text = Editable.Factory.getInstance().newEditable(date)
        }

        //endDatePicker
        val endDatePicker =
            MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select Event End Date")
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .setCalendarConstraints(CalendarConstraints.Builder()
                    .setValidator(DateValidatorPointForward.now()).build())
                .build()
        binding.etEventEndDate.setOnClickListener {
            endDatePicker.show(fragmentManager, "tag")
        }
        endDatePicker.addOnPositiveButtonClickListener {
            val date = endDatePicker.headerText
            binding.etEventEndDate.text = Editable.Factory.getInstance().newEditable(date)
        }

        graphViewModel.weatherData.observe(viewLifecycleOwner) {
            Log.d("dbApp", "Weather Data: $it")
        }

        // SELECT SENSOR BUTTONS
        val cardView = view.findViewById<CardView>(R.id.base_cardview)
        val arrow = view.findViewById<ImageButton>(R.id.arrow_button)
        val hiddenView = view.findViewById<LinearLayout>(R.id.hidden_view)

        arrow.setOnClickListener(View.OnClickListener {
            // If the CardView is already expanded, set its visibility
            //  to gone and change the expand less icon to expand more.
            if (hiddenView.getVisibility() == View.VISIBLE) {

                // The transition of the hiddenView is carried out
                //  by the TransitionManager class.
                // Here we use an object of the AutoTransition
                // Class to create a default transition.
                TransitionManager.beginDelayedTransition(
                    cardView,
                    AutoTransition()
                )
                hiddenView.setVisibility(View.GONE)
                arrow.setImageResource(R.drawable.ic_baseline_expand_more_24)
            } else {
                TransitionManager.beginDelayedTransition(
                    cardView,
                    AutoTransition()
                )
                hiddenView.setVisibility(View.VISIBLE)
                arrow.setImageResource(R.drawable.ic_baseline_expand_less_24)
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




