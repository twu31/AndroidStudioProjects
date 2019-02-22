package com.example.tonywu.stockwatch;


import android.app.AlertDialog;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


public class AsyncData extends AsyncTask<String,Integer,String> {

    private MainActivity mainAct;
    private int count;


    private final String prefix = "https://api.iextrading.com/1.0/stock/";
    private final String suffix = "/quote?displayPercent=true";
    private static final String TAG = "AsyncData";

    public AsyncData(MainActivity ma){ mainAct = ma;}

    @Override
    protected void onPreExecute(){
        //Toast.makeText(mainActivity, "Loading Financial Data...", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onPostExecute(String s) {
        Stock stock = parseJSON(s);

        //parseJSON(s);
       /* try{
            JSONArray jsonArray = new JSONArray(s);
            JSONObject jsonObject = (JSONObject) jsonArray.get(0);

            stock.setChangePercent(jsonObject.getDouble("changePercent"));
            stock.setPrice(jsonObject.getDouble("latestPrice"));
            stock.setPchange(jsonObject.getDouble("change"));
        }catch (JSONException e){
            AlertDialog.Builder builder = new AlertDialog.Builder(mainAct);
            builder.setTitle("Download has failed");
            builder.setMessage("404 Not Found");
            AlertDialog alert = builder.create();
            alert.show();

        }*/

        mainAct.addNewStock(stock);
        // call something to add new data to existing data
    }

    @Override
    protected String doInBackground(String... params) {

        String dataURL = prefix + params[0] + suffix;
        Log.d(TAG, "doInBackground: URL is " + dataURL);
        Uri dataUri = Uri.parse(dataURL);
        String urlToUse = dataUri.toString();
        Log.d(TAG, "doInBackground: " + urlToUse);

        StringBuilder sb = new StringBuilder();
        try {
            URL url = new URL(urlToUse);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            InputStream is = conn.getInputStream();
            BufferedReader reader = new BufferedReader((new InputStreamReader(is)));

            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }

            Log.d(TAG, "doInBackground: " + sb.toString());

        } catch (Exception e) {
            Log.e(TAG, "doInBackground: ", e);
            return null;
        }

        Log.d(TAG, "doInBackground: " + sb.toString());
        Log.d(TAG, "doInBackground: returning");
        return sb.toString();
    }

    private Stock parseJSON(String s) {

        Log.d(TAG, "parseJSON: started JSON");

        ArrayList<Stock> stocksList = new ArrayList<>();
        try {

            //JSONArray jsonArray = new JSONArray(s);
            JSONObject jStock = new JSONObject(s);
            //JSONArray stockArr = jsonObject.getJSONArray("symbol");
            //JSONObject jStock = (JSONObject) stockArr.get(0);
            //JSONObject jsonObject = (JSONObject) jsonArray.get(0);// {}
            //JSONObject jObjMain2 = jObjMain3.getJSONObject("ResultSet");
            //JSONArray jObjMain = jObjMain2.getJSONArray("Result");
            //count = jObjMain.length();

            //String symbol = jStock.getString("symbol");


            String symbol = jStock.getString("symbol");
            double price = Double.parseDouble (jStock.getString("latestPrice"));
            double change = Double.parseDouble (jStock.getString("change"));
            double percent = Double.parseDouble (jStock.getString("changePercent"));
            Stock stock = new Stock("", symbol, price, change, percent);
            return stock;

            /*

            //Log.d("jsonobject", ""+jsonObject.getDouble("changePercent"));

            stock.setChangePercent(jsonObject.getDouble("changePercent"));
            stock.setPrice(jsonObject.getDouble("latestPrice"));
            stock.setPchange(jsonObject.getDouble("change"));
            */
            //return stocksList;
        } catch (Exception e) {
            Log.d(TAG, "parseJSON: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
}
