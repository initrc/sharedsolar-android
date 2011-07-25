package org.sharedsolar;

import java.util.ArrayList;

import java.util.Collections;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.sharedsolar.R;
import org.sharedsolar.adapter.AccountListAdapter;
import org.sharedsolar.model.AccountModel;
import org.sharedsolar.model.AccountModelComparator;
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
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ToggleButton;

public class AccountList extends ListActivity {

	private ArrayList<AccountModel> modelList;
	private AccountListAdapter accountListAdapter;
	private ProgressDialog progressDialog;
	private String jsonString;
	private String aidToSwitch;
	private boolean aidSwitchStatus;
	private ToggleButton toggleBtn;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.account_list);

		updateList();

		((LinearLayout) findViewById(R.id.accountRefreshLayout))
				.setOnClickListener(new OnClickListener() {
					public void onClick(View view) {
						progressDialog = ProgressDialog.show(view.getContext(),
								"", getString(R.string.loading));
						new Thread() {
							public void run() {
								jsonString = new Connector(AccountList.this)
										.requestForString(
												getString(R.string.accountListUrl),
												AccountList.this);
								accountListHandler.sendEmptyMessage(0);
							}
						}.start();
					}
				});
	}

	public void updateList() {
		modelList = new ArrayList<AccountModel>();
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			String jsonString = extras.getString("accountList");
			Log.d("d", "AccountList: " + jsonString);
			try {
				JSONArray jsonArray = new JSONArray(jsonString);
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject jsonObject = (JSONObject) jsonArray.get(i);
					AccountModel model = new AccountModel(
							jsonObject.getString("aid"),
							jsonObject.getString("cid"),
							(int) (jsonObject.getDouble("cr") * 100),
							jsonObject.getBoolean("status"));
					modelList.add(model);
				}
			} catch (JSONException e) {
				MyUI.showNeutralDialog(this, R.string.invalidAccountList,
						R.string.invalidAccountListMsg, R.string.ok);
				return;
			}
			// sort by aid
			Collections.sort(modelList, new AccountModelComparator());
			accountListAdapter = new AccountListAdapter(this,
					R.layout.account_list_item, modelList, circuitSwitchHandler);
			setListAdapter(accountListAdapter);
			getListView().setOnItemClickListener(itemClickListener);
		}
	}

	OnItemClickListener itemClickListener = new OnItemClickListener() {
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			AccountModel model = (AccountModel) (getListView()
					.getItemAtPosition(position));
			if (model.getAid().equals("")) {
				MyUI.showNeutralDialog(
						view.getContext(),
						R.string.accountList,
						getString(R.string.accountIdEmptyMsg) + " "
								+ model.getCid(), R.string.ok);
			} else {
				Intent intent = new Intent(view.getContext(),
						VendorAddCredit.class);
				intent.putExtra("aid", model.getAid());
				intent.putExtra("cid", model.getCid());
				intent.putExtra("cr", model.getCr());
				startActivity(intent);
			}
		}
	};

	private Handler accountListHandler = new Handler() {
		public void handleMessage(Message msg) {
			progressDialog.dismiss();
			if (jsonString != null) {
				if (jsonString.equals(String
						.valueOf(Connector.CONNECTION_TIMEOUT))) {
					MyUI.showNeutralDialog(AccountList.this,
							R.string.accountList,
							R.string.accountListTimeoutMsg, R.string.ok);
				} else {
					getIntent().putExtra("accountList", jsonString);
					updateList();
				}
			} else {
				MyUI.showNeutralDialog(AccountList.this, R.string.accountList,
						R.string.loadingAccountListError, R.string.ok);
			}
		}
	};

	private Handler circuitSwitchHandler = new Handler() {
		public void handleMessage(Message msg) {
			int index = msg.what / 2;
			AccountModel accountModel = modelList.get(index);
			aidSwitchStatus = msg.what % 2 != 0;
			aidToSwitch = accountModel.getAid();
			toggleBtn = (ToggleButton) msg.obj;

			String message = getString(R.string.switchConfirm) + " "
					+ accountModel.getAid() + " "
					+ getString(aidSwitchStatus ? R.string.on : R.string.off) + "?";
			MyUI.showlDialog(AccountList.this, R.string.circuitSwitch, message,
					R.string.yes, R.string.no,
					switchDialoguePositiveClickListener);
		}
	};

	DialogInterface.OnClickListener switchDialoguePositiveClickListener = new DialogInterface.OnClickListener() {
		public void onClick(DialogInterface dialog, int id) {
			dialog.cancel();

			progressDialog = ProgressDialog.show(AccountList.this, "",
					getString(R.string.switchingCircuit));
			new Thread() {
				public void run() {
					Connector connector = new Connector(AccountList.this);
					int status = connector.vendorToggleStatus(
							getString(R.string.toggleStatusUrl),
							AccountList.this, aidToSwitch, aidSwitchStatus);
					circuitSwitchDoneHandler.sendEmptyMessage(status);
				}
			}.start();
		}
	};

	private Handler circuitSwitchDoneHandler = new Handler() {
		public void handleMessage(Message msg) {
			progressDialog.dismiss();

			int status = msg.what;
			int message;
			if (status == Connector.CONNECTION_SUCCESS) {
				toggleBtn.setChecked(!toggleBtn.isChecked());
			} else {
				if (status == Connector.CONNECTION_TIMEOUT) {
					message = R.string.addCreditTimeout;
				} else {
					message = R.string.addCreditError;
				}
				MyUI.showNeutralDialog(AccountList.this, R.string.circuitSwitch,
						message, R.string.ok);
			}
		}
	};
}