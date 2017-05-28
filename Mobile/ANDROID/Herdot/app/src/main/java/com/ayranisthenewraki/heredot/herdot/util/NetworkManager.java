package com.ayranisthenewraki.heredot.herdot.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

/**
 * Created by idilgun on 04/05/17.
 */

public class NetworkManager {

    final static int TIMEOUT_SECONDS = 100;

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        //should check null because in air plan mode it will be null
        return (netInfo != null && netInfo.isConnected());
    }

    public static OkHttpClient getNewClient() {

        return new OkHttpClient.Builder()
                    .readTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
                    .connectTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
                    .build();
    }

}
