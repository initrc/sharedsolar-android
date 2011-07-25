package org.sharedsolar.adapter;

import java.text.DecimalFormat;
import java.util.ArrayList;

import org.sharedsolar.model.AccountModel;
import org.sharedsolar.R;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.ToggleButton;

public class AccountListAdapter extends ArrayAdapter<AccountModel> implements OnClickListener{

	private LayoutInflater mInflater;
	private ArrayList<AccountModel> modelList;
	private Handler statusToggleHandler;
	
	public AccountListAdapter(Context context, int textViewResourceId, ArrayList<AccountModel> modelList, Handler statusToggleHandler) {
		super(context, textViewResourceId);
		mInflater = LayoutInflater.from(context);
		this.modelList = modelList;
		this.statusToggleHandler = statusToggleHandler;
	}
	
	public int getCount() {
		return modelList.size();
	}
	
	public AccountModel getItem(int position) {
		return modelList.get(position);
	}
	
	public long getItemId(int position) {
		return position;
	}
	
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.account_list_item, null);
			holder = new ViewHolder();
			holder.statusBtn = (ToggleButton)convertView.findViewById(R.id.accountStatus);
			holder.aidText = (TextView)convertView.findViewById(R.id.accountListAid);
			holder.crText = (TextView)convertView.findViewById(R.id.accountListCr);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder)convertView.getTag();
		}
		holder.statusBtn.setTag(position);
		holder.statusBtn.setOnClickListener(this);
		
		AccountModel model = modelList.get(position);
		if (model != null) {
			holder.statusBtn.setChecked(model.getStatus());
			holder.aidText.setText(model.getAid());
			DecimalFormat df = new DecimalFormat("#0.00");
			holder.crText.setText(df.format(model.getCr()/100.00));
		}
		
		return convertView;
	}
	
	public void onClick(View view) {
		ToggleButton btn = (ToggleButton) view;
		int msgWhat = (Integer) btn.getTag() * 2;
		msgWhat += btn.isChecked() ? 1 : 0;
		Message msg = new Message();
		msg.obj = btn;
		msg.what = msgWhat;
		// set checked after the confirm dialog
		btn.setChecked(!btn.isChecked());
		statusToggleHandler.sendMessage(msg);
	}
	
	public static class ViewHolder {
		public ToggleButton statusBtn;
		TextView aidText;
		TextView crText;
	}
}
