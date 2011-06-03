package org.sharedsolar.adapter;

import java.util.ArrayList;

import org.sharedsolar.model.CreditSummaryModel;
import org.sharedsolar.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class TechAddCreditAdapter extends ArrayAdapter<CreditSummaryModel> {

	private LayoutInflater mInflater;
	private ArrayList<CreditSummaryModel> modelList;
	
	public TechAddCreditAdapter(Context context, int textViewResourceId, ArrayList<CreditSummaryModel> modelList) {
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
			convertView = mInflater.inflate(R.layout.tech_add_credit_item, null);
			holder = new ViewHolder();
			holder.denominationText = (TextView)convertView.findViewById(R.id.techAddCreditDenomination);
			holder.countText = (TextView)convertView.findViewById(R.id.techAddCreditCount);
			holder.addedCountText = (TextView)convertView.findViewById(R.id.techAddCreditAddedCount);
			holder.minusBtn = (Button)convertView.findViewById(R.id.techAddCreditMinusBtn);
			holder.plusBtn = (Button)convertView.findViewById(R.id.techAddCreditPlusBtn);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder)convertView.getTag();
		}
		
		CreditSummaryModel model = modelList.get(position);
		if (model != null) {
			holder.denominationText.setText(String.valueOf(model.getDenomination()));
			holder.countText.setText(String.valueOf(model.getCount()));
			holder.addedCountText.setText("0");
			holder.minusBtn.setEnabled(false);
			holder.minusBtn.setOnClickListener(new OnClickListener()
	        {
				public void onClick(View v) {
					LinearLayout row = (LinearLayout)v.getParent().getParent();
					TextView addedCountText = (TextView)row.getChildAt(2);
					int addedCount = Integer.parseInt(addedCountText.getText().toString());
					addedCount--;
					addedCountText.setText(String.valueOf(addedCount));
					// disable minusBtn
					if (addedCount <= 0) v.setEnabled(false);
				}
	        });
			holder.plusBtn.setOnClickListener(new OnClickListener()
	        {
				public void onClick(View v) {
					LinearLayout row = (LinearLayout)v.getParent().getParent();
					TextView addedCountText = (TextView)row.getChildAt(2);
					int addedCount = Integer.parseInt(addedCountText.getText().toString());
					// enable minusBtn
					Button minusBtn = (Button)(((LinearLayout)v.getParent()).getChildAt(0));
					if (addedCount == 0) minusBtn.setEnabled(true);
					
					addedCount++;
					addedCountText.setText(String.valueOf(addedCount));
				}
	        });
		}
		return convertView;
	}
	
	static class ViewHolder {
		TextView denominationText;
		TextView countText;
		TextView addedCountText;
		Button minusBtn;
		Button plusBtn;
	}
}
