/* AUTHOR: Elena Oat
 * Class for ListView display on the main screen
 * 
 */

package com.mesba.taskschedular;

public class Slot {
	public int id;
	public int time;
	public String name;

	public Slot(int id, int time, String name) {
		this.time = time;
		this.name = name;
		this.id = id;
	}

	//returns time as string: "06:00", "12:00"
	public String timeStr(){
		StringBuilder builder = new StringBuilder();
		if (time <= 9) {
			
			  builder.append("0"); 
			  builder.append(time);
			  builder.append(":00");
			  return builder.toString();
			
		} else {
			
			 builder.append(time); 
			 builder.append(":00");
			 return builder.toString();
			
		}
	}
	
	// returns hour as string: "06", "12", etc.
	public String hourStr(){
		StringBuilder builder = new StringBuilder();
		if (time <= 9) {
			
			  builder.append("0"); 
			  builder.append(time);

			  return builder.toString();
			
		} else {			
			 builder.append(time); 
			 return builder.toString();
			
		}
	}

	public int getId() {
		return id;
	}

	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}

	public String getName() {
		return name;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}
}
