package com.impsycho.nustalumni;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class RowAdapter extends ArrayAdapter<String>{
	private final Context context;
	private final String[] order_data;
	
	public RowAdapter(Context context, String[] order_data) {
		super(context, R.layout.row_order, order_data);
		this.context = context;
		this.order_data = order_data;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
 
		View rowView;
		rowView = inflater.inflate(R.layout.row_order, parent, false);

		final TextView question = (TextView) rowView.findViewById (R.id.row_question);
		final TextView answer   = (TextView) rowView.findViewById (R.id.row_answer);
		
		question.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				if (answer.getVisibility() == TextView.VISIBLE)
					answer.setVisibility(TextView.GONE);
				else
					answer.setVisibility(TextView.VISIBLE);					
			}
		});
		
		question.setText(order_data[position]);
		answer.setText(ParseValues.faq_answers.get(position));
	
		return rowView;
	}


}