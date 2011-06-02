package org.sharedsolar.adapter;

import java.util.ArrayList;

import org.sharedsolar.model.AccountListModel;
import org.sharedsolar.R;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class AccountListAdapter extends ArrayAdapter<AccountListModel> {

	private ArrayList<AccountListModel> items;
	
	public AccountListAdapter(Context context, int textViewResourceId) {
		super(context, textViewResourceId);
	}
	
	public void setItems(ArrayList<AccountListModel> items)
	{
		this.items = items;
	}
	
	@Override
	public View getView(int pos, View convertView, ViewGroup parent) {
		View view = convertView;
		AccountListModel item = items.get(pos);
		if (item != null) {
			((TextView)view.findViewById(R.id.accountListAid)).setText(item.getAid());
			((TextView)view.findViewById(R.id.accountListCredit)).setText(String.valueOf(item.getCr()));
		}
		return view;
	}
}
