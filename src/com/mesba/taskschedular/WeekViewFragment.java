/**
 * AUTHOR: Md. Mesbahul Islam
 * This Class is responsible for the Week View
 */
package com.mesba.taskschedular;

import java.awt.font.TextAttribute;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mesba.dynamicui.R;

@SuppressLint({ "NewApi", "SimpleDateFormat" })
public class WeekViewFragment extends Fragment implements OnClickListener {
	private View weekView;

	private ImageButton prevWeek;
	private ImageButton nextWeek;
	private LinearLayout lLayout, cLayout;
	private TextView tView;
	private final String[] weekdays = new String[] { "Mon", "Tue", "Wed",
			"Thu", "Fri", "Sat", "Sun" };
	int weekNumber, year;
	private Calendar c1;

	List<TextView> allTvs = new ArrayList<TextView>();
	List<TextView> dateTvs = new ArrayList<TextView>();
	TextView[] busket = new TextView[1];

	public final static String DATE = "com.taskmanager.DATE";
	public final static String HOUR = "com.taskmanager.HOUR";
	public final static String ID = "com.taskmanager.ID";

	public SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	public Calendar today = Calendar.getInstance();

	private ArrayList<HashMap<String, String>> allTasks = new ArrayList<HashMap<String, String>>();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		setHasOptionsMenu(true);
		weekView = inflater
				.inflate(R.layout.layout_week_view, container, false);

		allTvs.clear();
		dateTvs.clear();

		prevWeek = (ImageButton) weekView.findViewById(R.id.prevWeek);
		prevWeek.setOnClickListener(this);

		nextWeek = (ImageButton) weekView.findViewById(R.id.nextWeek);
		nextWeek.setOnClickListener(this);

		c1 = Calendar.getInstance();
		weekNumber = c1.get(Calendar.WEEK_OF_YEAR);

		loadData(weekNumber);

		weekView(weekNumber);

		return weekView;
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// TODO Add your menu entries here
		Log.i("OptionsMenu", "Menu Item is override in Week Fragment");
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {

		// When today button is clicked, the view will set to current week
		case R.id.item_today:
			Log.i("OptionsMenu", "option 1 selected from Week Fragment");
			c1 = Calendar.getInstance();
			weekNumber = c1.get(Calendar.WEEK_OF_YEAR);

			refreshWeekView();
			weekView(weekNumber);
			break;
		}

		return super.onOptionsItemSelected(item);
	}

	/**
	 * This function will draw the week view.
	 * 
	 * @param weekNum
	 *            is the week number of current year
	 */
	public void weekView(int weekNum) {
		c1.set(Calendar.WEEK_OF_YEAR, weekNum);
		c1.get(Calendar.WEEK_OF_YEAR);

		// first day of week
		c1.set(Calendar.DAY_OF_WEEK, c1.getFirstDayOfWeek());

		int year1 = c1.get(Calendar.YEAR);
		int month1 = c1.get(Calendar.MONTH) + 1;
		int day1 = c1.get(Calendar.DAY_OF_MONTH);

		// last day of week
		c1.set(Calendar.DAY_OF_WEEK, 7);

		int year7 = c1.get(Calendar.YEAR);
		int month7 = c1.get(Calendar.MONTH) + 1;
		int day7 = c1.get(Calendar.DAY_OF_MONTH) + 1;

		tView = new TextView(getActivity());
		tView = (TextView) weekView.findViewById(R.id.weekHeaderTxt);
		String weekHeaderText = "";
		String yearString = "";

		if (month1 == month7) {
			weekHeaderText = day1 + " - "
					+ day7 + " "
					+ new DateFormatSymbols().getMonths()[month7 - 1];
		} else {
			weekHeaderText = day1 + " "
					+ new DateFormatSymbols().getMonths()[month1 - 1] + " - "
					+ day7 + " "
					+ new DateFormatSymbols().getMonths()[month7 - 1];
		}
		
		if(year1 == year7)
			yearString = "" + year1;
//		else
//			yearString = "" + year1 + "/" + year7;
		// setting the TOP text
		tView.setText("[W" + c1.get(Calendar.WEEK_OF_YEAR) + "] " + weekHeaderText + ", " + yearString);
		lLayout = new LinearLayout(getActivity());
		lLayout = (LinearLayout) weekView.findViewById(R.id.weekDays);
		lLayout.setOrientation(LinearLayout.HORIZONTAL);

		tView = new TextView(getActivity());
		tView.setText("");
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		params.weight = 11;
		tView.setLayoutParams(params);
		lLayout.addView(tView);

		int day, month;
		// creating seven days name and the date
		for (int i = 0; i < 7; i++) {
			c1.set(Calendar.DAY_OF_WEEK, i + 2);

			month = c1.get(Calendar.MONTH) + 1;
			day = c1.get(Calendar.DAY_OF_MONTH);

			tView = new TextView(getActivity());
			tView.setText(weekdays[i] + "\n" + day + "/" + month);
			tView.setTypeface(Typeface.SERIF);
			tView.setTag(c1.get(Calendar.YEAR));
			tView.setGravity(Gravity.CENTER);
			params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT);
			params.weight = 13;
			tView.setLayoutParams(params);
			// tView.setBackgroundResource(R.drawable.rect_shape);
			dateTvs.add(tView);
			lLayout.addView(tView);
		}

		// details of week
		lLayout = (LinearLayout) weekView.findViewById(R.id.weekDaysDetails);

		LinearLayout.LayoutParams cParams = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

		for (int p = 0; p < 24; p++) {
			cLayout = new LinearLayout(getActivity());
			cLayout.setOrientation(LinearLayout.HORIZONTAL);
			cParams.weight = 100;
			cLayout.setLayoutParams(cParams);

			tView = new TextView(getActivity());

			String DayTime = "";

			if (p < 10)
				DayTime = "0" + p + ":00";
			else
				DayTime = p + ":00";

			tView.setText(DayTime);
			tView.setTypeface(Typeface.SERIF);
			tView.setTextAlignment(4);
//			tView.setGravity(Gravity.CENTER);

			LinearLayout.LayoutParams tParams = new LinearLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT, 60);
			tParams.weight = 9;
			tView.setLayoutParams(tParams);

			cLayout.addView(tView);

			for (int q = 0; q < 7; q++) {
				tView = new TextView(getActivity());
				tView.setId(allTvs.size() + 1);
				tView.setClickable(true);
				String infoTag = dateTvs.get(q).getText() + "/"
						+ dateTvs.get(q).getTag().toString();

				String tvTag = getTagString(infoTag, DayTime);

				tView.setTag(tvTag);

				String tvText = getTvData(tvTag);

				if (!tvText.equals("no data")) {
					tView.setBackgroundResource(R.drawable.rect_shape);
				} else {
					tView.setBackgroundResource(R.drawable.round_rect_shape);
				}

				tView.setText("");

				tParams = new LinearLayout.LayoutParams(
						LayoutParams.WRAP_CONTENT, 60);
				tParams.weight = 13;
				tView.setLayoutParams(tParams);

				tView.setOnClickListener(this);

				allTvs.add(tView);

				cLayout.addView(tView);
			}

			lLayout.addView(cLayout);
		}
	}

	public String getTagString(String info, String DayTimeString) {
		String dateParts[] = info.split("\\r?\\n")[1].split("/");
		String timeParts[] = DayTimeString.split(":");

		String yr = dateParts[2];
		String month = dateParts[1];
		String date = dateParts[0];

		if (month.length() == 1)
			month = "0" + month;
		if (date.length() == 1)
			date = "0" + date;

		return yr + "-" + month + "-" + date + ":" + timeParts[0];
	}

	@Override
	public void onClick(View v) {
		int viewId = v.getId();
		if (v == prevWeek) {
			if (weekNumber <= 1) {
				weekNumber = 52;
				year--;
			} else {
				weekNumber--;
			}
			refreshWeekView();
			weekView(weekNumber);
		} else if (v == nextWeek) {
			if (weekNumber > 52) {
				weekNumber = 1;
				year++;
			} else {
				weekNumber++;
			}

			refreshWeekView();
			weekView(weekNumber);
		} else {
			if (busket[0] == null) {
				busket[0] = allTvs.get(viewId - 1);

				String data = getTvData(busket[0].getTag().toString());

				if (!data.equals("no data")) {
					allTvs.get(viewId - 1).setBackgroundResource(
							R.drawable.rect_shape_blue);
					showTaskDataDialog(Integer.parseInt(data));
				} else {
					allTvs.get(viewId - 1).setBackgroundResource(
							R.drawable.rect_shape_blue);
					HelperMethods.displayToast(
							"Press one more time to create a task",
							getActivity());
					// Toast.makeText(
					// getActivity(),
					// "Selected ID: " + viewId + "\n"
					// + v.getTag().toString(), Toast.LENGTH_SHORT)
					// .show();
				}

			} else {
				// if select the same textView again then go to add task
				// activity
				if (busket[0].getTag().toString() == v.getTag().toString()) {
					String tagParts[] = v.getTag().toString().split(":");
					String data = getTvData(busket[0].getTag().toString());

					if (!data.equals("no data")) {
						int taskId = Integer.parseInt(allTasks.get(
								Integer.parseInt(data)).get("id"));
						Intent intent = new Intent(getActivity(),
								AddNewTaskActivity.class);
						intent.putExtra("tag", "edit");
						intent.putExtra(ID, taskId);
						startActivity(intent);
					} else {
						Intent intent = new Intent(getActivity(),
								AddNewTaskActivity.class);
						String date = tagParts[0];
						int time = Integer.parseInt(tagParts[1]);
						intent.putExtra(DATE, date);
						intent.putExtra(HOUR, time);
						intent.putExtra("tag", "add");
						startActivity(intent);
					}
				}
				// if select a new textView
				else {
					TextView oldTv = busket[0];
					busket[0] = allTvs.get(viewId - 1);
					// allTvs.get(viewId - 1).setBackgroundColor(Color.GRAY);

					String data = getTvData(busket[0].getTag().toString());
					String dataOld = getTvData(oldTv.getTag().toString());

					if (!data.equals("no data")) {
						if (!dataOld.equals("no data")) {
							oldTv.setBackgroundResource(R.drawable.rect_shape);
						} else {
							oldTv.setBackgroundResource(R.drawable.round_rect_shape);
						}
						allTvs.get(viewId - 1).setBackgroundResource(
								R.drawable.rect_shape_blue);
						showTaskDataDialog(Integer.parseInt(data));
					} else {
						if (!dataOld.equals("no data")) {
							oldTv.setBackgroundResource(R.drawable.rect_shape);
						} else {
							oldTv.setBackgroundResource(R.drawable.round_rect_shape);
						}

						allTvs.get(viewId - 1).setBackgroundResource(
								R.drawable.rect_shape_blue);
						HelperMethods.displayToast(
								"Press one more time to create a task",
								getActivity());
						// Toast.makeText(
						// getActivity(),
						// "Selected ID: " + viewId + "\n"
						// + v.getTag().toString(),
						// Toast.LENGTH_SHORT).show();
					}

				}
			}

		}
	}

	/**
	 * This function is responsible for redrawing the weekView
	 */
	public void refreshWeekView() {
		ViewGroup vg = (ViewGroup) weekView.findViewById(R.id.weekDaysDetails);
		vg.removeAllViews();
		vg.refreshDrawableState();
		vg = (ViewGroup) weekView.findViewById(R.id.weekDays);
		vg.removeAllViews();
		vg.refreshDrawableState();
		allTvs.clear();
		dateTvs.clear();
		loadData(weekNumber);
	}

	/*
	 * this function will load data in a Arraylist of hash map
	 */
	public void loadData(int weekNum) {
		allTasks.clear();
		DatabaseAdapter dbAdapter = new DatabaseAdapter(getActivity());

		dbAdapter.Open();

		c1.set(Calendar.WEEK_OF_YEAR, weekNum);
		c1.get(Calendar.WEEK_OF_YEAR);

		int day, month;
		String dayStr, monthStr;
		int year = c1.get(Calendar.YEAR);
		// creating seven days name and the date
		for (int i = 0; i < 7; i++) {
			c1.set(Calendar.DAY_OF_WEEK, i + 2);

			month = c1.get(Calendar.MONTH) + 1;
			day = c1.get(Calendar.DAY_OF_MONTH);

			monthStr = (month < 10) ? "0" + month : "" + month;
			dayStr = (day < 10) ? "0" + day : "" + day;

			Cursor curs = dbAdapter.getEventByDate(year + "-" + monthStr + "-"
					+ dayStr);

			if (curs.moveToFirst()) {
				do {
					HashMap<String, String> map = new HashMap<String, String>();

					int id = curs.getInt(curs.getColumnIndex("id"));
					String name = curs.getString(curs.getColumnIndex("name"));
					String description = curs.getString(curs
							.getColumnIndex("description"));
					String eventStartDayTime = curs.getString(curs
							.getColumnIndex("eventStartDayTime"));
					String eventEndDayTime = curs.getString(curs
							.getColumnIndex("eventEndDayTime"));

					map.put("id", "" + id);
					map.put("name", name);
					map.put("description", description);
					map.put("startDate", eventStartDayTime.split(" ")[0]);
					map.put("startHour", eventStartDayTime.split(" ")[1]);
					map.put("endDate", eventEndDayTime.split(" ")[0]);
					map.put("endHour", eventEndDayTime.split(" ")[1]);
					map.put("tag", eventStartDayTime.split(" ")[0] + ":"
							+ eventStartDayTime.split(" ")[1].split(":")[0]);

					allTasks.add(map);

				} while (curs.moveToNext());

			}

			curs.close();
		}

		dbAdapter.Close();
	}

	public String getTvData(String tvTag) {
		String tagStr;
		String tvName = "no data";
		for (int count = 0; count < allTasks.size(); count++) {
			tagStr = allTasks.get(count).get("tag");
			if (tagStr.equals(tvTag)) {
				// tvName = allTasks.get(count).get("name");
				tvName = "" + count;
				break;
			}
		}

		return tvName;
	}

	public void showTaskDataDialog(int index) {
		final Dialog dialogTask = new Dialog(getActivity());
		dialogTask.setContentView(R.layout.layout_task_dialog);

		final int taskId = Integer.parseInt(allTasks.get(index).get("id"));
		final int arrayIndex = index;

		String name = allTasks.get(index).get("name");
		dialogTask.setTitle(name);

		// set the custom dialog components - text, image and button
		TextView textDescription = (TextView) dialogTask
				.findViewById(R.id.task_details);
		TextView textDuration = (TextView) dialogTask
				.findViewById(R.id.task_time);

		String description = allTasks.get(index).get("description");
		String duration = "From: " + allTasks.get(index).get("startDate") + " "
				+ allTasks.get(index).get("startHour") + "\nTo: "
				+ allTasks.get(index).get("endDate") + " "
				+ allTasks.get(index).get("endHour");

		textDescription.setText(description);
		textDuration.setText(duration);

		Button dialogEditButton = (Button) dialogTask
				.findViewById(R.id.edit_btn);
		Button dialogDeleteButton = (Button) dialogTask
				.findViewById(R.id.delete_btn);
		Button dialogCancelButton = (Button) dialogTask
				.findViewById(R.id.cancel_btn);

		// if button is clicked, close the custom dialog
		dialogCancelButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialogTask.dismiss();
			}
		});

		// if button is clicked, go to add new task activity with edit feature
		dialogEditButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(),
						AddNewTaskActivity.class);
				intent.putExtra("tag", "edit");
				intent.putExtra(ID, taskId);
				startActivity(intent);
			}
		});

		// if button is clicked, delete the selected task
		dialogDeleteButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				new AlertDialog.Builder(getActivity())
						.setTitle("Delete entry")
						.setMessage(
								"Are you sure you want to delete this entry?")
						.setPositiveButton("Yes",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int which) {
										// continue with delete
										DatabaseAdapter dbAdapter = new DatabaseAdapter(
												getActivity());
										dbAdapter.Open();

										if (dbAdapter.deleteEvents(taskId)) {
											// Toast.makeText(getActivity(),
											// "Deleted Task" + taskId,
											// Toast.LENGTH_LONG).show();
										} else {
											// Toast.makeText(getActivity(),
											// "Failed to delete Task" + taskId,
											// Toast.LENGTH_LONG).show();
											HelperMethods.displayToast(
													"Delete failed",
													getActivity());
										}
										dbAdapter.Close();

										dialogTask.dismiss();

										allTasks.remove(arrayIndex);

									}
								})
						.setNegativeButton("No",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int which) {
										// do nothing
									}
								}).show();
			}
		});

		dialogTask.show();
	}
}
