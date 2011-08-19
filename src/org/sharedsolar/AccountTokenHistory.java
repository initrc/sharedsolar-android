package org.sharedsolar;

import java.util.ArrayList;
import java.util.Collections;

import org.sharedsolar.adapter.AccountTokenListAdapter;
import org.sharedsolar.db.DatabaseAdapter;
import org.sharedsolar.model.TokenModel;
import org.sharedsolar.model.TokenModelComparator;

import android.app.ListActivity;
import android.os.Bundle;
import android.widget.TextView;

public class AccountTokenHistory extends ListActivity {

	private ArrayList<TokenModel> modelList;
	private AccountTokenListAdapter accountTokenListAdapter;
	private DatabaseAdapter dbAdapter;
	private String aid;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.account_token_history_list);

		Bundle extras = getIntent().getExtras();
		aid = extras.getString("aid");
		((TextView) findViewById(R.id.accountTokenHistoryListAid)).setText(aid);
		updateList();
	}

	public void updateList() {
		dbAdapter = new DatabaseAdapter(this);
		dbAdapter.open();
		modelList = dbAdapter.getSoldTokensByAccount(aid);
		
		Collections.sort(modelList, new TokenModelComparator());
		accountTokenListAdapter = new AccountTokenListAdapter(this,
				R.layout.account_token_history_list_item, modelList);
		setListAdapter(accountTokenListAdapter);
	}
}
