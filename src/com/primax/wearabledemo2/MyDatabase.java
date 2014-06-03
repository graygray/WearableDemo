package com.primax.wearabledemo2;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MyDatabase extends SQLiteOpenHelper{

	public final static int _DBVersion = 1; 							//<-- ª©¥»
	public final static String _DBName = "wristbandDatabase.db";  		//<-- db name
	public final static String _TableName = "wristbandTable"; 		 	//<-- table name
	
	public MyDatabase(Context context) {
        super(context, _DBName, null, _DBVersion);
    }
	
	public MyDatabase(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		if (MainActivity.isDebug) {
			Log.e("gray", "MyDatabase.java:onCreate, " + "create database");
		}
		final String SQL = "CREATE TABLE IF NOT EXISTS " + _TableName + "( " +
				"_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
				"_pressure INTEGER, " +
				"_activity INTEGER, " +
				"_walkStepCount INTEGER, " +
				"_runningStepCount INTEGER, " +
				"_sleepQulity INTEGER, " +
				"_calories FLOAT, " +
				"_calories_standing FLOAT, " +
				"_calories_running FLOAT, " +
				"_calories_walking FLOAT, " +
				"_calories_driving FLOAT, " +
				"_calories_biking FLOAT, " +
				"_calories_sitting FLOAT, " +
				"_calories_swimming FLOAT, " +
				"_calories_sleeping FLOAT, " +
				"_created_at DATETIME " +
				");";
				db.execSQL(SQL);
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		if (MainActivity.isDebug) {
			Log.e("gray", "MyDatabase.java:onUpgrade" + "update database");
		}
		final String SQL = "DROP TABLE " + _TableName;
		db.execSQL(SQL); 
		onCreate(db);
	}

}
