package com.mesba.taskschedular;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

import com.mesba.dynamicui.R;

@SuppressLint({ "NewApi", "SimpleDateFormat" })
public class MainActivity extends Activity {
	private static final String STATE_SELECTED_NAVIGATION_ITEM = "selected_navigation_item";
	private DatabaseAdapter dbAdapter;
	//private CreateNotification cn;

	public final static String DATE = "com.taskmanager.DATE";
	public final static String HOUR = "com.taskmanager.HOUR";

	public SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	public Calendar today = Calendar.getInstance();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		dbAdapter = new DatabaseAdapter(getApplicationContext());
		initializeGlobalConfigWithDefaultValues();

		ActionBar actionbar = getActionBar();
		actionbar.show();

		actionbar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		

		// initiating both tabs and set text to it.
		ActionBar.Tab dayTab = actionbar.newTab().setText("Day");
		ActionBar.Tab weekTab = actionbar.newTab().setText("Week");
		ActionBar.Tab monthTab = actionbar.newTab().setText("Month");
		ActionBar.Tab taskListTab = actionbar.newTab().setText("Tasks");

		// create the two fragments we want to use for display content
		Fragment dayFragment = new DayViewFragment();
		Fragment weekFragment = new WeekViewFragment();
		Fragment monthFragment = new MonthViewFragment();
		Fragment tasksFragment = new TaskListViewFragment();

		// set the Tab Listener. Now we can listen for clicks.
		dayTab.setTabListener(new MyTabsListener(dayFragment));
		weekTab.setTabListener(new MyTabsListener(weekFragment));
		monthTab.setTabListener(new MyTabsListener(monthFragment));
		taskListTab.setTabListener(new MyTabsListener(tasksFragment));

		// add tabs to the ActionBar
		// If you want to change the tab order change in following order of adding
		actionbar.addTab(dayTab);
		actionbar.addTab(weekTab);
		actionbar.addTab(monthTab);
		actionbar.addTab(taskListTab);

		// set the app icon as an action to go Home

		actionbar.setDisplayHomeAsUpEnabled(false);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		if (savedInstanceState.containsKey(STATE_SELECTED_NAVIGATION_ITEM)) {
			getActionBar().setSelectedNavigationItem(
					savedInstanceState.getInt(STATE_SELECTED_NAVIGATION_ITEM));
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt(STATE_SELECTED_NAVIGATION_ITEM, getActionBar()
				.getSelectedNavigationIndex());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.item_add:
			Intent intent = new Intent(getApplicationContext(),
					AddNewTaskActivity.class);
			String date = dateFormat.format(today.getTime());
			int time = 0;
			intent.putExtra(DATE, date);
			intent.putExtra(HOUR, time);
			intent.putExtra("tag", "add");
			startActivity(intent);
			break;
//		case R.id.item_today:
//			Log.i("OptionsMenu", "option 1 selected from activity");
//			return false;
//			Toast.makeText(this, "Today...", Toast.LENGTH_SHORT).show();
//			break;
		case R.id.action_settings:
			Intent setting = new Intent(MainActivity.this,
					SettingsPrefs.class);
			startActivity(setting);
			break;
		case R.id.action_about:
			showAbout();
//			Toast.makeText(this, "About us", Toast.LENGTH_SHORT).show();
			break;
		}

		return super.onOptionsItemSelected(item);
	}

	/**
	 * initialize global configuration
	 */
	public void initializeGlobalConfigWithDefaultValues() {
		dbAdapter.Open();
		dbAdapter.initializeGConfig();
		dbAdapter.Close();
	}

	public void showAbout() {
		final Dialog dialog = new Dialog(MainActivity.this);
		dialog.setContentView(R.layout.layout_about);
		dialog.setTitle("About Task Scheduler");

		// set the custom dialog components - text, image and button
	//	TextView text = (TextView) dialog.findViewById(R.id.text);
	//	text.append("\n" + "Android custom dialog example!");
		ImageView image = (ImageView) dialog.findViewById(R.id.image);
		image.setImageResource(R.drawable.ic_launcher_task);

		Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);
		// if button is clicked, close the custom dialog
		dialogButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});

		dialog.show();
	}

	class MyTabsListener implements ActionBar.TabListener {
		public Fragment fragment;

		public MyTabsListener(Fragment fragment) {
			this.fragment = fragment;
		}

		@Override
		public void onTabReselected(Tab tab, FragmentTransaction ft) {
		//	Toast.makeText(getApplicationContext(), "Reselected!",
		//			Toast.LENGTH_LONG).show();
			
		}

		@SuppressLint("NewApi")
		@Override
		public void onTabSelected(Tab tab, FragmentTransaction ft) {
	
				// / If it exists, simply attach it in order to show it
			//	ft.attach(fragment);
				
			
			
			ft.replace(R.id.fragment_container, fragment);
			//ft.show(fragment);
		}

		@SuppressLint("NewApi")
		@Override
		public void onTabUnselected(Tab tab, FragmentTransaction ft) {
			ft.remove(fragment);
		}
	}
}
