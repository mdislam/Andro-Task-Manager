package com.mesba.taskschedular;

import java.util.Calendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

public class AlarmCreator {
	private Context context;

	public AlarmCreator(Context c) {
		this.context = c;
	}	
	public void createAlarm(
			Calendar c, int notificationB4,
			String title,String description,
			String eventStartDayTime){

		c.add(Calendar.MINUTE, notificationB4*-1);
    //Create a new PendingIntent and add it to the AlarmManager
	    Intent intent = new Intent(this.context, AlarmReceiverActivity.class);
	    intent.putExtra("title",title);
	    intent.putExtra("description",description);
	    intent.putExtra("eventStartDayTime",eventStartDayTime);
	    PendingIntent pendingIntent = PendingIntent.getActivity(this.context,12345, intent, PendingIntent.FLAG_CANCEL_CURRENT);
	    AlarmManager am = (AlarmManager)this.context.getSystemService(Context.ALARM_SERVICE);
	    am.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(),pendingIntent); 	
	  
	}
}
