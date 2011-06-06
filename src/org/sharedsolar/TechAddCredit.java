package org.sharedsolar;

import java.util.ArrayList;

import org.sharedsolar.R;
import org.sharedsolar.adapter.TechAddCreditAdapter;
import org.sharedsolar.db.DatabaseAdapter;
import org.sharedsolar.model.CreditSummaryModel;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
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
		techAddCreditAdapter = new TechAddCreditAdapter(this,
				R.layout.tech_add_credit_item, modelList,
				(TextView) findViewById(R.id.creditAddedTV));
		setListAdapter(techAddCreditAdapter);
		
		// submit
		((Button)findViewById(R.id.techAddCreditSubmitBtn)).setOnClickListener(new OnClickListener()
        {
			public void onClick(View v) {
				ListView list = getListView();
				for (int i=0; i<list.getChildCount(); i++) {
					LinearLayout row = (LinearLayout)list.getChildAt(i);
					TextView ownCountTV = (TextView)row.getChildAt(3);
					modelList.get(i).setCount(Integer.parseInt(ownCountTV.getText().toString()));
					dbAdapter.open();
					dbAdapter.updateVendorCredit(modelList);
					dbAdapter.close();
				}
			}
        });
	}
}