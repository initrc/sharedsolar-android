package org.sharedsolar;

import java.util.ArrayList;

import org.sharedsolar.R;
import org.sharedsolar.adapter.CreditRepoAdapter;
import org.sharedsolar.db.DatabaseAdapter;
import org.sharedsolar.model.CreditSummaryModel;

import android.app.ListActivity;
import android.os.Bundle;
import android.widget.TextView;

public class CreditRepo extends ListActivity {
	
	private ArrayList<CreditSummaryModel> modelList;
	private CreditRepoAdapter creditRepoAdapter;
	private DatabaseAdapter dbAdapter;
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.credit_repo);
        
        // get model list from db
        dbAdapter = new DatabaseAdapter(this);
        dbAdapter.open();
        modelList = dbAdapter.getCreditSummaryModelList();
        dbAdapter.close();
        
        // list adapter
      	creditRepoAdapter = new CreditRepoAdapter(this, R.layout.credit_repo_item, modelList);			
		setListAdapter(creditRepoAdapter);
		
		// calculate sum
		int sum=0;
		for (CreditSummaryModel model : modelList) {
			sum += model.getDenomination() * model.getCount();
		}
		((TextView)findViewById(R.id.availableCredit)).setText(String.valueOf(sum));
    }
}