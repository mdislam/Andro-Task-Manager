/*AUTHOR: Elena Oat
 * This class is meant for storing data about events which are
 * extracted from database for a particular day and displayed
 * on them main screen. 
 * */

package com.mesba.taskschedular;

public class Task {

	public String name;
	public int id;
	public String description;

	public String eventStartDayTime;
	public String eventEndDayTime;

	public Task() {
		super();
	}

	public Task(int id, String name, String description,
			String eventStartDayTime, String eventEndDayTime) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
		this.eventStartDayTime = eventStartDayTime;
		this.eventEndDayTime = eventEndDayTime;
		// this.eventEndDayTime = eventEndDayTime;
	}

	public String getEventStartDayTime() {
		return eventStartDayTime;
	}

	public void setEventStartDayTime(String eventStartDayTime) {
		this.eventStartDayTime = eventStartDayTime;
	}

	public String getEventEndDayTime() {
		return eventEndDayTime;
	}

	public void setEventEndDayTime(String eventEndDayTime) {
		this.eventEndDayTime = eventEndDayTime;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	/*
	 * public void setEventEndDayTime(String eventEndDayTime) {
	 * this.eventEndDayTime = eventEndDayTime; }
	 * 
	 * public String getEventStartDayTime() { return eventStartDayTime; }
	 * 
	 * public void setEventStartDayTime(String eventStartDayTime) {
	 * this.eventStartDayTime = eventStartDayTime; }
	 */

}