package com.mesba.taskschedular;

import java.util.Date;
import java.util.Calendar;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import com.mesba.dynamicui.R;

public class CreateNotification {
	
	public Context context;
	
	public CreateNotification(Context c) {
		this.context = c;
	}	
	

	@SuppressWarnings("deprecation")
	public void createNotification(String title, String des, String tme) {
	    // Prepare intent which is triggered if the
	    // notification is selected
	    Intent intent = new Intent(this.context, MainActivity.class);
	    PendingIntent pIntent = PendingIntent.getActivity(this.context,0, intent, 0);
	    
	    Calendar cal = Calendar.getInstance();
	    cal.setTime(new Date());
	    cal.add(Calendar.MINUTE, 4);
	    
	    // Build notification
	    // Actions are just fake
	    NotificationManager nm = (NotificationManager) this.context.getSystemService(Context.NOTIFICATION_SERVICE);
	    
	    Notification notif= new Notification();
	    notif.icon= R.drawable.not;
	    notif.tickerText="Hello";
	    notif.when=cal.getTimeInMillis();
	    notif.setLatestEventInfo(this.context, "Notification !", title+"--"+des+"\n Time"+ tme , pIntent);
	    System.out.print("Here Notification created. \n");
	    
	   
	    nm.notify(1,notif);
	  }

}
