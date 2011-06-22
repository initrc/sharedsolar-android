package org.sharedsolar.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "sharedsolar";
	private static final int DATABASE_VERSION = 1;
	public static final String USER_TABLE = "user";
	public static final String TOKEN_TABLE = "token";
	
	private static final String CREATE_USER = "create table " 
		+ USER_TABLE + " (_id integer primary key autoincrement, "
		+ "username text not null, password text not null);";
	private static final String CREATE_TOKEN = "create table " 
		+ TOKEN_TABLE + " (_id integer primary key autoincrement, "
		+ "token_id integer not null, denomination integer not null, "
		+ "state integer not null, account_id text, "
		+ "timestamp timestamp default CURRENT_TIMESTAMP);";
	
	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_USER);
		db.execSQL(CREATE_TOKEN);
	}

	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(DatabaseHelper.class.getName(), 
				"Upgrading database from version " + oldVersion + " to " + newVersion);
		db.execSQL("DROP TABLE IF EXISTS " + USER_TABLE);
		db.execSQL("DROP TABLE IF EXISTS " + TOKEN_TABLE);
		onCreate(db);
	}
}
