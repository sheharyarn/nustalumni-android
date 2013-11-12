package com.impsycho.nustalumni;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class PeopleFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	View daView = inflater.inflate(R.layout.fragment_people_pager, container, false);
    	
    	return daView;
    }
    
}
 