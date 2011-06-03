package org.sharedsolar;

import java.util.ArrayList;

import org.sharedsolar.R;
import org.sharedsolar.adapter.TechAddCreditAdapter;
import org.sharedsolar.db.DatabaseAdapter;
import org.sharedsolar.model.CreditSummaryModel;

import android.app.ListActivity;
import android.os.Bundle;
import android.widget.TextView;

public class TechAddCredit extends ListActivity {
	
	private ArrayList<CreditSummaryModel> modelList;
	private TechAddCreditAdapter techAddCreditAdapter;
	private DatabaseAdapter dbAdapter = new DatabaseAdapter(this);
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tech_add_credit);
        
        // get model list from db
        dbAdapter.open();
        modelList = dbAdapter.getCreditSummaryModelList();
        dbAdapter.close();
        
        // list adapter
        techAddCreditAdapter = new TechAddCreditAdapter(this, R.layout.tech_add_credit_item, modelList);			
		setListAdapter(techAddCreditAdapter);
		
		// calculate sum
		int sum=0;
		for (CreditSummaryModel model : modelList) {
			sum += model.getDenomination() * model.getCount();
		}
		((TextView)findViewById(R.id.availableCredit)).setText(String.valueOf(sum));
    }
}