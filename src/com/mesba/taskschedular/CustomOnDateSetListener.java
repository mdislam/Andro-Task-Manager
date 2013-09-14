package com.mesba.taskschedular;

import android.app.DatePickerDialog;
import android.widget.Button;
import android.widget.DatePicker;

public class CustomOnDateSetListener implements
		DatePickerDialog.OnDateSetListener {
	private Button btn;
	private CustomDate date;

	public CustomOnDateSetListener(Button v, CustomDate date) {
		this.btn = v;
		this.date = date;
	}
	@Override

	public void onDateSet(DatePicker view, int year, int month, int day) {

		date.setDay(day);
		date.setDayStr();
		// add 1, because month numbering starts from 0
		date.setMonth(month);
		date.setMonthStr();

		date.setYear(year);
		date.setYearStr();
		//Log.v("date inside the method", Integer.toString(date.getYear()));
		btn.setText(date.getDate());

	}

}
