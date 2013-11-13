package com.impsycho.nustalumni;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class PeopleRowAdapter extends ArrayAdapter<String>{
	private final Context context;
	private final String[] order_data;
	private final int pos;
	
	public PeopleRowAdapter(Context context, String[] order_data, int pos) {
		super(context, R.layout.row_person, order_data);
		this.context = context;
		this.order_data = order_data;
		this.pos = pos;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
 
		View rowView;
		rowView = inflater.inflate(R.layout.row_person, parent, false);

		final TextView name       = (TextView) rowView.findViewById (R.id.row_person_name);
		final TextView discipline = (TextView) rowView.findViewById (R.id.row_person_discipline);
		
		name.setText(order_data[position]);
		discipline.setText(ParseValues.people_disciplines.get(pos).get(position));
	
		return rowView;
	}


}