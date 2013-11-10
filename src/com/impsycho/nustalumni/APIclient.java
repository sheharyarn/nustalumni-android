package com.impsycho.nustalumni;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class APIclient {
  private static final String BASE_URL = "https://nustalumni.herokuapp.com/api/v1";
  private static AsyncHttpClient client = new AsyncHttpClient();
  public static String api_auth_token = null;

  
  //--------------------------------------------------------
  
  // API GET Calls
  public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
	  if (params == null) 
		  params = new RequestParams();
	  params.put("auth_token", api_auth_token);
      client.get(getAbsoluteUrl(url), params, responseHandler);
  } 

  // API POST Calls
  public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
	  if (params == null)
		  params = new RequestParams();
	  params.put("auth_token", api_auth_token);
      client.post(getAbsoluteUrl(url), params, responseHandler);
  }

  private static String getAbsoluteUrl(String relativeUrl) {
      return BASE_URL + relativeUrl;
  }
  
}