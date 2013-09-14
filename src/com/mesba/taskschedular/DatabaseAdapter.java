package com.mesba.taskschedular;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteStatement;
import android.net.ParseException;
import android.os.Bundle;
import android.util.Log;

public class DatabaseAdapter extends Activity{
    private SQLiteDatabase database;
    private DatabaseOpenHelper dbHelper;
    private Context context;
    private long editParentId = 0;
       
    public DatabaseAdapter(Context c) {
        this.context = c;
        dbHelper = new DatabaseOpenHelper(context);
    }

    public void Open() {
        database = dbHelper.getWritableDatabase();
    }

    public void Close() {
        database.close();
    }

    public Cursor getAllConfig() {

        return database.query("global_config", new String[] { "property",
                "valueType", "textValue", "integerValue", "dateValue" }, null,
                null, null, null, null);
    }

    /**
     *  initialize global configuration table
     */
    public void initializeGConfig() {
        Cursor cursor;
        cursor = database.rawQuery("SELECT count(1) FROM global_config", null);
        cursor.moveToFirst();
        if (Long.parseLong(cursor.getString(0)) == 0) {
            ContentValues values = new ContentValues();

            values.put("property", "NotificationB4");
            values.put("valueType", "int");
            values.put("integerValue", "15");

            database.insert("global_config", "", values);

            values.put("property", "NotificationFreq");
            values.put("valueType", "int");
            values.put("integerValue", "5");

            database.insert("global_config", "", values);

            values.put("property", "NotificationType");
            values.put("valueType", "text");
            values.put("textValue", "Both");
            values.put("integerValue", "");

            database.insert("global_config", "", values);

            values.put("property", "RecurrenceFlag");
            values.put("valueType", "text");
            values.put("textValue", "");
            values.put("integerValue", "");

            database.insert("global_config", "", values);
        }
        cursor.close();
    }
   
    /**
      * @param id
      * @return
      */
        public Cursor getDistinctEventDates() {
            Cursor cursor;
            cursor = database.rawQuery("SELECT distinct substr(eventStartDayTime,1,10) FROM master_event where date(eventStartDayTime)>= date('now')", null);
            return cursor;
        }
   
   
 /**
  * @param id
  * @return
  */
    public Cursor viewEventByID(long id) {
    	Cursor cursor;
    	Log.v("gets here", "gets to viewEventById");
    	cursor = database.query("master_event", null, "id=" + id, null, null,
                null, null);
    	if (cursor.moveToFirst() && cursor != null){
    		return cursor;

    	} else 
    	{
    		Log.v("null cursor", "Cursor is null");
    		return null;
    	}
    }

    /**
     *
     * @param searchDate
     *            : Input parameter format: 'YYYY-MM-DD'
     * @return Cursor having the following columns:
     *         name,description,eventStartDayTime,eventEndDayTime
     */
    public Cursor getEventByDate(String searchDate) {

        Cursor cursor;
        cursor = database
                .rawQuery(
                        "SELECT id,name,description,eventStartDayTime,eventEndDayTime FROM master_event "
                                + "WHERE substr(eventStartDayTime,1,10) = '"
                                + searchDate + "'", null);
        return cursor;
    }
   
    /**
     * updated - Mesbahul
     * @return
     */
    public Cursor getAllEvents() {
        String sql = "SELECT id,name,description,eventStartDayTime,eventEndDayTime FROM master_event ORDER BY eventStartDayTime DESC";
        return database.rawQuery(sql, null);
    }

    public Cursor getOneEvent(long id) {
        return database.query("master_event", null, "id=" + id, null, null,
                null, null);
    }

    public boolean executeSql(String sql) {

        database.beginTransaction();
        SQLiteStatement sqlStmt = database.compileStatement(sql);
        try {
            sqlStmt.execute();
            database.setTransactionSuccessful();

        } catch (SQLiteException ex) {

            ex.printStackTrace();
            return false;

        } finally {
            database.endTransaction();
        }

        return true;
    }

    /**
     *
     * @param name
     * @param description
     * @param eventStartDayTime
     * @param eventEndDayTime
     * @param notificationFreq
     * @param notificationB4
     * @param notificationType
     * @param recurrenceFlag
     * @param recurrenceEndDay
     * @return
     */
    public long createEvent(String name, String description,
            String eventStartDayTime, String eventEndDayTime,
            String notificationFreq, String notificationB4,
            String notificationType, String recurrenceFlag,
            String recurrenceEndDay) {
        // TODO createEvent

        String sql, startTimeString, endTimeString;
        long parentId;
        Date startDate = null, endDate = null, endDayTime = null;
        int incr = 0,notificationB4int;
       
        AlarmCreator alarmCreator = new AlarmCreator(this.context);
       
        if(notificationB4.equals(""))
            notificationB4int = 15;
            //notificationB4int=getNotificationB4();
        else
            notificationB4int=Integer.parseInt(notificationB4);
           
       
       
        if (recurrenceFlag.equals("Once") || recurrenceFlag.equals("")) {           
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");

            try {
                startDate = format.parse(eventStartDayTime);
                endDate = format.parse(recurrenceEndDay);
                endDayTime = format.parse(eventEndDayTime);
                // System.out.println(date);
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (java.text.ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            Calendar c = Calendar.getInstance();
            c.setTime(startDate);
           
           
            sql = "insert into master_event (name,description,parentId,eventStartDayTime,"
                    + "eventEndDayTime,notificationFreq,notificationB4,notificationType,recurrenceFlag,"
                    + "recurrenceEndDay)"
                    + "values('"
                    + name
                    + "','"
                    + description
                    + "','"
                    + this.editParentId
                    + "','"
                    + eventStartDayTime
                    + "','"
                    + eventEndDayTime
                    + "','"
                    + notificationFreq
                    + "','"
                    + notificationB4
                    + "','"
                    + notificationType
                    + "','"
                    + recurrenceFlag
                    + "','"
                    + recurrenceEndDay + "')";

            if (!executeSql(sql))
                return 0;
            else
                alarmCreator.createAlarm(c, notificationB4int, name, description, eventStartDayTime);
        } else if (recurrenceFlag.equals("Daily")
                || recurrenceFlag.equals("Weekly")) {
            if (recurrenceFlag.equals("Daily"))
                incr = 1;
            else
                incr = 7;

            ContentValues values = new ContentValues();

            values.put("name", name);
            values.put("description", description);
            values.put("eventStartDayTime", eventStartDayTime);
            values.put("eventEndDayTime", eventEndDayTime);
            values.put("notificationFreq", notificationFreq);
            values.put("notificationB4", notificationB4);
            values.put("notificationType", notificationType);
            values.put("recurrenceFlag", recurrenceFlag);
            values.put("recurrenceEndDay", recurrenceEndDay);

            parentId = database.insert("master_event", "", values);

            if (parentId == -1)
                return 0;
            //else
                //alarmCreator.createAlarm(c, notificationB4int, name, description, eventStartDayTime);


            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");

            try {
                startDate = format.parse(eventStartDayTime);
                endDate = format.parse(recurrenceEndDay);
                endDayTime = format.parse(eventEndDayTime);
            } catch (ParseException e) {
                e.printStackTrace();
            } catch (java.text.ParseException e) {
                e.printStackTrace();
            }

            Calendar c = Calendar.getInstance();
            c.setTime(startDate);

            Calendar c1 = Calendar.getInstance();
            c1.setTime(endDayTime);

            c.add(Calendar.DATE, incr);
            startDate = c.getTime();

            c1.add(Calendar.DATE, incr);
            endDayTime = c1.getTime();

            while (startDate.before(endDate)) {

                startTimeString = format.format(startDate);
                endTimeString = format.format(endDayTime);

                sql = "insert into master_event (name,description,parentId,eventStartDayTime,"
                        + "eventEndDayTime,notificationFreq,notificationB4,notificationType,recurrenceFlag,"
                        + "recurrenceEndDay)"
                        + "values('"
                        + name
                        + "','"
                        + description
                        + "','"
                        + parentId
                        + "','"
                        + startTimeString
                        + "','"
                        + endTimeString
                        + "','"
                        + notificationFreq
                        + "','"
                        + notificationB4
                        + "','"
                        + notificationType
                        + "','"
                        + recurrenceFlag
                        + "','"
                        + recurrenceEndDay + "')";

                if (!executeSql(sql))
                    return 0;
                else
                    alarmCreator.createAlarm(c, notificationB4int, name, description, eventStartDayTime);
               
                c.add(Calendar.DATE, incr);
                startDate = (Date) c.getTime();

                c1.add(Calendar.DATE, incr);
                endDayTime = c1.getTime();

            }// end while

        } else if (recurrenceFlag.equals("Monthly")) {

            ContentValues values = new ContentValues();

            values.put("name", name);
            values.put("description", description);
            values.put("eventStartDayTime", eventStartDayTime);
            values.put("eventEndDayTime", eventEndDayTime);
            values.put("notificationFreq", notificationFreq);
            values.put("notificationB4", notificationB4);
            values.put("notificationType", notificationType);
            values.put("recurrenceFlag", recurrenceFlag);
            values.put("recurrenceEndDay", recurrenceEndDay);

            parentId = database.insert("master_event", "", values);

            if (parentId == -1)
                return 0;
            //else
                //createAlarm(c,notificationB4);

            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");

            try {
                startDate = format.parse(eventStartDayTime);
                endDate = format.parse(recurrenceEndDay);
                endDayTime = format.parse(eventEndDayTime);
                // System.out.println(date);
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (java.text.ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            Calendar c = Calendar.getInstance();
            c.setTime(startDate);

            Calendar c1 = Calendar.getInstance();
            c1.setTime(endDayTime);

            c.add(Calendar.MONTH, 1);
            startDate = c.getTime();

            c1.add(Calendar.MONTH, 1);
            endDayTime = c1.getTime();

            while (startDate.before(endDate)) {

                startTimeString = format.format(startDate);
                endTimeString = format.format(endDayTime);

                sql = "insert into master_event (name,description,parentId,eventStartDayTime,"
                        + "eventEndDayTime,notificationFreq,notificationB4,notificationType,recurrenceFlag,"
                        + "recurrenceEndDay)"
                        + "values('"
                        + name
                        + "','"
                        + description
                        + "','"
                        + parentId
                        + "','"
                        + startTimeString
                        + "','"
                        + endTimeString
                        + "','"
                        + notificationFreq
                        + "','"
                        + notificationB4
                        + "','"
                        + notificationType
                        + "','"
                        + recurrenceFlag
                        + "','"
                        + recurrenceEndDay + "')";

                if (!executeSql(sql))
                    return 0;
                else
                    alarmCreator.createAlarm(c, notificationB4int, name, description, eventStartDayTime);
               
                c.add(Calendar.MONTH, 1);
                startDate = (Date) c.getTime();

                c1.add(Calendar.MONTH, 1);
                endDayTime = c1.getTime();

            } // end while

        } else {
            return 0;
        }

        return 1;
    }
   
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);   
    }
/**
 *
 * @param id
 * @return
 */
    public String getRecurrenceFlag(long id) {

        Cursor cursor;
        cursor = database.rawQuery("SELECT recurrenceFlag FROM master_event "
                + "WHERE id =" + id, null);
        cursor.moveToFirst();
        return cursor.getString(0);

    }
/**
 *
 * @param id
 * @return
 */
    public long getParentId(int id) {

        Cursor cursor;
        cursor = database.rawQuery("SELECT parentId FROM master_event "
                + "WHERE id =" + id, null);
        cursor.moveToFirst();
        return cursor.getInt(0);
    }
/**
 *
 * @param id
 * @return
 */
    public boolean deleteEvents(int id) {
        // TODO delete all rows that has the perentID equal to given ID

        // String tmprecur=getRecurrenceFlag(id);

        if (getRecurrenceFlag(id).equals("")) {
            database.execSQL("delete from master_event where id = " + id);
            return true;
        } else if (!getRecurrenceFlag(id).equals("")) {
            database.execSQL("delete from master_event where id = " + id
                    + " or parentID = " + id);
            Log.v("inside databaseadapter", "inside");
            return true;
        } else {
            return false;
        }

    }
/**
 *
 * @param sId
 * @param name
 * @param description
 * @param eventStartDayTime
 * @param eventEndDayTime
 * @param notificationFreq
 * @param notificationB4
 * @param notificationType
 * @param recurrenceFlag
 * @param recurrenceEndDay
 * @return
 */
    public boolean editEvent(int sId, String name, String description,
            String eventStartDayTime, String eventEndDayTime,
            String notificationFreq, String notificationB4,
            String notificationType, String recurrenceFlag,
            String recurrenceEndDay) {
        // TODO createEvent

        if ((editParentId = getParentId(sId)) > 0) {
            recurrenceFlag = "";
        }

        Log.v("inside databaseadapter", "inside");
        deleteEvents(sId);
        createEvent(name, description, eventStartDayTime, eventEndDayTime,
                notificationFreq, notificationB4, notificationType,
                recurrenceFlag, recurrenceEndDay);
        this.editParentId = 0;
        return true;
    }

    /**/
    public String getNotificationB4() {
        String var = "15";
        Cursor cursor;
        cursor = database.rawQuery("SELECT integerValue FROM global_config "
                + "WHERE property = 'NotificationB4' ", null);
       
        if (cursor.moveToFirst()){
           // var = Integer.parseInt(cursor.getString(cursor.getColumnIndex("integerValue")));
        	var = cursor.getString(cursor.getColumnIndex("integerValue"));
        }
        Log.v("var", cursor.getString(cursor.getColumnIndex("integerValue")));
        cursor.close();
        return var;
    }

    public int getNotificationFreq() {
        Cursor cursor;
        cursor = database.rawQuery("SELECT integerValue FROM global_config "
                + "WHERE property = 'NotificationFreq' ", null);
        return Integer.parseInt(cursor.getString(0));
    }

    public String getNotificationType() {
        Cursor cursor;
        cursor = database.rawQuery("SELECT textValue FROM global_config "
                + "WHERE property = 'NotificationType' ", null);
        return cursor.getString(0);
    }

    public String getRecurrenceFlag() {

        return null;
    }

    public boolean setNotificationB4(int newValue) {

        String sql = "UPDATE global_config SET integerValue = '" + newValue
                + "' WHERE property = 'NotificationB4'";
        if (executeSql(sql))
            return true;
        return false;

    }
  /**
   *
   * @param newValue
   * @return
   */
    public boolean setNotificationFreq(int newValue) {

        String sql = "UPDATE global_config SET integerValue = '" + newValue
                + "' WHERE property = 'NotificationFreq'";
        if (executeSql(sql))
            return true;
        return false;

    }
/**
 *
 * @param txt
 * @return
 */
    public boolean setNotificationType(String txt) {

        String sql = "UPDATE global_config SET textValue = '" + txt
                + "' WHERE property = 'NotificationType'";
        if (executeSql(sql))
            return true;
        return false;

    }
/**
 *
 * @param txt
 * @return
 */
    public boolean setRecurrenceFlag(String txt) {

        String sql = "UPDATE global_config SET textValue = '" + txt
                + "' WHERE Property = 'RecurrenceFlag'";
        if (executeSql(sql))
            return true;
        return false;

    }
/**
 *
 * @param txt
 * @return
 */
    public boolean setRecurrenceEndTime(String txt) {

        String sql = "UPDATE global_config SET TextValue = " + txt
                + " WHERE Property = 'RecurrenceEndTime'";
        if (executeSql(sql))
            return true;
        return false;

    }
/**
 *    
 * @param id
 * @param title
 * @param body
 */

    public void updateEvent(long id, String title, String body) {
        ContentValues editCon = new ContentValues();
        editCon.put("title", title);
        editCon.put("body", body);
        database.update("master_event", editCon, "_id=" + id, null);
    }

    /**
     *
     * @param sId
     * @param name
     * @param description
     * @param eventStartDayTime
     * @param eventEndDayTime
     * @param notificationFreq
     * @param notificationB4
     * @param notificationType
     * @param recurrenceFlag
     * @param recurrenceEndDay
     * @return
     */

    /*
     * Insert a new event in the database Input parameter format: all are String
     * Output: 0 = failure; 1 = success
     */
    public long createBriefEvent(String name, String description,
            String eventStartDayTime, String eventEndDayTime) {
        // TODO createEvent
        String sql = "insert into master_event (name,description,eventStartDayTime,"
                + "eventEndDayTime)"
                + "values('"
                + '?'
                + "','"
                + '?'
                + "','" + eventStartDayTime + "','" + eventEndDayTime + "')";

        database.beginTransaction();
        SQLiteStatement sqlStmt = database.compileStatement(sql);
        try {
            sqlStmt.execute();
            database.setTransactionSuccessful();
        } catch (SQLiteException ex) {
            ex.printStackTrace();
            return 0;
        } finally {
            database.endTransaction();
        }
        return 1;
    }

}