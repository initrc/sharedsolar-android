package org.sharedsolar.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "sharedsolar";
	private static final int DATABASE_VERSION = 1;
	private static final String USER_TABLE = "user";
	private static final String CREDIT_TABLE = "credit";
	private static final String CREATE_USER = "create table " 
		+ USER_TABLE + " (_id integer primary key autoincrement, "
		+ "username text not null, password text not null);";
	private static final String CREATE_CREDIT = "create table " 
		+ CREDIT_TABLE + " (_id integer primary key autoincrement, "
		+ "denomination integer not null, count integer not null);";
	
	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_USER);
		db.execSQL(CREATE_CREDIT);
	}

	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(DatabaseHelper.class.getName(), 
				"Upgrading database from version " + oldVersion + " to " + newVersion);
		db.execSQL("DROP TABLE IF EXISTS " + USER_TABLE);
		db.execSQL("DROP TABLE IF EXISTS " + CREDIT_TABLE);
		onCreate(db);
	}
}
