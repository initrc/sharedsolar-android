package org.sharedsolar.db;

import java.util.ArrayList;

import org.sharedsolar.R;
import org.sharedsolar.model.CreditSummaryModel;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class DatabaseAdapter {
	
	private Context context;
	private SQLiteDatabase database;
	private DatabaseHelper databaseHelper;
	
	private static final String USER_TABLE = "user";
	private static final String CREDIT_TABLE = "credit";
	
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
	
	private ContentValues createUser(String username, String password) {
		ContentValues values = new ContentValues();
		values.put("username", username);
		values.put("password", password);
		return values;
	}
	
	private ContentValues createCredit(int denomination, int count) {
		ContentValues values = new ContentValues();
		values.put("denomination", denomination);
		values.put("count", count);
		return values;
	}
	
	public void init() {
		if (isEmpty()) {
			// insert USER
			ContentValues values = createUser("tech", "");
			database.insert(USER_TABLE, null, values);
			// insert CREDIT
			String[] denominationValues = context.getResources().getStringArray(R.array.denominationValues);
			for (String v : denominationValues) {
				values = createCredit(Integer.parseInt(v), 0);
				database.insert(CREDIT_TABLE, null, values);
			}
		}		
	}
	
	public boolean isEmpty() throws SQLException {
		Cursor cursor = database.query(USER_TABLE, new String[] {"password"}, 
				null, null, null, null, null);
		if (cursor == null || cursor.getCount() == 0) return true;
		return false;
	}
	
	public boolean userAuth(String username, String password) throws SQLException {
		Cursor cursor = database.query(true, USER_TABLE, new String[] {"password"}, 
				"username = '" + username +"'", null, null, null, null, null);
		if (cursor != null) {
			cursor.moveToFirst();
			String p=cursor.getString(0);
			if (p.equals(password)) return true;					
		}
		return false;
	}
	
	public boolean delete(String username) {
		return database.delete(USER_TABLE, "username = '" + username + "'", null) > 0;
	}
	
	public ArrayList<CreditSummaryModel> getCreditSummaryModelList() {
		Cursor cursor = database.query(true, CREDIT_TABLE, 
				new String[] {"denomination", "count"}, null, null, null, null, null, null);
		if (cursor == null)
			return null;
		ArrayList<CreditSummaryModel> modelList = new ArrayList<CreditSummaryModel>();
		while (cursor.moveToNext()) {
			CreditSummaryModel model = new CreditSummaryModel(cursor.getInt(0), cursor.getInt(1));
			modelList.add(model);
		}
		return modelList;
	}
	
	public void updateVendorCredit(ArrayList<CreditSummaryModel> modelList) {
		ContentValues values = new ContentValues();
		for (int i=0; i<modelList.size(); i++) {
			values.put("count", modelList.get(i).getCount());
			database.update(CREDIT_TABLE, values, "denomination = " + modelList.get(i).getDenomination(), null);
		}
	}
}
