/*AUTHOR: Elena Oat
 * Class for storing event time information 
 * */

package com.mesba.taskschedular;

public class CustomTime {
	private int hour;
	private int minute;
	
	public CustomTime(int hour, int minute){
		this.hour = hour;
		this.minute = minute;
	}

	public CustomTime(String date){
		this.hour = Integer.parseInt(date.substring(11, 13));
		this.minute = Integer.parseInt(date.substring(14, 16));
	}
	public int getHour() {
		return hour;
	}

	public void setHour(int hour) {
		this.hour = hour;
	}

	public int getMinute() {
		return minute;
	}

	public void setMinute(int minute) {
		this.minute = minute;
	}
	public String getTimeStr(){
		StringBuffer strBuff = new StringBuffer();
		strBuff.append(padTime(hour));
		strBuff.append(":");
		strBuff.append(padTime(minute));
		return strBuff.toString();
	}

	public StringBuffer padTime(int m) {
		StringBuffer strBuff = new StringBuffer();
		
		if (Integer.toString(m).length() == 1) {
			strBuff.append(0);
		}
		strBuff.append(m);
		return strBuff;
	}
	
	


}
