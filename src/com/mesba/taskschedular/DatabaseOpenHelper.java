package com.mesba.taskschedular;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseOpenHelper extends SQLiteOpenHelper {

	public static final String DB_NAME = "taskManager.db";
	public static final String TABLE_MASTER = "master_event";
	public static final String TABLE_GCONFIG = "global_config";
	public static final int DATABASE_VERSION = 1 ;
	public Context context;
	
	public static final String CREATE_TABLE_MASTER = "CREATE TABLE " + TABLE_MASTER
			+ "(id INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ "recurrenceFlag TEXT, "
			+ "recurrenceEndDay TEXT, "
			+ "parentID TEXT, "
			+ "name TEXT, "
			+ "description TEXT, "
			+ "createDayTime TEXT, "
			+ "eventStartDayTime TEXT, "
			+ "eventEndDayTime TEXT, "
			+ "notificationB4 TEXT, "
			+ "notificationFreq TEXT, "
			+ "notificationType TEXT, "
			+ "deleteFlag TEXT);";
	
	public static final String CREATE_TABLE_GCONFIG = "CREATE TABLE " + TABLE_GCONFIG
			+ "(property TEXT PRIMARY KEY, "
			+ "valueType TEXT, "
			+ "textValue TEXT, "
			+ "integerValue TEXT, "
			+ "dateValue TEXT);";
	
	public DatabaseOpenHelper(Context contxt) {
		super(contxt, DB_NAME, null, DATABASE_VERSION);
		this.context=contxt;
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		// TODO Auto-generated method stub
		try{
			
			database.execSQL(CREATE_TABLE_MASTER);
			database.execSQL(CREATE_TABLE_GCONFIG);
			
		} catch (Exception ex){
			ex.printStackTrace();
		}
		
      //(new DatabaseAdapter(context)).initializeGConfig();
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
		if (oldVersion == 1 && newVersion == 2) {
			db.execSQL("DROP TABLE IF EXISTS " + CREATE_TABLE_MASTER);
	        db.execSQL("DROP TABLE IF EXISTS " + CREATE_TABLE_GCONFIG);
	        onCreate(db);
		}

	} //end of onUpgrade

}

