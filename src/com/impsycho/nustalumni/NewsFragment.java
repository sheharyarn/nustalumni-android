package com.impsycho.nustalumni;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.loopj.android.http.JsonHttpResponseHandler;

public class NewsFragment extends Fragment {
	static ListView newslist;
	ProgressBar emptyloader;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	View newsView = inflater.inflate(R.layout.fragment_news, container, false);
    	
    	emptyloader = (ProgressBar)newsView.findViewById(R.id.news_loader);
    	newslist = (ListView)newsView.findViewById(R.id.news_list);
    	
    	newslist.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
	            Intent newsarticle = new Intent(getActivity(), NewsActivity.class);
	            newsarticle.putExtra(NewsActivity.POST_ID, position);
	            startActivity(newsarticle);
			}
    	});

    	InflateNewsData(); 
    	ParseNewsData();
    	
    	return newsView;
    }
    
    public void InflateNewsData() {
    	if (!ParseValues.hasNews) {
    		newslist.setVisibility(LinearLayout.GONE);
    		emptyloader.setVisibility(ProgressBar.VISIBLE);
    	} else {
    		newslist.setVisibility(LinearLayout.VISIBLE);
    		emptyloader.setVisibility(ProgressBar.GONE);
    		
    		newslist.setAdapter(new NewsRowAdapter(getActivity().getApplicationContext(), ParseValues.news_titles.toArray(new String[ParseValues.news_titles.size()])));

        }
    } 
    
    public void ParseNewsData() {
    	if (!ParseValues.hasNews)
    		ForceParseNewsData();
    }
    
    public void ForceParseNewsData() {
    	APIclient.get("/data/news/", null, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(JSONArray response) {
	        	try {
        			ParseValues.news_titles   = new ArrayList<String>();
        			ParseValues.news_dates    = new ArrayList<String>();
        			ParseValues.news_urls    = new ArrayList<String>();
        			ParseValues.news_images   = new ArrayList<String>();
        			ParseValues.news_contents = new ArrayList<String>();
        			
	        		for (int i=0; i<response.length(); i++) {
	        			JSONObject item = response.getJSONObject(i);
	        			ParseValues.news_titles.add(item.getString("title"));
	        			ParseValues.news_dates.add(item.getString("date"));
	        			ParseValues.news_urls.add(item.getString("url"));
	        			ParseValues.news_images.add(item.getString("image"));
	        			ParseValues.news_contents.add(item.getString("content"));
	        		}
	        		
	        		ParseValues.hasNews = true;
	    			InflateNewsData();
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
	
	public void ErrorAlert(String message) {
		new AlertDialog.Builder(getActivity())
	    .setTitle("Error")
	    .setMessage(message)
	    .setNegativeButton("Cancel", null)
	    .show();
		
	}
}
