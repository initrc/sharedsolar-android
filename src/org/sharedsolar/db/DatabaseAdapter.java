package org.sharedsolar.db;

import java.util.ArrayList;

import org.json.JSONArray;
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

public class DatabaseAdapter {
	
	private Context context;
	private SQLiteDatabase database;
	private DatabaseHelper databaseHelper;
	
	private final String USER_TABLE = DatabaseHelper.USER_TABLE;
	private final String TOKEN_TABLE = DatabaseHelper.TOKEN_TABLE;
	
	private final int TOKEN_STATE_AT_VENDOR;
	private final int TOKEN_STATE_AT_METER;
	private final int TOKEN_STATE_EXPIRED;
	
	public DatabaseAdapter(Context context) {
		this.context = context;
		TOKEN_STATE_AT_VENDOR = Integer.parseInt(context.getString(R.string.tokenStateAtVendor).toString());
		TOKEN_STATE_AT_METER = Integer.parseInt(context.getString(R.string.tokenStateAtMeter).toString());
		TOKEN_STATE_EXPIRED = Integer.parseInt(context.getString(R.string.tokenStateExpired).toString());
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

	private ContentValues createToken(long tokenId, int denomination, 
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
	public void insertToken(long tokenId, int denomination, 
			int state, String accountId) {
		ContentValues values = createToken(tokenId, denomination, state, accountId);
		database.insert(TOKEN_TABLE, null, values);		
	}

	public void insertTokenAtVendor(long tokenId, int denomination) {
		insertToken(tokenId, denomination, TOKEN_STATE_AT_VENDOR, null);
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
	public void sellToken(ArrayList<CreditSummaryModel> modelList, String accountId) {
		for (CreditSummaryModel model : modelList) {
			int denomination = model.getDenomination();
			int count = model.getCount();			
			database.execSQL("update " + TOKEN_TABLE
					+ " set state = " + TOKEN_STATE_AT_METER
					+ ", account_id = '" + accountId + "'"
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
		JSONArray tokenArray = new JSONArray();
		try {
			json.put("device_id", Device.getId(context));
			while (cursor.moveToNext()) {
				JSONObject tokenObject = new JSONObject();
				tokenObject.put("token_id", String.valueOf(cursor.getLong(0)));
				tokenObject.put("account_id", cursor.getString(1));
				tokenObject.put("timestamp", cursor.getString(2));
				tokenArray.put(tokenObject);
			}
			json.put("tokens", tokenArray);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return json;
	}

	// delete token at meter
	public void expireTokenAtMeter() { 
		database.execSQL("update " + TOKEN_TABLE
				+ " set state = " + TOKEN_STATE_EXPIRED
				+ " where state = " + TOKEN_STATE_AT_METER); 
	}
}
