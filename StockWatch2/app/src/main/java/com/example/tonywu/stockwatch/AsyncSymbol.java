package com.example.tonywu.stockwatch;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONObject;


public class AsyncSymbol extends AsyncTask<String,Integer,String> {

    private MainActivity mainActivity;
    private int count;


    //private final String prefix = "api.iextrading.com/1.0/ref-data/symbols;
    private final String prefix = "http://d.yimg.com/aq/autoc?region=US&lang=en-US&query=";
    private static final String TAG = "AsyncStockLoader";

    public AsyncSymbol(MainActivity ma){ mainActivity = ma;}

    @Override
    protected void onPreExecute(){
        Toast.makeText(mainActivity, "Loading Stocks ", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onPostExecute(String s) {
        ArrayList<Stock> stocksList = parseJSON(s);
        //final Stock stock = new Stock;
        if(stocksList.size() == 0){

            mainActivity.notFoundDialog();
        }
        else {


            mainActivity.stockSelect(stocksList);
            //mainActivity.updateData(stocksList);
        }
    }

    @Override
    protected String doInBackground(String... params) {

        String dataURL = prefix + params[0];
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
            //return sb.toString();

            Log.d(TAG, "doInBackground: " + sb.toString());

        } catch (Exception e) {
            Log.e(TAG, "doInBackground: ", e);
            return null;
        }

        Log.d(TAG, "doInBackground: " + sb.toString());
        Log.d(TAG, "doInBackground: done");
        return sb.toString();
    }


    private ArrayList<Stock> parseJSON(String s) {

        Log.d(TAG, "parseJSON: started JSON");

        //s = s.substring(1,s.length());
        ArrayList<Stock> stocksList = new ArrayList<>();
        try {
            //JSONArray jsonArray = new JSONArray(s);
            JSONObject jObject3 = new JSONObject(s);
            JSONObject jObject2 = jObject3.getJSONObject("ResultSet");
            JSONArray jObject = jObject2.getJSONArray("Result");
            count = jObject.length();

            for (int i = 0; i < jObject.length(); i++) {
                JSONObject jStock = (JSONObject) jObject.get(i);
                String type = jStock.getString("type");

                //Stock stock = new Stock(jStock.getString("symbol"), jStock.getString("name");
                //stocksList.addStock(stock);
                if( type.equals("S")  ){

                    String symbol = jStock.getString("symbol");
                    String name = jStock.getString("name");

                    int idx = symbol.indexOf('.');
                    if(idx >= 0){
                        Log.d(TAG, "parseJSON: ignored " + name + ", " + symbol);
                    }
                    else{
                        Log.d(TAG, "parseJSON: loaded " + name + ", " + symbol);
                        stocksList.add(new Stock(name, symbol));
                    }

                }


            }
            return stocksList;
        } catch (Exception e) {
            Log.d(TAG, "parseJSON: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }


}
