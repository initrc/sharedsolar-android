package org.sharedsolar;

import java.util.ArrayList;
import java.util.Collections;

import org.sharedsolar.adapter.TokenListAdapter;
import org.sharedsolar.db.DatabaseAdapter;
import org.sharedsolar.model.TokenModel;
import org.sharedsolar.model.TokenModelComparator;

import android.app.ListActivity;
import android.os.Bundle;

public class CreditHistory extends ListActivity {

	private ArrayList<TokenModel> modelList;
	private TokenListAdapter tokenListAdapter;
	private DatabaseAdapter dbAdapter;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.token_history_list);

		updateList();
	}

	public void updateList() {
		dbAdapter = new DatabaseAdapter(this);
		dbAdapter.open();
		modelList = dbAdapter.getSoldTokens();
		
		Collections.sort(modelList, new TokenModelComparator());
		tokenListAdapter = new TokenListAdapter(this,
				R.layout.token_history_list_item, modelList);
		setListAdapter(tokenListAdapter);
	}
}
