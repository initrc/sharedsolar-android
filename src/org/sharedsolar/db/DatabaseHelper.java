package org.sharedsolar.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "sharedsolar";
	private static final String TABLE_NAME = "user";
	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_CREATE = "create table " 
		+ TABLE_NAME + " (_id integer primary key autoincrement, "
		+ "username text not null, password text not null);";
	
	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	public void onCreate(SQLiteDatabase db) {
		db.execSQL(DATABASE_CREATE);
	}

	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(DatabaseHelper.class.getName(), 
				"Upgrading database from version " + oldVersion + " to " + newVersion);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
		onCreate(db);
	}
}
