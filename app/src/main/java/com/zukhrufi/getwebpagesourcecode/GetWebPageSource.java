package com.zukhrufi.getwebpagesourcecode;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by DELL on 22/10/2017.
 */

public class GetWebPageSource extends AsyncTaskLoader<String>{
    private String urlLink;

    public GetWebPageSource(Context context, String urlLink) {
        super(context);
        this.urlLink = urlLink;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public String loadInBackground() {
        InputStream in;

        try {
            URL url = new URL(urlLink);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setReadTimeout(10000);
            connection.setConnectTimeout(20000);
            connection.setRequestMethod("GET");
            connection.connect();

            in = connection.getInputStream();

            BufferedReader buf = new BufferedReader(new InputStreamReader(in));
            StringBuilder st = new StringBuilder();
            String line="";
            while((line = buf.readLine()) != null){
                st.append(line+"  \n");
            }
            buf.close();
            in.close();

            return  st.toString();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "Error";
    }

}
