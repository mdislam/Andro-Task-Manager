package com.mesba.taskschedular;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.ActionMode;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.mesba.dynamicui.R;

@SuppressLint({ "NewApi", "SimpleDateFormat" })
public class DayViewFragment extends Fragment {
	private View dayView;
	public int pos;
	//private ActionMode mActionMode;
	private ActionMode.Callback mActionModeCallback;
	private DatabaseAdapter dbAdapter;
	private TextView currentDate;

	private String currentDateTimeString;
	private String currentDate_YYYY_mm_dd;

	private int selectedModeFlag = 0;

	public SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	public Calendar today = Calendar.getInstance();

	private List<Slot> hours;
	private List<Task> t_array;

	private ListView listView;
	private TaskListAdapter adapter;

	private String receivedDateFromMonth;

	public final static String ID = "com.taskmanager.ID";
	public final static String DATE = "com.taskmanager.DATE";
	public final static String HOUR = "com.taskmanager.HOUR";
	public final static String MINUTE = "com.taskmanager.MINUTE";
	public final static String NAME = "com.taskmanager.NAME";
	public final static String DESCRIPTION = "com.taskmanager.DESCRIPTION";
	private String day_month_year;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		dayView = inflater.inflate(R.layout.layout_day_view, container, false);

		dbAdapter = new DatabaseAdapter(getActivity());

		Bundle bundle = this.getArguments();
		currentDate = (TextView) dayView.findViewById(R.id.dvu_header);
		if (bundle != null) {
			selectedModeFlag = 1;
			day_month_year = bundle.getString("selectedDate");

			System.out.print("selectedDate" + day_month_year);
			showPassingtDate(day_month_year);
			formatReceivedStringFromMonthview(day_month_year);
		} else {

			showCurrentDate();
		}
		dbAdapter.Open();
		showTasks();
		dbAdapter.Close();

		return dayView;
	}

	private void formatReceivedStringFromMonthview(String day_month_year) {
		// TODO Auto-generated method stub

		try {
			Date d = new SimpleDateFormat("MMM", Locale.ENGLISH)
					.parse(day_month_year.split("-")[1]);
			Calendar cal = Calendar.getInstance();
			cal.setTime(d);
			int month = cal.get(Calendar.MONTH);

			String yearStr = day_month_year.split("-")[2];
			String dateStr = day_month_year.split("-")[0];
			String monthStr = "" + (month + 1);

			if (dateStr.length() == 1)
				dateStr = "0" + dateStr;
			if (monthStr.length() == 1)
				monthStr = "0" + monthStr;

			receivedDateFromMonth = yearStr + "-" + monthStr + "-" + dateStr;

		} catch (ParseException e) {
			e.printStackTrace();
		}

	}

	private void showPassingtDate(String p) {
		// TODO Auto-generated method stub
		System.out.println(currentDate_YYYY_mm_dd);
		currentDate.setText(p);

	}

	/**
	 * This function is responsible for setting the current date in the day view
	 * header
	 */
	public void showCurrentDate() {
		currentDateTimeString = DateFormat.getDateInstance(DateFormat.LONG)
				.format(new Date());
		currentDate_YYYY_mm_dd = new String(dateFormat.format(today.getTime()));
		System.out.println(currentDate_YYYY_mm_dd);
		currentDate.setText(currentDateTimeString);
	}

	public void showTasks() {
		t_array = new ArrayList<Task>();
		Cursor curs;
		if (selectedModeFlag == 1) {
			curs = dbAdapter.getEventByDate(receivedDateFromMonth);
		} else
			curs = dbAdapter.getEventByDate(dateFormat.format(today.getTime()));

		if (curs.moveToFirst()) {
			do {

				// should the substring parameters be hard-coded?
				// name,description,eventStartDayTime,eventEndDayTime
				int id = curs.getInt(curs.getColumnIndex("id"));
				// Log.v("id ", Integer.toString(id));
				String name = curs.getString(curs.getColumnIndex("name"));
				String description = curs.getString(curs
						.getColumnIndex("description"));
				// Log.v("description", description);
				String eventStartDayTime = curs.getString(curs
						.getColumnIndex("eventStartDayTime"));
				// Log.v("event start", eventStartDayTime);
				String eventEndDayTime = curs.getString(curs
						.getColumnIndex("eventEndDayTime"));
				// Log.v("event end", eventEndDayTime);
				t_array.add(new Task(id, name, description, eventStartDayTime,
						eventEndDayTime));

			} while (curs.moveToNext());
		}

		hours = new ArrayList<Slot>();
		for (int i = 0; i < 24; i++) {
			Slot slot;
			slot = new Slot(-1, i, "");
			hours.add(slot);
		}
		// 2013-04-15 06:00
		// check if there are any events in the database
		int size_events = t_array.size();
		for (int i = 0; i < size_events; i++) {
			// Log.v("hour in events",
			// t_array.get(i).eventStartDayTime.substring(11, 13));

			for (int j = 0; j < 24; j++) {
				try {
					// Log.v("hour in slot ", hours.get(j).hourStr());
					if ((t_array.get(i).eventStartDayTime.substring(11, 13))
							.equals(hours.get(j).hourStr())) {
						hours.get(j).setName(
								t_array.get(i).name);
								//+ "-"
									//	+ t_array.get(i).description);
						hours.get(j).setId(t_array.get(i).id);
					}
				} catch (ArrayIndexOutOfBoundsException e) {
					Log.v("MainActivity", "Wrong format for datetime");
				}
			}

		}
		adapter = new TaskListAdapter(getActivity(), R.layout.custom_simple,
				hours);

		listView = (ListView) dayView.findViewById(R.id.hour_slots);
		listView.setAdapter(adapter);

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				pos = position;
				//mActionMode = 
				getActivity().startActionMode(mActionModeCallback);
			
			}

		});

		mActionModeCallback = new ActionMode.Callback() {

			// Called when the action mode is created; startActionMode() was
			// called
			@Override
			public boolean onCreateActionMode(ActionMode mode, Menu menu) {
				// Inflate a menu resource providing context menu items
				MenuInflater inflater = mode.getMenuInflater();
				inflater.inflate(R.menu.menu_dayview, menu);
				return true;
			}

			// Called each time the action mode is shown. Always called after
			// onCreateActionMode, but
			// may be called multiple times if the mode is invalidated.
			@Override
			public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
				return false; // Return false if nothing is done
			}

			// Called when the user selects a contextual menu item
			@Override
			public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
				switch (item.getItemId()) {
				case R.id.edit:
					// shareCurrentItem();

					if (hours.get(pos).id == -1) {
						String date = "";
						Intent intent = new Intent(getActivity(),
								AddNewTaskActivity.class);
						if (selectedModeFlag == 0)
							date = dateFormat.format(today.getTime());
						else
							date = receivedDateFromMonth;
						int time = hours.get(pos).time;
						intent.putExtra(DATE, date);
						intent.putExtra(HOUR, time);
						intent.putExtra("tag", "add");
						startActivity(intent);

					} else if (hours.get(pos).getId() != -1) {
						// Send only ID, all the others will be extracted from
						// the
						// DB
						// TODO need to fix this part
						Intent intent = new Intent(getActivity(),
								AddNewTaskActivity.class);
						intent.putExtra("tag", "edit");
						intent.putExtra(ID, hours.get(pos).getId());
						startActivity(intent);
					}

					// Log.v("on Long Click", "picked");
					mode.finish(); // Action picked, so close the CAB
					return true;
				case R.id.delete:
					if (hours.get(pos).id == -1) {
						Log.v("the task doesnt exist", "id = -1");
						HelperMethods
								.displayToast(
										"You cannot delete an event that doesn't exist",
										getActivity());

					} else if (hours.get(pos).getId() != -1) {
						Intent intent = new Intent(getActivity(), DeleteTaskActivity.class);
						intent.putExtra("tag", "delete");
						intent.putExtra(ID, hours.get(pos).getId());
						startActivity(intent);

//						mode.finish();
					}

				default:
					return false;
				}
			}

			@Override
			public void onDestroyActionMode(ActionMode arg0) {
				// TODO Auto-generated method stub

			}
		};
		// listView.setOnItemLongClickListener(new OnItemLongClickListener() {
		//
		// public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
		// int pos, long id) {
		// //Log.v("long clicked", "pos" + " " + pos);
		// position = pos;
		// mActionMode = getActivity().startActionMode(mActionModeCallback);
		// return true;
		// }
		// });

		curs.close();
	}

	public void show() {

		dbAdapter.Open();
		Cursor c = dbAdapter.getAllConfig();

		String message = "";

		if (c.moveToFirst()) {
			do {
				// Call displayItem method in below
				// System.out.println("Property =" + c.getString(0)+
				// "\nValuetype =" + c.getString(1)+ "\nintegerValue =" +
				// c.getString(3));
				message = "Property: " + c.getString(0) + "\n" + "ValueType: "
						+ c.getString(1) + "\n" + "Textvalue : "
						+ c.getString(2) + "\n" + "IntegerValue : "
						+ c.getString(3);
				displayToast(message);
			} while (c.moveToNext());

		} else {
			displayToast("No item found");
		}
		c.close();
		dbAdapter.Close();
	}

	public void displayToast(String message) {
		Toast toast = Toast
				.makeText(getActivity(), message, Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL,
				0, 0);
		toast.show();
	}

}

// TODO LIST
/*
 * BUG when going to a day from month view and then selecting day view, it says
 * you are already in the day view There should be some home view and day view
 * separately Update data in the view once there is new task inserted Sent
 * minute with hour at the same time
 */