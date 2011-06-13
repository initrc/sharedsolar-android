package org.sharedsolar;

import java.util.ArrayList;

import org.sharedsolar.R;
import org.sharedsolar.adapter.TechAddCreditAdapter;
import org.sharedsolar.db.DatabaseAdapter;
import org.sharedsolar.model.CreditSummaryModel;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class TechAddCredit extends ListActivity {

	private ArrayList<CreditSummaryModel> modelList;
	private ArrayList<CreditSummaryModel> newModelList;
	private TechAddCreditAdapter techAddCreditAdapter;
	private DatabaseAdapter dbAdapter = new DatabaseAdapter(this);
	private String info;

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
				(TextView) findViewById(R.id.techAddCreditAddedTV));
		setListAdapter(techAddCreditAdapter);
		
		// submit
		((Button)findViewById(R.id.techAddCreditSubmitBtn)).setOnClickListener(submitBtnClickListener);
	}
	
	View.OnClickListener submitBtnClickListener = new OnClickListener() {
		public void onClick(View v) {
			info="";
			// build new model list
        	ListView list = getListView();
        	newModelList = new ArrayList<CreditSummaryModel>();
			for (int i=0; i<list.getChildCount(); i++) {
	            LinearLayout row = (LinearLayout)list.getChildAt(i);
	            TextView denominationTV = (TextView)row.getChildAt(1);
				TextView addedCountTV = (TextView)row.getChildAt(2);
				TextView ownCountTV = (TextView)row.getChildAt(3);
				int denomination = Integer.parseInt(denominationTV.getText().toString());
				int addedCount = Integer.parseInt(addedCountTV.getText().toString());
				int ownCount = Integer.parseInt(ownCountTV.getText().toString());
				if (addedCount != 0) {
					newModelList.add(new CreditSummaryModel(denomination, ownCount));
					info += getString(R.string.denomination) + " " + denomination +": " + addedCount+" Added\n";
				}
			}			
			// update info string
			String creditAdded = ((TextView)TechAddCredit.this.findViewById(R.id.techAddCreditAddedTV)).getText().toString();
			int newCr = Integer.parseInt(creditAdded);
			if (newCr > 0) {
				info += "\n" + getString(R.string.creditAdded) + " " +creditAdded;
				// dialog
				AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
		        builder.setMessage(info + "\n\n" + getString(R.string.addCreditConfirm));
		        builder.setTitle(getString(R.string.addCredit));
		        builder.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
		            public void onClick(DialogInterface dialog, int id) {
		            	dbAdapter.open();
		    			dbAdapter.updateVendorCredit(newModelList);
		    			dbAdapter.close();
		            	dialog.cancel();    	
						Intent intent = new Intent(TechAddCredit.this, TechAddCreditReceipt.class);
						intent.putExtra("info", info);
		                startActivity(intent);
		            }
		        });
		        builder.setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
		            public void onClick(DialogInterface dialog, int id) {
		                dialog.cancel();
		            }
		        });
		        builder.show();
			}
		}
	};
}