package com.example.tonywu.stockwatch;

import android.app.AlertDialog;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class AsyncUpdate extends AsyncTask<Stock, Void, String> {
    private static final String prefix = "https://api.iextrading.com/1.0/stock/";
    private static final String suffix = "/quote?displayPercent=true";

    private MainActivity mainAct;
    private Stock stock;

    public AsyncUpdate (MainActivity mainAct){
        this.mainAct = mainAct;
    }

    @Override
    protected String doInBackground(Stock...params){
        stock = params[0];

        StringBuilder stringBuilder = new StringBuilder();

        try{

            URL url = new URL(prefix+stock.getSymbol()+suffix);
            Log.d("url", url.toString());
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            InputStream inputStream = connection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader((new InputStreamReader(inputStream)));

            String line;
            while((line = bufferedReader.readLine()) !=null) {
                stringBuilder.append(line).append("\n");
                return  stringBuilder.toString();
            }

        }
        catch (MalformedURLException e){
            e.printStackTrace();
        }catch (ProtocolException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }
    public void parseJSON(String s){
        try{
            JSONArray jsonArray = new JSONArray(s);
            JSONObject jsonObject = (JSONObject) jsonArray.get(0);

            stock.setChangePercent(jsonObject.getDouble("changePercent"));
            stock.setPrice(jsonObject.getDouble("latestPrice"));
            stock.setPchange(jsonObject.getDouble("change"));
        }catch (JSONException e){
            AlertDialog.Builder builder = new AlertDialog.Builder(mainAct);
            builder.setTitle("Download has failed: update");
            builder.setMessage("404 Not Found");
            AlertDialog alert = builder.create();
            alert.show();

        }
    }
    @Override
    protected void onPostExecute(String s){
        parseJSON(s);
        if (stock.getPrice()==0){
            return;
        }
        else{
            mainAct.updateData(stock);
        }
    }
}
