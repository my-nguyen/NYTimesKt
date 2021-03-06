package com.nguyen.nytimeskt

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.nguyen.nytimeskt.databinding.FragmentDatePickerBinding
import java.util.*

class DatePickerFragment : DialogFragment() {
    companion object {
        const val EXTRA_DAY_OBJECT = "DAY_OBJECT"

        fun newInstance(date: Date?) : DatePickerFragment {
            val fragment = DatePickerFragment()
            val bundle = Bundle()
            bundle.putSerializable(EXTRA_DAY_OBJECT, date)
            fragment.arguments = bundle
            return fragment
        }
    }

    private lateinit var binding: FragmentDatePickerBinding

    override fun onCreateDialog(bundle: Bundle?): Dialog {
        val inflater = LayoutInflater.from(activity)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_date_picker, null, false)

        var date = arguments?.getSerializable(EXTRA_DAY_OBJECT) as Date?
        if (date == null) {
            date = Date(GregorianCalendar())
        }
        binding.datePicker.init(date.year, date.month, date.day, null)

        // create and return AlertDialog
        val dialog = AlertDialog.Builder(activity!!)
        dialog.setView(binding.root)
        dialog.setTitle(R.string.date_picker_title)
        dialog.setPositiveButton(android.R.string.ok) { _, _ ->
            targetFragment?.let {
                val intent = Intent()
                val dt = Date(binding.datePicker.year, binding.datePicker.month, binding.datePicker.dayOfMonth)
                intent.putExtra(EXTRA_DAY_OBJECT, dt)
                it.onActivityResult(targetRequestCode, Activity.RESULT_OK, intent)
            }
        }
        return dialog.create()
    }
}
