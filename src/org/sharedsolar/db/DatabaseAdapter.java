package org.sharedsolar.db;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;
import org.sharedsolar.R;
import org.sharedsolar.model.CreditSummaryModel;
import org.sharedsolar.tool.Device;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class DatabaseAdapter {
	
	private Context context;
	private SQLiteDatabase database;
	private DatabaseHelper databaseHelper;
	
	private final String USER_TABLE = DatabaseHelper.USER_TABLE;
	private final String TOKEN_TABLE = DatabaseHelper.TOKEN_TABLE;
	
	private final int TOKEN_STATE_AT_VENDOR;
	private final int TOKEN_STATE_AT_METER;
	
	public DatabaseAdapter(Context context) {
		this.context = context;
		TOKEN_STATE_AT_VENDOR = Integer.parseInt(context.getString(R.string.tokenStateAtVendor).toString());
		TOKEN_STATE_AT_METER = Integer.parseInt(context.getString(R.string.tokenStateAtMeter).toString());
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

	private ContentValues createToken(int tokenId, int denomination, 
			int state, String accountId) {
		ContentValues values = new ContentValues();
		values.put("token_id", tokenId);
		values.put("denomination", denomination);
		values.put("state", state);
		values.put("account_id", accountId);
		return values;
	}
	
	public void init() {
		if (isEmpty()) {
			// insert USER
			ContentValues values = createUser("tech", "");
			database.insert(USER_TABLE, null, values);
		}		
	}
	
	public boolean isEmpty() throws SQLException {
		Cursor cursor = database.query(USER_TABLE, new String[] {"password"}, 
				null, null, null, null, null);
		if (cursor == null || cursor.getCount() == 0) return true;
		return false;
	}
	
	public boolean userAuth(String username, String password) throws SQLException {
		Cursor cursor = database.query(USER_TABLE, new String[] {"password"}, 
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
		ArrayList<CreditSummaryModel> modelList = new ArrayList<CreditSummaryModel>();
		String[] denominationValues = context.getResources().getStringArray(R.array.denominationValues);
		for (String s : denominationValues) {
			int denomination = Integer.parseInt(s);
			CreditSummaryModel model = new CreditSummaryModel(denomination, 
					getTokenCountAtVendor(denomination));
			modelList.add(model);
		}
		return modelList;
	}

	// insert token
	public void insertToken(int tokenId, int denomination, 
			int state, String accountId) {
		ContentValues values = createToken(tokenId, denomination, state, accountId);
		database.insert(TOKEN_TABLE, null, values);		
	}

	public void insertTokenAtVendor(int tokenId, int denomination) {
		insertToken(tokenId, denomination, TOKEN_STATE_AT_VENDOR, null);
	}
	
	public void insertTokenAtMeter(int tokenId, int denomination, String accountId) {
		insertToken(tokenId, denomination, TOKEN_STATE_AT_METER, accountId);
	}
	
	// get token count
	public int getTokenCount(int state, int denomination) {
		Cursor cursor = database.rawQuery("select count(*) from " + TOKEN_TABLE + " where state = "
				+ state + " and denomination = " + denomination, null);
		if (cursor == null) return 0;
		cursor.moveToFirst();
		return cursor.getInt(0);
	}
	
	public int getTokenCountAtVendor(int denomination) {
		return getTokenCount(TOKEN_STATE_AT_VENDOR, denomination);
	}
	
	public int getTokenCountAtMeter(int denomination) {
		return getTokenCount(TOKEN_STATE_AT_METER, denomination);
	}
	
	// sell token
	public void sellToken(ArrayList<CreditSummaryModel> modelList) {
		for (CreditSummaryModel model : modelList) {
			int denomination = model.getDenomination();
			int count = model.getCount();
			Log.d("d", String.valueOf(denomination) + " " + String.valueOf(count));
			database.execSQL("update " + TOKEN_TABLE
					+ " set state = " + TOKEN_STATE_AT_METER
					+ ", timestamp = CURRENT_TIMESTAMP"
					+ " where _id in ("
					+ " select _id from " + TOKEN_TABLE
					+ " where denomination = " + denomination
					+ " and state = " + TOKEN_STATE_AT_VENDOR 
					+ " limit " + count + " )");
		}
	}
	
	// get token at meter
	public JSONObject getTokenAtMeter() {
		Cursor cursor = database.query(TOKEN_TABLE, 
				new String[]{"token_id", "account_id", "timestamp"}, 
				"state = " + TOKEN_STATE_AT_METER, null, null, null, null);
		if (cursor == null) return null;
		JSONObject json = new JSONObject();
		try {
			json.put("device_id", Device.getId(context));
			while (cursor.moveToNext()) {
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("token_id", String.valueOf(cursor.getInt(0)));
				map.put("account_id", cursor.getString(1));
				map.put("timestamp", cursor.getString(2));
				json.accumulate("tokens", map);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return json;
	}

	// delete token at meter
	public void deleteTokenAtMeter() { 
		database.execSQL("delete from " + TOKEN_TABLE
				+ " where state = " + TOKEN_STATE_AT_METER); 
	}
}
