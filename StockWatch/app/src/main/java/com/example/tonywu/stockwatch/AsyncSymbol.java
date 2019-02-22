package com.example.tonywu.stockwatch;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class AsyncSymbol extends AsyncTask<String, Void, String> {

    private HashMap<String, String> wData = new HashMap<>();
    private static final String stockURL = "https://api.iextrading.com/1.0/ref-data/symbols";
    //private static final String prefix = "https://api.iextrading.com/1.0/stock/";
    //private static final String suffix = "/quote?displayPercent=true";
    private MainActivity mainAct;


    public AsyncSymbol (MainActivity mainAct){
        this.mainAct = mainAct;
    }

    @Override
    protected String doInBackground(String...params){
        StringBuilder stringBuilder = new StringBuilder();
        try{
            //URL url = new URL(prefix+params[0]+suffix);
            URL url = new URL(stockURL);
            //URL url = new URL(stockURL+params[0]);
            //Uri.Builder buildUrl = Uri.parse(stockURL).buildUpon();
            Log.d("url: ",url.toString() +" "+ params[0] );
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            InputStream inputStream = connection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader((new InputStreamReader(inputStream)));
            String str;
            while ((str = bufferedReader.readLine())!=null) {
                stringBuilder.append(str).append("\n");
            }

        }catch (java.io.IOException e){
            e.printStackTrace();
        }
        Log.d("asyncSymbol: doInBack", stringBuilder.toString());
        return stringBuilder.toString();
    }

    private ArrayList<Stock> parseJSON(String s){
         ArrayList<Stock> stocksList = new ArrayList<>();
         Toast.makeText(mainAct, "parsing JSON", Toast.LENGTH_SHORT).show();


        try{
            JSONArray jsonArray = new JSONArray(s);
            for (int i = 0; i<jsonArray.length(); i++){
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                Stock stock = new Stock(jsonObject.getString("symbol"),jsonObject.getString("name"));
                //Stock stock = new Stock(jsonObject.getString("company_symbol"), jsonObject.getString("company_name"));
                stocksList.add(stock);

            }
            return stocksList;
        }catch (JSONException e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String s){
        Toast.makeText(mainAct, "Load stock", Toast.LENGTH_SHORT).show();
        final ArrayList<Stock> stocksList = parseJSON(s);
        AlertDialog.Builder builder = new AlertDialog.Builder(mainAct);
        final Stock[] stock = new Stock[1];

        if (stocksList == null) {
            builder.setTitle("Symbol Not Found");
            builder.setMessage("404 Not Found");
            AlertDialog alert = builder.create();
            alert.show();
            return;
        }
        else if(stocksList.size() == 1){
            stock[0] = stocksList.get(0);
            AsyncData asyncData = new AsyncData(mainAct);
            asyncData.execute(stock[0]);
        }
        else {
            CharSequence[] stocks = new CharSequence[stocksList.size()];
            int i = 0;
            for (Stock each : stocksList) {
                CharSequence tmp = each.getSymbol() + " - " + each.getName();
                stocks[i] = tmp;
                i++;
            }


            builder.setTitle("Select stock");
            builder.setItems(stocks, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    stock[0] = stocksList.get(which);
                    AsyncData asyncData = new AsyncData(mainAct);
                    asyncData.execute(stock[0]);

                }
            });

            builder.setNegativeButton("Exit", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //leave
                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
    }



}
