/*AUTHOR: Elena Oat
 * Class, whose object holds event date
 * */

package com.mesba.taskschedular;

public class CustomDate {
	public String year_str;
	public String month_str;
	public String day_str;
	public int year;
	public int month;
	public int day;
	
	public CustomDate(){}
	
	public CustomDate(int day, int month, int year){
		this.day = day;
		this.month = month;
		this.year = year;
		this.year_str = Integer.toString(year);
		this.month_str = padDate(month+1);
		this.day_str = padDate(day);
	}
	
	public CustomDate(String date){
		this.year_str = date.substring(0, 4);
		this.month_str = date.substring(5, 7);
		this.day_str = date.substring(8, 10);
		this.year = Integer.parseInt(date.substring(0, 4));
		this.month = Integer.parseInt(removeZero(date.substring(5, 7))) - 1;
		this.day = Integer.parseInt(removeZero(date.substring(8, 10)));
	}

	public void setYearStr(){
		this.year_str = Integer.toString(year);
	}
	
	public void setMonthStr(){
		this.month_str = padDate(month+1);
	}
	public void setDayStr(){
		this.day_str = padDate(day);
	}

	public String removeZero(String str){
		String str_edited;
		if (str.charAt(0) == '0'){
			str_edited = new String(str.substring(1, 2));
		}
		else {
			str_edited = new String(str);
		}
		return str_edited;
	}
	/*
	 * for UI date
	 */
	public String getDate(){
		//Log.v("day in class", day_str);
	/*	if (day_str.equals("0")){
			
			getDay_str();
		}
		if (month_str.equals("0")){
			
			getMonth_str();
		}

		if (year_str.equals("0")){
			getYear_str();
		}
*/
		StringBuilder strBuilder = new StringBuilder();
		strBuilder.append(day_str);
		strBuilder.append("-");
		strBuilder.append(month_str);
		strBuilder.append("-");
		strBuilder.append(year_str);
		return strBuilder.toString();
	}
	
	public String getDateForDB(){
		StringBuilder strBuilder = new StringBuilder();
		strBuilder.append(year_str);
		strBuilder.append("-");
		strBuilder.append(month_str);
		strBuilder.append("-");
		strBuilder.append(day_str);
		return strBuilder.toString();
	}

	public String getYear_str() {
		year_str = Integer.toString(year);
		return year_str;
	}

	public String getMonth_str() {
		month_str = padDate(month);
		return month_str;
	}

	public String getDay_str() {
		day_str = padDate(day);
		return day_str;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
		
	}

	public int getDay() {
		return day;
	}

	public void setDay(int day) {
		this.day = day;
	}

	public String padDate(int d) {
		StringBuffer strBuff = new StringBuffer();
		
		if (Integer.toString(d).length() == 1) {
			strBuff.append(0);
		}
		strBuff.append(d);
		return strBuff.toString();
	}
}
