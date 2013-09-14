package com.mesba.taskschedular;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import com.mesba.dynamicui.R;

public class DeleteTaskActivity extends Activity {

	private static final String STATE_SELECTED_NAVIGATION_ITEM = "selected_navigation_item";

	private DatabaseAdapter dbAdapter;
	private EditText etTitle, etBody, etNotify;
	private Button fromTimeBtn, toTimeBtn, fromDateBtn, toDateBtn, recDateBtn;
	private String notification_before;
	private int id;
	public CustomDate date_DT_from, date_DT_to, date_DT_rec, date_save_from,
			date_save_to, date_save_rec;
	public CustomTime time_DT_from, time_DT_to, time_save_from, time_save_to;

	private String name, description;


	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_delete_task);

		dbAdapter = new DatabaseAdapter(getApplicationContext());

		ActionBar actionbar = getActionBar();
		actionbar.setDisplayHomeAsUpEnabled(true);

		fromTimeBtn = (Button) findViewById(R.id.from_time_picker);
		toTimeBtn = (Button) findViewById(R.id.to_time_picker);
		fromDateBtn = (Button) findViewById(R.id.from_date_picker);
		toDateBtn = (Button) findViewById(R.id.to_date_picker);
		recDateBtn = (Button) findViewById(R.id.rec_date_picker);

		// recDateBtn.setText("Click if not 'none'");

		etTitle = (EditText) findViewById(R.id.etTitle);
		etBody = (EditText) findViewById(R.id.etNote);
		etNotify = (EditText) findViewById(R.id.notify_before);
		Intent intent = getIntent();
		//tag = intent.getStringExtra("tag");

		if (intent.getStringExtra("tag").equals("delete")) {
			if (intent.hasExtra("com.taskmanager.ID")) {
				id = intent.getIntExtra("com.taskmanager.ID", 0);
				getDataFromDatabase(id);
				getDefaultSettingsFromDatabase();
				etTitle.setText(name);
				etBody.setText(description);
				// Log.v("ID obtained from another activity",
				// Integer.toString(id));
			} else {
				Log.v("Error", "obtaining ID");
			}
		}

		setReceivedTime();
		setReceivedDate();
		// addListenerOnSpinnerItemSelection();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_delete_task, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent = new Intent(getApplicationContext(), MainActivity.class);
		switch (item.getItemId()) {
		case android.R.id.home:
			// app icon in action bar clicked; go home
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			break;
		case R.id.item_delete:
			delete();
			break;
		case R.id.item_cancel:
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			break;
		}

		return super.onOptionsItemSelected(item);
	}

	@SuppressLint("NewApi")
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		if (savedInstanceState.containsKey(STATE_SELECTED_NAVIGATION_ITEM)) {
			getActionBar().setSelectedNavigationItem(
					savedInstanceState.getInt(STATE_SELECTED_NAVIGATION_ITEM));
		}
	}

	@SuppressLint("NewApi")
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		// outState.putInt(STATE_SELECTED_NAVIGATION_ITEM, getActionBar()
		// .getSelectedNavigationIndex());
	}

	// Button click -> display TimePickerDialog

	public void setReceivedTime() {
		fromTimeBtn.setText(time_DT_from.getTimeStr());
		toTimeBtn.setText(time_DT_to.getTimeStr());
	}

	public void setReceivedDate() {

		fromDateBtn.setText(date_DT_from.getDate());
		toDateBtn.setText(date_DT_to.getDate());
		recDateBtn.setText(date_DT_to.getDate());
		Log.v("date received", date_DT_to.getDate());
	}

	private void getDefaultSettingsFromDatabase() {
		dbAdapter.Open();
		notification_before = dbAdapter.getNotificationB4();
		// Log.v("notifi_befre", Integer.toString(notification_before));
		etNotify.setText(notification_before);

		dbAdapter.Close();
	}

	private void getDataFromDatabase(int eventId) {
		dbAdapter.Open();
		Cursor cursor = dbAdapter.viewEventByID(eventId);
		cursor.moveToFirst();

		date_DT_from = new CustomDate(cursor.getString(cursor
				.getColumnIndex("eventStartDayTime")));
		date_DT_to = new CustomDate(cursor.getString(cursor
				.getColumnIndex("eventEndDayTime")));
		time_DT_from = new CustomTime(cursor.getString(cursor
				.getColumnIndex("eventStartDayTime")));
		time_DT_to = new CustomTime(cursor.getString(cursor
				.getColumnIndex("eventEndDayTime")));
		name = cursor.getString(cursor.getColumnIndex("name"));
		description = cursor.getString(cursor.getColumnIndex("description"));

		dbAdapter.Close();
	}

	private void delete() {
		dbAdapter.Open();
		if (dbAdapter.deleteEvents(id)){
			Intent i = new Intent(this, MainActivity.class);
			startActivity(i);
		}
		else {
			HelperMethods.displayToast("Failed deleting event", this);
		}
		dbAdapter.Close();
		
	}

}
