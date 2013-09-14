package com.mesba.taskschedular;

import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.mesba.dynamicui.R;

public class AlarmReceiverActivity extends Activity {

	private MediaPlayer mMediaPlayer;
	private CreateNotification cn;

	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.alarm);

		String message = "";
		String title = "", description = "", eventStartDayTime = "";
		cn=new CreateNotification(getApplicationContext());
		
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			title = extras.getString("title");
			description = extras.getString("description");
			eventStartDayTime = extras.getString("eventStartDayTime");

			message = "\n \n Notification for Task : \n " + title
					+ "\n Description: " + description + "\n Time:"
					+ eventStartDayTime;
		}
		cn.createNotification(title, description, eventStartDayTime);

		TextView item = (TextView) findViewById(R.id.AlarmMessage);
		item.setText(message);

		System.out
				.println("I have called the alarm activity. Means, the alarm has been fired");

		Button stopAlarm = (Button) findViewById(R.id.stopAlarm);
		stopAlarm.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				mMediaPlayer.stop();
				finish();
			}
		});

		playSound(this, getAlarmUri());
		
	}

	private void playSound(Context context, Uri alert) {
		mMediaPlayer = new MediaPlayer();
		try {
			mMediaPlayer.setDataSource(context, alert);
			final AudioManager audioManager = (AudioManager) context
					.getSystemService(Context.AUDIO_SERVICE);
			if (audioManager.getStreamVolume(AudioManager.STREAM_ALARM) != 0) {
				mMediaPlayer.setAudioStreamType(AudioManager.STREAM_ALARM);
				mMediaPlayer.prepare();
				mMediaPlayer.start();
			}
		} catch (IOException e) {
			System.out.println("OOPS");
		}
	}

	/*
	 * Get an alarm sound. Try for an alarm. If none set, try notification,
	 * Otherwise, ringtone.
	 */
	private Uri getAlarmUri() {
		Uri alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
		if (alert == null) {
			alert = RingtoneManager
					.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
			if (alert == null) {
				alert = RingtoneManager
						.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
			}
		}
		return alert;
	}

}
