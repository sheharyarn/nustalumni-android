package com.impsycho.nustalumni;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerTitleStrip;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.loopj.android.http.JsonHttpResponseHandler;


public class PeopleFragment extends Fragment {
    PeoplePagerAdapter peopleAdapter;
    ViewPager mViewPager;
    PagerTitleStrip titlePager;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	View daView = inflater.inflate(R.layout.fragment_people_pager, container, false);

    	peopleAdapter = new PeoplePagerAdapter(getChildFragmentManager());
    	titlePager = (PagerTitleStrip)daView.findViewById(R.id.pager_title_strip);
    	titlePager.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 12);
    	
        mViewPager = (ViewPager) daView.findViewById(R.id.pager);
        mViewPager.setAdapter(peopleAdapter);
        
        ParsePeopleData();
        
        return daView;
    }

    public void ParsePeopleData() {
    	if (!ParseValues.hasPeople)
    		ForceParsePeopleData();
    }
    
    public void ForceParsePeopleData() {
    	APIclient.get("/data/people/", null, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(JSONObject response) {
	        	try {
        			ParseValues.people_names       = new ArrayList<ArrayList<String>>();
        			ParseValues.people_emails      = new ArrayList<ArrayList<String>>();
        			ParseValues.people_disciplines = new ArrayList<ArrayList<String>>();

        			JSONArray people[] = new JSONArray[3];
        			
        			people[0] = response.getJSONArray("type_0");
        			people[1] = response.getJSONArray("type_1");
        			people[2] = response.getJSONArray("type_2");
        			
        			for (int pi=0; pi<3; pi++) {
        				ParseValues.people_names.add(new ArrayList<String>());
        				ParseValues.people_emails.add(new ArrayList<String>());
        				ParseValues.people_disciplines.add(new ArrayList<String>());
        				
		        		for (int i=0; i<people[pi].length(); i++) {
		        			JSONObject item = people[pi].getJSONObject(i);
		        			ParseValues.people_names.get(pi).add(item.getString("name"));
		        			ParseValues.people_emails.get(pi).add(item.getString("email"));
		        			ParseValues.people_disciplines.get(pi).add(item.getString("discipline"));
		        		}
        			}

    				ParseValues.hasPeople = true;
    		        mViewPager.setAdapter(peopleAdapter);
	        	} catch (JSONException e1) { e1.printStackTrace(); }
			}
			
			@Override
			public void onFailure(Throwable e, JSONObject response) {
	        	try {
	    			ErrorAlert(LoginActivity.Capitalize(response.getString("error")));
	        	} catch (JSONException e1) { e1.printStackTrace(); }
			}
		});
    }

    public static class PeoplePagerAdapter extends FragmentPagerAdapter {
        public PeoplePagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public Fragment getItem(int i) {
        	Fragment fragment = new PeopleSectionFragment();
            Bundle args = new Bundle();
            args.putInt(PeopleSectionFragment.ARG_POSITION, i);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public int getCount() { return 3;}

        @Override
        public CharSequence getPageTitle(int position) {
        	switch (position) {
        		case 0:
        			return "SEEKING JOBS";
        		case 1:
        			return "SEEKING EMPLOYEES";
        		case 2:
        			return "SEEKING CONNECTIONS";
        	}
            return "OTHER";
        }
    }

    public static class PeopleSectionFragment extends Fragment {
        public static final String ARG_POSITION = "position_object";
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_people_section, container, false);
            int pos = getArguments().getInt(ARG_POSITION);
            
        	ListView peoplelist = (ListView)rootView.findViewById(R.id.people_list);
        	ProgressBar loader  = (ProgressBar)rootView.findViewById(R.id.people_loader);
            
            if (ParseValues.hasPeople) {
            	loader.setVisibility(ProgressBar.GONE);
	        	peoplelist.setAdapter(new PeopleRowAdapter(
	        		getActivity().getApplicationContext(),
	        		ParseValues.people_names.get(pos).toArray(
	        			new String[ParseValues.people_names.get(pos).size()]
	        		),
	        		pos
	        	));	
            } else {
            	loader.setVisibility(ProgressBar.VISIBLE);
            }

            return rootView;
        }
    }
    
	public void ErrorAlert(String message) {
		new AlertDialog.Builder(getActivity())
	    .setTitle("Error")
	    .setMessage(message)
	    .setNegativeButton("OK", null)
	    .show();
	}
    
}

 