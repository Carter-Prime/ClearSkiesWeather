package fi.carterm.clearskiesweather.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.google.android.material.textfield.TextInputLayout
import fi.carterm.clearskiesweather.R
import fi.carterm.clearskiesweather.utilities.WeatherApplication
import fi.carterm.clearskiesweather.viewmodels.HomeViewModel


class InputLocationDialogFragment: DialogFragment(), View.OnClickListener {

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var app: WeatherApplication
    lateinit var btnAccept: Button
    lateinit var btnCancel: Button
    lateinit var btnReset: Button
    lateinit var edit: TextInputLayout
    private var inputText: String? = null

    private val textWatcher = object : TextWatcher{
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            edit.error = if(count > 19){ "You have reached the maximum number of characters" }else { "" }
            inputText = s.toString().replace(Regex("\\s{2,}"), " ").trim().lowercase()
        }

        override fun afterTextChanged(s: Editable?) {
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dialog!!.window?.setBackgroundDrawableResource(R.drawable.dialog_gradient)
        val viewModel: HomeViewModel by activityViewModels()
        homeViewModel = viewModel
        return inflater.inflate(R.layout.dialog_layout, container, false)
    }

    override fun onStart() {
        super.onStart()
        val width = resources.displayMetrics.widthPixels
        val height = resources.displayMetrics.heightPixels
        dialog!!.window?.setLayout(width, height)


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        app = activity?.application as WeatherApplication
        btnAccept = view.findViewById(R.id.btn_accept)
        btnCancel = view.findViewById(R.id.btn_cancel)
        btnReset = view.findViewById(R.id.btn_reset)
        edit = view.findViewById(R.id.tf_edit_location)

        edit.editText?.addTextChangedListener(textWatcher)

        btnAccept.setOnClickListener(this)
        btnCancel.setOnClickListener(this)
        btnReset.setOnClickListener(this)

    }

    override fun onClick(v: View?) {
        if (v != null) {
            when(v.id){
                R.id.btn_accept -> {
                    inputText?.let {homeViewModel.getLocationFromName(it) }
                    homeViewModel.useCurrentLocation = false
                    inputText = ""
                    dismiss()}
                R.id.btn_cancel -> {
                    inputText = ""
                    dismiss()
                }
                R.id.btn_reset -> {
                    homeViewModel.useCurrentLocation = true
                    dismiss()
                }

            }
        }
    }



}