package org.sharedsolar.db;

import org.sharedsolar.tool.RandomToken;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class DatabaseAdapter {
	
	private Context context;
	private SQLiteDatabase database;
	private DatabaseHelper databaseHelper;
	
	private static final String DATABASE_TABLE = "user";

	public DatabaseAdapter(Context context) {
		this.context = context;
	}
	
	public DatabaseAdapter open() throws SQLException {
		databaseHelper = new DatabaseHelper(context);
		database = databaseHelper.getWritableDatabase();
		init();
		return this;
	}

	public void close() {
		databaseHelper.close();
	}
	
	private ContentValues createContentValues(String username, String password) {
		ContentValues values = new ContentValues();
		values.put("username", username);
		values.put("password", password);
		return values;
	}
	
	public void init() {
		if (isEmpty()) {
			ContentValues values = createContentValues("tech", "tech");
			database.insert(DATABASE_TABLE, null, values);
			values = createContentValues("vendor", RandomToken.generate());
			database.insert(DATABASE_TABLE, null, values);
		}		
	}
	
	public boolean isEmpty() throws SQLException {
		Cursor cursor = database.query(DATABASE_TABLE, new String[] {"password"}, 
				null, null, null, null, null);
		if (cursor == null || cursor.getCount() == 0) return true;
		return false;
	}
	
	public boolean userAuth(String username, String password) throws SQLException {
		Cursor cursor = database.query(true, DATABASE_TABLE, new String[] {"password"}, 
				"username = '" + username +"'", null, null, null, null, null);
		if (cursor != null) {
			cursor.moveToFirst();
			String p=cursor.getString(0);
			if (p.equals(password)) return true;					
		}
		return false;
	}
	
	public boolean delete(String username) {
		return database.delete(DATABASE_TABLE, "username = '" + username + "'", null) > 0;
	}
	
	public String getVendorToken()
	{
		Cursor cursor = database.query(true, DATABASE_TABLE, new String[] {"password"}, 
				"username = 'vendor'", null, null, null, null, null);
		if (cursor != null) {
			cursor.moveToFirst();
			String token=cursor.getString(0);
			return token;					
		}
		return null;
	}
}
