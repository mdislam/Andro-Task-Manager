package com.mesba.taskschedular;

import android.app.TimePickerDialog;
import android.util.Log;
import android.widget.Button;
import android.widget.TimePicker;

public class CustomOnTimeSetListener implements
		TimePickerDialog.OnTimeSetListener {
	private Button btn;
	private CustomTime dtime;

	public CustomOnTimeSetListener(Button v, CustomTime dtime) {
		this.btn = v;
		this.dtime = dtime;
	}
	@Override
	public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
		dtime.setHour(hourOfDay);
		dtime.setMinute(minute);
		Log.v("saved minutes", dtime.getTimeStr());
		Log.v("only minutes", Integer.toString(dtime.getMinute()));
		btn.setText(dtime.getTimeStr());
	}

}
