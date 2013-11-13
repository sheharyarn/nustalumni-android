package com.impsycho.nustalumni;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class PeopleFragment extends Fragment {

    DemoCollectionPagerAdapter mDemoCollectionPagerAdapter;
    ViewPager mViewPager;
    
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	View daView = inflater.inflate(R.layout.fragment_people_pager, container, false);

        mViewPager = (ViewPager) daView.findViewById(R.id.pager);
        mViewPager.setAdapter(mDemoCollectionPagerAdapter);
    	
        return daView;
    }
    
    public static class DemoCollectionPagerAdapter extends FragmentStatePagerAdapter {

        public DemoCollectionPagerAdapter(android.support.v4.app.FragmentManager fm) {
            super(fm);
        }

        @Override
        public android.support.v4.app.Fragment getItem(int i) {
        	android.support.v4.app.Fragment fragment = new DemoObjectFragment();
            Bundle args = new Bundle();
            args.putInt(DemoObjectFragment.ARG_OBJECT, i + 1); // Our object is just an integer :-P
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public int getCount() {
            // For this contrived example, we have a 100-object collection.
            return 10;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return "OBJECT " + (position + 1);
        }
    }


    public static class DemoObjectFragment extends android.support.v4.app.Fragment {
        public static final String ARG_OBJECT = "object";
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_people_section, container, false);
            Bundle args = getArguments();
            ((TextView) rootView.findViewById(R.id.textView1)).setText(Integer.toString(args.getInt(ARG_OBJECT)));
            return rootView;
        }
    }
    
    private class SetAdapterTask extends AsyncTask<Void, Void, Void> {
        protected Void doInBackground(Void... params) {
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

            if(mDemoCollectionPagerAdapter!= null) mViewPager.setAdapter(mDemoCollectionPagerAdapter);
        }
    } 
}

 