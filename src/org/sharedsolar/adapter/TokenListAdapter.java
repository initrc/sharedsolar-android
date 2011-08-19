package org.sharedsolar.adapter;

import java.util.ArrayList;

import org.sharedsolar.model.TokenModel;
import org.sharedsolar.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class TokenListAdapter extends ArrayAdapter<TokenModel>{

	private LayoutInflater mInflater;
	private ArrayList<TokenModel> modelList;
	
	public TokenListAdapter(Context context, int textViewResourceId, ArrayList<TokenModel> modelList) {
		super(context, textViewResourceId);
		mInflater = LayoutInflater.from(context);
		this.modelList = modelList;
	}
	
	public int getCount() {
		return modelList.size();
	}
	
	public TokenModel getItem(int position) {
		return modelList.get(position);
	}
	
	public long getItemId(int position) {
		return position;
	}
	
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.token_history_list_item, null);
			holder = new ViewHolder();
			holder.timestampText = (TextView)convertView.findViewById(R.id.tokenListTimestamp);
			holder.aidText = (TextView)convertView.findViewById(R.id.tokenListAid);
			holder.denominationText = (TextView)convertView.findViewById(R.id.tokenListDenomination);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder)convertView.getTag();
		}
		
		TokenModel model = modelList.get(position);
		if (model != null) {
			holder.timestampText.setText(model.getLocalTimestampString());
			holder.aidText.setText(model.getAid());
			holder.denominationText.setText(String.valueOf(model.getDenomination()));
		}
		
		return convertView;
	}
	
	public static class ViewHolder {
		TextView timestampText;
		TextView aidText;
		TextView denominationText;
	}
}
