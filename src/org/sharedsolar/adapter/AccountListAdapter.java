package org.sharedsolar.adapter;

import java.text.DecimalFormat;
import java.util.ArrayList;

import org.sharedsolar.model.AccountListModel;
import org.sharedsolar.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class AccountListAdapter extends ArrayAdapter<AccountListModel> {

	private LayoutInflater mInflater;
	private ArrayList<AccountListModel> modelList;
	
	public AccountListAdapter(Context context, int textViewResourceId, ArrayList<AccountListModel> modelList) {
		super(context, textViewResourceId);
		mInflater = LayoutInflater.from(context);
		this.modelList = modelList;
	}
	
	public int getCount() {
		return modelList.size();
	}
	
	public AccountListModel getItem(int position) {
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
			holder.idText = (TextView)convertView.findViewById(R.id.accountListNo);
			holder.aidText = (TextView)convertView.findViewById(R.id.accountListAid);
			holder.crText = (TextView)convertView.findViewById(R.id.accountListCr);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder)convertView.getTag();
		}
		
		AccountListModel model = modelList.get(position);
		if (model != null) {
			holder.idText.setText(String.valueOf(position + 1));
			holder.aidText.setText(model.getAid());
			DecimalFormat df = new DecimalFormat("#0.00");
			holder.crText.setText(df.format(model.getCr()/100.00));
		}
		return convertView;
	}
	
	static class ViewHolder {
		TextView idText;
		TextView aidText;
		TextView crText;
	}
}
