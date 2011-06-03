package org.sharedsolar;

import java.util.ArrayList;

import org.sharedsolar.R;
import org.sharedsolar.adapter.CreditSummaryAdapter;
import org.sharedsolar.db.DatabaseAdapter;
import org.sharedsolar.model.CreditSummaryModel;

import android.app.ListActivity;
import android.os.Bundle;
import android.widget.TextView;

public class TechAddCredit extends ListActivity {
	
	private ArrayList<CreditSummaryModel> modelList;
	private CreditSummaryAdapter creditSummaryAdapter;
	private DatabaseAdapter dbAdapter = new DatabaseAdapter(this);
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tech_add_credit);
        
        // get model list from db
        dbAdapter.open();
        modelList = dbAdapter.getCreditSummaryModelList();
        dbAdapter.close();
        
        // list adapter
      	creditSummaryAdapter = new CreditSummaryAdapter(this, R.layout.credit_summary_item, modelList);			
		setListAdapter(creditSummaryAdapter);
		
		// calculate sum
		int sum=0;
		for (CreditSummaryModel model : modelList) {
			sum += model.getDenomination() * model.getCount();
		}
		((TextView)findViewById(R.id.availableCredit)).setText(String.valueOf(sum));
    }
}