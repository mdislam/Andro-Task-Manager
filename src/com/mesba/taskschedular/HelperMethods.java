/*AUTHOR: Elena Oat
 * This class contains methods that are shared among all the classes in the package.
 * Provided functionality of these methods is re-used in several places, that's why
 * the class was created.
 * 
 * */

package com.mesba.taskschedular;

import java.util.List;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class HelperMethods {

	/*
	 * method that creates a new TimePickerDialog and returns to activity the
	 * chosen time
	 */
	public static CustomTime chooseTime(View v, CustomTime time_DT_from,
			Activity activity) {

		CustomTime time_save_from = new CustomTime(0, 0);
		new TimePickerDialog(activity, new CustomOnTimeSetListener((Button) v,
				time_save_from), time_DT_from.getHour(),
				time_DT_from.getMinute(), true).show();

		return time_save_from;
	}

	/*
	 * method that creates a new DatePickerDialog and returns to activity the
	 * chosen date
	 */
	public static CustomDate chooseDate(View v, CustomDate date_DT_from,
			Activity activity) {

		CustomDate date_save_from = new CustomDate(0, 0, 0);
		new DatePickerDialog(activity, new CustomOnDateSetListener((Button) v,
				date_save_from), date_DT_from.getYear(),
				date_DT_from.getMonth(), date_DT_from.getDay()).show();

		return date_save_from;
	}

	/*
	 * ArrayList <CustomDate> dates dates[0] = date entered from the picker for
	 * "FROM" picker, i.e. modified date dates[1] = date entered before the
	 * picker was selected, extracted from the DB dates[2] = date entered from
	 * the picker for "FROM" picker, i.e. modified date dates[3] = date entered
	 * before the picker was selected, extracted from the DB
	 */

	/* method for displaying an short toast */
	public static void displayToast(String message, Activity activity) {
		Toast toast = Toast.makeText(activity, message, Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL,
				0, 0);
		toast.show();
	}

	public static CustomTime addHour(CustomTime time) {
		CustomTime plusHourTime = new CustomTime(time.getHour() + 1,
				time.getMinute());
		return plusHourTime;
	}

	/* method for validation of DateTime periods */
	public static boolean validateDateTime(View view, List<CustomDate> dates,
			List<CustomTime> times) {

		if (dates.get(0) == null) {
			dates.set(0, dates.get(1));
		}
		if (dates.get(2) == null) {
			dates.set(2, dates.get(3));
		}

		if (times.get(0) == null) {
			times.set(0, times.get(1));
		}
		if (times.get(2) == null) {
			times.set(2, times.get(3));
		}
		if ((times.get(0).getHour() > times.get(2).getHour()
				&& dates.get(0).getYear() >= dates.get(2).getYear()
				&& dates.get(0).getMonth() >= dates.get(1).getMonth() && dates
				.get(0).getDay() >= dates.get(2).getDay())
				|| (times.get(0).getHour() == times.get(2).getHour() && times
						.get(0).getMinute() >= times.get(2).getMinute())
				&& dates.get(0).getYear() >= dates.get(2).getYear()
				&& dates.get(0).getMonth() >= dates.get(2).getMonth()
				&& dates.get(0).getDay() >= dates.get(2).getDay()) {
			return false;

		} else {
			return true;
		}

	}

}
