package com.impsycho.nustalumni;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class APIclient {
  private static final String BASE_URL = "https://nustalumni.herokuapp.com/api/v1";
  private static AsyncHttpClient client = new AsyncHttpClient();
  public static String api_auth_token = null;

  
  //--------------------------------------------------------
  
  // API GET Calls
  public static void get(String url, RequestParams params, Context context, AsyncHttpResponseHandler responseHandler) {
	  ConfirmInternetConnectivity(context);
	  
	  if (params == null) 
		  params = new RequestParams();
	  params.put("auth_token", api_auth_token);
      client.get(getAbsoluteUrl(url), params, responseHandler);
  } 

  // API POST Calls
  public static void post(String url, RequestParams params, Context context, AsyncHttpResponseHandler responseHandler) {
	  ConfirmInternetConnectivity(context);
	  
	  if (params == null)
		  params = new RequestParams();
	  params.put("auth_token", api_auth_token);
	  client.post(getAbsoluteUrl(url), params, responseHandler);
  }

  private static String getAbsoluteUrl(String relativeUrl) {
      return BASE_URL + relativeUrl;
  }
  
  public static void MakeInternetToast(Context context) {
	  Toast.makeText(context, "Please check your Internet Connection", Toast.LENGTH_SHORT).show();
  }

  public static boolean isNetworkAvailable(Context context) {
      boolean outcome = false;

      if (context != null) {
          ConnectivityManager cm = (ConnectivityManager) context
                  .getSystemService(Context.CONNECTIVITY_SERVICE);

          NetworkInfo[] networkInfos = cm.getAllNetworkInfo();
          for (NetworkInfo tempNetworkInfo : networkInfos) {
              if (tempNetworkInfo.isConnected()) {
                  outcome = true;
                  break;
              }
          }
      }

      return outcome;
  }
  
  public static void ConfirmInternetConnectivity(Context context) {
	  if (!isNetworkAvailable(context))
		  MakeInternetToast(context);
  }
  
}