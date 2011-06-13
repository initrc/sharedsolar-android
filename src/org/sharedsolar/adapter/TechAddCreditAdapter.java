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
	private TextView creditAddedTV;
	private Button submitBtn;

	public TechAddCreditAdapter(Context context, int textViewResourceId,
			ArrayList<CreditSummaryModel> modelList, TextView creditAddedTV,
			Button submitBtn) {
		super(context, textViewResourceId);
		mInflater = LayoutInflater.from(context);
		this.modelList = modelList;
		this.creditAddedTV = creditAddedTV;
		this.submitBtn = submitBtn;
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
			convertView = mInflater
					.inflate(R.layout.tech_add_credit_item, null);
			holder = new ViewHolder();
			holder.denominationText = (TextView) convertView
					.findViewById(R.id.techAddCreditDenomination);
			holder.totalCountText = (TextView) convertView
					.findViewById(R.id.techAddCreditOwnCount);
			holder.addedCountText = (TextView) convertView
					.findViewById(R.id.techAddCreditAddedCount);
			holder.minusBtn = (Button) convertView
					.findViewById(R.id.techAddCreditMinusBtn);
			holder.plusBtn = (Button) convertView
					.findViewById(R.id.techAddCreditPlusBtn);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		CreditSummaryModel model = modelList.get(position);
		if (model != null) {
			holder.denominationText.setText(String.valueOf(model
					.getDenomination()));
			holder.addedCountText.setText("0");
			holder.totalCountText.setText(String.valueOf(model.getCount()));
			holder.minusBtn.setEnabled(false);
			holder.plusBtn.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					updateCredit(v, 1);
				}
			});
			holder.minusBtn.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					updateCredit(v, -1);
				}
			});
		}
		return convertView;
	}

	public void updateCredit(View v, int sign) {
		LinearLayout row = (LinearLayout) v.getParent().getParent();
		TextView denominationText = (TextView) row.getChildAt(1);
		TextView addedCountText = (TextView) row.getChildAt(2);
		TextView ownCountText = (TextView) row.getChildAt(3);
		int denomination = Integer.parseInt(denominationText.getText()
				.toString());
		int addedCount = Integer.parseInt(addedCountText.getText().toString());
		int ownCount = Integer.parseInt(ownCountText.getText().toString());
		int ownCredit = Integer.parseInt(creditAddedTV.getText().toString());
		// update text
		addedCount += sign;
		addedCountText.setText(String.valueOf(addedCount));
		ownCountText.setText(String.valueOf(ownCount + sign));
		creditAddedTV.setText(String.valueOf(ownCredit + sign * denomination));
		// disable minusBtn
		if (sign == -1 && addedCount <= 0)
			v.setEnabled(false);
		// enable minusBtn
		if (sign == 1 && addedCount >= 1) {
			Button minusBtn = (Button) (((LinearLayout) v.getParent())
					.getChildAt(1));
			minusBtn.setEnabled(true);
		}
		// submitBtn
		ownCredit = Integer.parseInt(creditAddedTV.getText().toString());
		submitBtn.setEnabled(ownCredit > 0);
	}

	static class ViewHolder {
		TextView denominationText;
		TextView addedCountText;
		TextView totalCountText;
		Button minusBtn;
		Button plusBtn;
	}
}
