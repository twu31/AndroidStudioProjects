package com.example.tonywu.newsgateway;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;


public class NewsSourceDownloader extends AsyncTask<String,Void,String> {


    private static final String URLprefix = "https://newsapi.org/v1/sources?language=en&country=us&category=";
    private static final String URLend = "&apiKey=f4d9182f45d74ed79d68cbb0cd6547f8";
    private MainActivity mainActivity;
    private String newsCategory;

    private ArrayList<String> categories;

    public NewsSourceDownloader(MainActivity ma, String nc){
        mainActivity = ma;

        if(nc.equals("all") || nc.equals("")){
            newsCategory = "";
        }
    }

    @Override
    protected void onPreExecute(){
        Log.d("newsSource", "preExec ");
    }

    @Override
    protected void onPostExecute(String s){
        Log.d("newsSource", "postExec: ");
        try {
            mainActivity.setSources(parseJSON(s), categories);
        } catch (Exception e){
            Log.d("newsSource", "postExec: " + e.getMessage());
            e.printStackTrace();
        }

    }

    @Override
    protected String doInBackground(String... params){


        String dataURL = URLprefix + newsCategory + URLend;
        Log.d("newsSource", "URL: " + dataURL);

        Uri dataUri = Uri.parse(dataURL);
        String finalURL = dataUri.toString();
        //Log.d("newsource", "URL " + urlToUse);

        StringBuilder stringBuilder = new StringBuilder();
        try {
            URL url = new URL(finalURL);

            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader reader = new BufferedReader((new InputStreamReader(inputStream)));
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line).append('\n');
            }
            //Log.d(newsSource, "toString" + sb.toString());

        } catch (MalformedURLException e){
            e.printStackTrace();
        }
        catch (Exception e) {
            Log.e("newsSource", "exception ", e);
            return null;
        }


        Log.d("newsSource", "done");
        return stringBuilder.toString();

    }

    private ArrayList<Source> parseJSON(String s){
        //Log.d("newSource", "parseJSON: ");

        ArrayList<Source> sourceList = new ArrayList<>();
        categories = new ArrayList<>();
        //Log.d("newsSource", "parseJSON: " + s);
        try{
            // get json object
            Log.d("newsSource","parsing");
            JSONObject entire = new JSONObject(s);
            JSONArray sources = entire.getJSONArray("sources");

            for(int i = 0; i < sources.length(); i++){
                JSONObject obj = sources.getJSONObject(i);
                String id = obj.getString("id");
                String name = obj.getString("name");
                String url = obj.getString("url");
                String category = obj.getString("category");

                if(! categories.contains(category) ){
                    categories.add(category);
                    //Log.d("newsSource", "category: " + category);
                }

                sourceList.add(new Source(id,name,url,category));
            }
            //mainActivity.addSource(new Source(id,name,url,catergory));
            return sourceList;

        }catch (Exception e){
            Log.d("newsource", "parseJSON: " + e.getMessage());
            e.printStackTrace();
        }

        return null;

    }
}