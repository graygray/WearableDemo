package com.primax.wearabledemo2;

import java.util.Calendar;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.util.Log;
import android.widget.DatePicker;


public class DatePickerFragment extends DialogFragment implements
		DatePickerDialog.OnDateSetListener {
	
	public static int pickedYear;
	public static int pickedMonth;
	public static int pickedDay;
	public static boolean isReDrawPie;

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// Use the current date as the default date in the picker
		final Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH);
		int day = c.get(Calendar.DAY_OF_MONTH);

		// Create a new instance of DatePickerDialog and return it
		return new DatePickerDialog(getActivity(), this, year, month, day);
	}

	public void onDateSet(DatePicker view, int year, int month, int day) {
		// Do something with the date chosen by the user
		Log.e("gray", "DatePickerFragment.java:onDateSet, year:" + year + ", month:" + month + ", day:" + day);
		pickedYear = year;
		pickedMonth = month;
		pickedDay = day;
		isReDrawPie = true;
	}
}
