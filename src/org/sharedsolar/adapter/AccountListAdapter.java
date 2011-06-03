package org.sharedsolar.adapter;

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
			holder.aidText = (TextView)convertView.findViewById(R.id.accountListAid);
			holder.crText = (TextView)convertView.findViewById(R.id.accountListCr);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder)convertView.getTag();
		}
		
		AccountListModel item = modelList.get(position);
		if (item != null) {
			holder.aidText.setText(item.getAid());
			holder.crText.setText(String.valueOf(item.getCr()));
		}
		return convertView;
	}
	
	static class ViewHolder {
		TextView aidText;
		TextView crText;
	}
}
