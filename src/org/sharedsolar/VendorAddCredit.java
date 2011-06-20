package org.sharedsolar;

import java.util.ArrayList;

import org.sharedsolar.R;
import org.sharedsolar.adapter.VendorAddCreditAdapter;
import org.sharedsolar.db.DatabaseAdapter;
import org.sharedsolar.model.AccountModel;
import org.sharedsolar.model.CreditSummaryModel;
import org.sharedsolar.tool.Connector;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class VendorAddCredit extends ListActivity {

	private ArrayList<CreditSummaryModel> modelList;
	private ArrayList<CreditSummaryModel> newModelList;
	private AccountModel accountModel;
	private VendorAddCreditAdapter vendorAddCreditAdapter;
	private DatabaseAdapter dbAdapter = new DatabaseAdapter(this);
	private String info;
	private int newCr;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.vendor_add_credit);

		// get account model from extras
		Bundle extras = getIntent().getExtras();
		accountModel = new AccountModel(extras.getString("aid"),
				extras.getString("cid"),
				(int) (extras.getDouble("cr") * 100));
		((TextView)findViewById(R.id.vendorAddCreditAid)).setText(accountModel.getAid());
		
		// get model list from db
		dbAdapter.open();
		modelList = dbAdapter.getCreditSummaryModelList();
		dbAdapter.close();

		// list adapter
		vendorAddCreditAdapter = new VendorAddCreditAdapter(this,
				R.layout.vendor_add_credit_item, modelList,
				(TextView) findViewById(R.id.vendorAddCreditAddedTV), 
				(Button) findViewById(R.id.vendorAddCreditSubmitBtn));
		setListAdapter(vendorAddCreditAdapter);
		
		// submit
		Button submitBtn = (Button)findViewById(R.id.vendorAddCreditSubmitBtn); 
		submitBtn.setOnClickListener(submitBtnClickListener);
		submitBtn.setEnabled(false);
	}
	
	View.OnClickListener submitBtnClickListener = new View.OnClickListener() {
		public void onClick(View v) {
			info=getString(R.string.accountLabel) + " " + accountModel.getAid() + "\n\n";
			// build new model list
        	ListView list = getListView();
        	newModelList = new ArrayList<CreditSummaryModel>();
			for (int i=0; i<list.getChildCount(); i++) {
	            LinearLayout row = (LinearLayout)list.getChildAt(i);
	            TextView denominationTV = (TextView)row.getChildAt(1);
				TextView addedCountTV = (TextView)row.getChildAt(2);
				TextView remainCountTV = (TextView)row.getChildAt(3);
				int denomination = Integer.parseInt(denominationTV.getText().toString());
				int addedCount = Integer.parseInt(addedCountTV.getText().toString());
				int remainCount = Integer.parseInt(remainCountTV.getText().toString());
				if (addedCount != 0) {
					newModelList.add(new CreditSummaryModel(denomination, remainCount));
					info += getString(R.string.denomination) + " " + denomination + ": " 
						+ addedCount + " " + getString(R.string.added) + "\n";
				}
			}			
			// update info string
			String creditAdded = ((TextView)VendorAddCredit.this.findViewById(R.id.vendorAddCreditAddedTV)).getText().toString();
			newCr = Integer.parseInt(creditAdded);
			if (newCr > 0)
			{
				info += "\n" + getString(R.string.creditAdded) + " " +creditAdded;
				// dialog
				AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
		        builder.setMessage(info + "\n\n" + getString(R.string.addCreditConfirm));
		        builder.setTitle(getString(R.string.addCredit));
		        builder.setPositiveButton(getString(R.string.yes), submitDialoguePositiveClickListener);
		        builder.setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
		            public void onClick(DialogInterface dialog, int id) {
		                dialog.cancel();
		            }
		        });
		        builder.show();
			}
		}
	};
	
	DialogInterface.OnClickListener submitDialoguePositiveClickListener = new DialogInterface.OnClickListener() {
		public void onClick(DialogInterface dialog, int id) {
        	dialog.cancel();
        	// http post
			Connector connector = new Connector(VendorAddCredit.this);
			int status = connector.vendorAddCredit(getString(R.string.addCreditUrl), 
					VendorAddCredit.this,
					accountModel.getAid(),
					accountModel.getCid(),
					newCr);
        	if (status == Connector.CONNECTION_SUCCESS) {
            	// update db
            	dbAdapter.open();
    			dbAdapter.updateVendorCredit(newModelList);
    			dbAdapter.close();
        		Intent intent = new Intent(VendorAddCredit.this, VendorAddCreditReceipt.class);
				intent.putExtra("info", info);
                startActivity(intent);
        	} else {
        		AlertDialog.Builder builder = new AlertDialog.Builder(VendorAddCredit.this);
        		builder.setTitle(getString(R.string.addCredit));
	            if (status == Connector.CONNECTION_TIMEOUT) {
	            	builder.setMessage(getString(R.string.addCreditTimeout));
	            }
	            else {
	            	builder.setMessage(getString(R.string.addCreditError));
	            }
	            builder.setNeutralButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
	                public void onClick(DialogInterface dialog, int id) {
	                    dialog.cancel();
	                }
	            });
	            builder.show();
        	}
			
        }
	};
}