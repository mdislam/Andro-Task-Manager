package com.mesba.taskschedular;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mesba.dynamicui.R;

@SuppressLint("NewApi")
public class TaskListViewFragment extends Fragment {
	private View taskListView;
	private DatabaseAdapter dbAdapter;
	private TextView eventList;
	private String[] dateTimeSplitStart;
	private String[] dateTimeSplitEnd;

	private int counter = 0;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		taskListView = inflater.inflate(R.layout.layout_tasks_list, container,
				false);

		dbAdapter = new DatabaseAdapter(getActivity());

		eventList = (TextView) taskListView.findViewById(R.id.all_tasks);
		

		// eventList.setTextSize(15);
		getAllEvents();

		return taskListView;
	}

	public void getAllEvents() {
		String distinctDate, name, description, start, end = "";
		dbAdapter.Open();

		Cursor c = dbAdapter.getDistinctEventDates();

		if (c.moveToFirst()) {
			do {
				distinctDate = c.getString(0);
				// date.setText(distinctDate);
				// eventList.append(distinctDate + "\n-----------\n");
				eventList.append(Html.fromHtml("<b><div align='center'>" + distinctDate
						+ "</div></b>"));
				Cursor tasklistByDate = dbAdapter.getEventByDate(distinctDate);
				if (tasklistByDate.moveToFirst()) {
					do {

						name = tasklistByDate.getString(tasklistByDate
								.getColumnIndex("name"));
						description = tasklistByDate.getString(tasklistByDate
								.getColumnIndex("description"));
						start = tasklistByDate.getString(tasklistByDate
								.getColumnIndex("eventStartDayTime"));
						end = tasklistByDate.getString(tasklistByDate
								.getColumnIndex("eventEndDayTime"));

						dateTimeSplitStart = start.split(" ");
						dateTimeSplitEnd = end.split(" ");
						eventList.append(Html.fromHtml(++counter + ". " + name + ": "
								+ description + "<br />"
								+ "    " + dateTimeSplitStart[1] + " - " +  dateTimeSplitEnd[1] + "<br />" + "<br />"));
						// eventList.append( ++counter +". "+name + ": " +
						// description+ "\n"
						// + "    "+ dateTimeSplitStart[1]+ " - "+
						// dateTimeSplitEnd[1]+ "\n\n");

					} while (tasklistByDate.moveToNext());
					tasklistByDate.close();
				}
				counter = 0;
			} while (c.moveToNext());

		}

		c.close();

		dbAdapter.Close();

	}
}
