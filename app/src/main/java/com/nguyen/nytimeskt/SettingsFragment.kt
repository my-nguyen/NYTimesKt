package com.nguyen.nytimeskt

import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.CompoundButton
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.nguyen.nytimeskt.DatePickerFragment.Companion.EXTRA_DAY_OBJECT
import com.nguyen.nytimeskt.databinding.FragmentSettingsBinding

class SettingsFragment() : DialogFragment() {
    companion object {
        val EXTRA_SETTINGS_OBJECT = "SETTINGS_OBJECT"
        val TAG = "SettingsFragment"
        val REQUEST_CODE_DATE = 31

        fun newInstance(settings: Settings) : SettingsFragment {
            val fragment = SettingsFragment()
            val args = Bundle()
            args.putSerializable(EXTRA_SETTINGS_OBJECT, settings)
            fragment.setArguments(args)
            return fragment
        }
    }

    lateinit var settings: Settings
    lateinit var listener: DialogListener
    lateinit var binding: FragmentSettingsBinding

    // interface used to pass a Settings object from this SettingsFragment to MainActivity
    interface DialogListener {
        fun onFinish(settings: Settings)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_settings, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        settings = arguments?.getSerializable(EXTRA_SETTINGS_OBJECT) as Settings

        // set up "Begin Date"
        settings.beginDate?.let {
            binding.beginDateText.setText(settings.getBeginDate())
        }
        binding.beginDateText.requestFocus()

        // set up DatePicker dialog
        binding.beginDateText.setOnClickListener(View.OnClickListener() {
            val datePicker = DatePickerFragment.newInstance(settings.beginDate)
            datePicker.setTargetFragment(this@SettingsFragment, REQUEST_CODE_DATE)
            datePicker.show(activity!!.supportFragmentManager, "DatePickerFragment")
        })

        // set up "Sort Order"
        val adapter = ArrayAdapter.createFromResource(activity!!.applicationContext, R.array.sort_order_array, R.layout.item_spinner)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.sortOrderSpinner.adapter = adapter
        val position = adapter.getPosition(settings.sortOrder)
        binding.sortOrderSpinner.setSelection(position)
        binding.sortOrderSpinner.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                settings.sortOrder = if (position == 0) null else parent?.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        })

        // set up "News Desk Values"
        binding.artsCheckBox.isChecked = settings.arts
        binding.artsCheckBox.setOnCheckedChangeListener { _, isChecked ->
            settings.arts = isChecked
        }
        binding.fashionStyleCheckBox.isChecked = settings.fashionStyle
        binding.fashionStyleCheckBox.setOnCheckedChangeListener { _, isChecked ->
            settings.fashionStyle = isChecked
        }
        binding.sportsCheckBox.isChecked = settings.sports
        binding.sportsCheckBox.setOnCheckedChangeListener { _, isChecked ->
            settings.sports = isChecked
        }

        // set up Save button
        binding.saveButton.setOnClickListener {
            listener.onFinish(settings)
            dismiss()
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = context as DialogListener
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_DATE) {
            val date = data?.getSerializableExtra(EXTRA_DAY_OBJECT) as Date
            binding.beginDateText.setText(date.toString())
            settings.beginDate = date
        }
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
    }
}
