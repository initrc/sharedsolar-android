package org.sharedsolar.adapter;

import java.util.ArrayList;

import org.sharedsolar.model.CreditSummaryModel;
import org.sharedsolar.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class CreditSummaryAdapter extends ArrayAdapter<CreditSummaryModel> {

	private LayoutInflater mInflater;
	private ArrayList<CreditSummaryModel> modelList;
	
	public CreditSummaryAdapter(Context context, int textViewResourceId, ArrayList<CreditSummaryModel> modelList) {
		super(context, textViewResourceId);
		mInflater = LayoutInflater.from(context);
		this.modelList = modelList;
	}
	
	public int getCount() {
		return modelList.size();
	}
	
	public CreditSummaryModel getItem(int position) {
		return modelList.get(position);
	}
	
	public long getItemId(int position) {
		return position;
	}
	
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.credit_summary_item, null);
			holder = new ViewHolder();
			holder.denominationText = (TextView)convertView.findViewById(R.id.creditSummaryDenomination);
			holder.countText = (TextView)convertView.findViewById(R.id.creditSummaryCount);
			holder.totalText = (TextView)convertView.findViewById(R.id.creditSummaryTotal);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder)convertView.getTag();
		}
		
		CreditSummaryModel model = modelList.get(position);
		if (model != null) {
			holder.denominationText.setText(String.valueOf(model.getDenomination()));
			holder.countText.setText(String.valueOf(model.getCount()));
			holder.totalText.setText(String.valueOf(model.getDenomination() * model.getCount()));
		}
		return convertView;
	}
	
	static class ViewHolder {
		TextView denominationText;
		TextView countText;
		TextView totalText;
	}
}
