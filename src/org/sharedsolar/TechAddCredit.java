package org.sharedsolar;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.sharedsolar.R;
import org.sharedsolar.adapter.TechAddCreditAdapter;
import org.sharedsolar.db.DatabaseAdapter;
import org.sharedsolar.model.CreditSummaryModel;
import org.sharedsolar.tool.Connector;
import org.sharedsolar.tool.MyUI;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class TechAddCredit extends ListActivity {

	private ArrayList<CreditSummaryModel> modelList;
	private ArrayList<CreditSummaryModel> newModelList;
	private ArrayList<CreditSummaryModel> diffModelList;
	private TechAddCreditAdapter techAddCreditAdapter;
	private DatabaseAdapter dbAdapter;
	private ProgressDialog progressDialog;
	private String info;
	private String jsonString;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tech_add_credit);

		// get model list from db
		dbAdapter = new DatabaseAdapter(this);
		dbAdapter.open();
		modelList = dbAdapter.getCreditSummaryModelList();
		dbAdapter.close();

		// list adapter
		techAddCreditAdapter = new TechAddCreditAdapter(this,
				R.layout.tech_add_credit_item, modelList,
				(TextView) findViewById(R.id.techAddCreditAddedTV),
				(Button) findViewById(R.id.techAddCreditSubmitBtn));
		setListAdapter(techAddCreditAdapter);

		// submit
		Button submitBtn = (Button) findViewById(R.id.techAddCreditSubmitBtn);
		submitBtn.setOnClickListener(submitBtnClickListener);
		submitBtn.setEnabled(false);
	}

	View.OnClickListener submitBtnClickListener = new OnClickListener() {
		public void onClick(View view) {
			info = "";
			// build new model list
			ListView list = getListView();
			newModelList = new ArrayList<CreditSummaryModel>();
			diffModelList = new ArrayList<CreditSummaryModel>();
			for (int i = 0; i < list.getChildCount(); i++) {
				LinearLayout row = (LinearLayout) list.getChildAt(i);
				TextView denominationTV = (TextView) row.getChildAt(1);
				TextView addedCountTV = (TextView) row.getChildAt(2);
				TextView ownCountTV = (TextView) row.getChildAt(3);
				int denomination = Integer.parseInt(denominationTV.getText()
						.toString());
				int addedCount = Integer.parseInt(addedCountTV.getText()
						.toString());
				int ownCount = Integer
						.parseInt(ownCountTV.getText().toString());
				if (addedCount != 0) {
					newModelList.add(new CreditSummaryModel(denomination,
							ownCount));
					diffModelList.add(new CreditSummaryModel(denomination,
							addedCount));
					info += getString(R.string.denomination) + " "
							+ denomination + ": " + addedCount + " "
							+ getString(R.string.added) + "\n";
				}
			}
			// update info string
			String creditAdded = ((TextView) TechAddCredit.this
					.findViewById(R.id.techAddCreditAddedTV)).getText()
					.toString();
			int newCr = Integer.parseInt(creditAdded);
			if (newCr > 0) {
				info += "\n" + getString(R.string.creditAddedLabel) + " "
						+ creditAdded;
				// dialog
				String message = info + "\n\n"
						+ getString(R.string.addCreditConfirm);
				MyUI.showlDialog(view.getContext(), R.string.addCredit,
						message, R.string.yes, R.string.no,
						submitDialoguePositiveClickListener);
			}
		}
	};

	DialogInterface.OnClickListener submitDialoguePositiveClickListener = new DialogInterface.OnClickListener() {
		public void onClick(DialogInterface dialog, int which) {
			dialog.cancel();
			progressDialog = ProgressDialog.show(TechAddCredit.this, "",
					getString(R.string.downloadingTokens));
			new Thread() {
				public void run() {
					Connector connector = new Connector(TechAddCredit.this);
					jsonString = connector.requestToken(
							getString(R.string.requestTokenUrl),
							TechAddCredit.this, diffModelList);
					handler.sendEmptyMessage(0);
				}
			}.start();
		}
	};

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			progressDialog.dismiss();
			jsonString = "dd";
			if (jsonString == null) {
				MyUI.showNeutralDialog(TechAddCredit.this,
						R.string.downloadError,
						R.string.downloadTokensErrorMsg, R.string.ok);
				return;
			}
			Log.d("d", jsonString);

			dbAdapter.open();
			try {
				JSONObject json = new JSONObject(jsonString);
				JSONArray arr = json.getJSONArray("token");

				for (int i = 0; i < arr.length(); i++) {
					JSONObject ele = arr.getJSONObject(i);
					dbAdapter.insertTokenAtVendor(ele.getInt("token_id"),
							ele.getInt("denomination"));
				}
			} catch (JSONException e) {
				MyUI.showNeutralDialog(TechAddCredit.this,
						R.string.invalidTokens, R.string.invalidTokensMsg,
						R.string.ok);
				dbAdapter.close();
				return;
			}

			dbAdapter.updateVendorCredit(newModelList);
			dbAdapter.close();
			Intent intent = new Intent(TechAddCredit.this,
					TechAddCreditReceipt.class);
			intent.putExtra("info", info);
			startActivity(intent);
		}
	};
}