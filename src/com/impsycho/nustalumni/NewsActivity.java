package com.impsycho.nustalumni;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.widget.ShareActionProvider;

public class NewsActivity extends Activity {
	private ShareActionProvider myShareActionProvider;
    public static final String POST_ID = "post_id";
    public int PostNumber = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_news);

        getActionBar().setDisplayHomeAsUpEnabled(true);
        
        PostNumber = getIntent().getIntExtra(NewsActivity.POST_ID, 0);
        
        String mPostTitle   = ParseValues.news_titles.get(PostNumber);
		String mPostDate    = ParseValues.news_dates.get(PostNumber);
		String mPostContent = ParseValues.news_contents.get(PostNumber);
		
		String MyCSS = "<link rel=\"stylesheet\" type=\"text/css\" href=\"styles.css\" />";
		String DatTitle = "<h1 class=\"title\" >" + mPostTitle + "</h1>";
		String DatSeparator = "<div class=\"separator\"></div>";
		String DatCategory = DatSeparator + "<h6 class=\"title\" >Posted on <span>" + mPostDate + "</span></h6>" + DatSeparator;
		String TitleAndCategory = DatTitle + DatCategory;
		
		String htmldata = "<html>" + MyCSS + "<body>" + TitleAndCategory + mPostContent + "</body>" + "</html>";
		
        WebView newsArticle = (WebView)findViewById(R.id.news_webview);
        newsArticle.loadDataWithBaseURL ("file:///android_asset/", htmldata, "text/html", "utf-8","about:blank");
		newsArticle.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.news, menu);
		
		MenuItem item = menu.findItem(R.id.menu_item_share);
	    myShareActionProvider = (ShareActionProvider)item.getActionProvider();
	    myShareActionProvider.setShareHistoryFileName(ShareActionProvider.DEFAULT_SHARE_HISTORY_FILE_NAME);
	    myShareActionProvider.setShareIntent(createShareIntent());
	    
		return true;
	}
	
	private Intent createShareIntent() {
		 String TheShitIShare = "Boom, you're Pregnant!";
		 TheShitIShare = ParseValues.news_titles.get(PostNumber) + " - " + ParseValues.news_urls.get(PostNumber);
		 
		 Intent shareIntent = new Intent(Intent.ACTION_SEND);
		 shareIntent.setType("text/plain");
		 shareIntent.putExtra(Intent.EXTRA_TEXT, TheShitIShare);
		 
		 return shareIntent;
	 }
	
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
