package com.example.tonywu.stockwatch;

import android.app.AlertDialog;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

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
import java.util.ArrayList;

public class AsyncData extends AsyncTask<Stock,Void, String> {

    private static final String prefix = "https://api.iextrading.com/1.0/stock/";
    private static final String suffix = "/quote?displayPercent=true";

    private MainActivity mainAct;
    private Stock stock;

    public AsyncData (MainActivity mainAct){
        this.mainAct = mainAct;
    }

    @Override
    protected String doInBackground(Stock...params){
        stock = params[0];
        Log.d("doInback",""+stock.getSymbol());
        String dataUrl = prefix+params[0]+suffix;
        Uri dataUri = Uri.parse(dataUrl);
        String urltoUse = dataUri.toString();
        StringBuilder stringBuilder = new StringBuilder();

        try{
            //URL url = new URL(urltoUse);
            //URL url = new URL(prefix+stock.getSymbol()+suffix);
            URL url = new URL(prefix+stock.getSymbol());
            Log.d("url", url.toString());
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            InputStream inputStream = connection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader((new InputStreamReader(inputStream)));

            String line;
            while((line = bufferedReader.readLine()) !=null) {
                stringBuilder.append(line).append("\n");
            }
                Log.d("stringBuilder", stringBuilder.toString());
                return stringBuilder.toString();


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

    private Stock  parseJSON(String s){
        ArrayList<Stock> stockList = new ArrayList<>();
        Log.d("parseJSON", s);
        //Toast.makeText(mainAct, "did stuff get downloaded", Toast.LENGTH_SHORT).show();
        try{


            JSONArray jsonArray = new JSONArray(s);
            //JSONObject jsonObject = new JSONObject(s);
            //JSONArray stockArr = jsonObject.getJSONArray("symbol");
            //JSONObject jStock = (JSONObject) stockArr.get(0);
            JSONObject jsonObject = (JSONObject) jsonArray.get(0);
            //Log.d("jsonobject", ""+jsonObject.getDouble("changePercent"));

            stock.setChangePercent(jsonObject.getDouble("changePercent"));
            stock.setPrice(jsonObject.getDouble("latestPrice"));
            stock.setPchange(jsonObject.getDouble("change"));
        }catch (JSONException e){
            AlertDialog.Builder builder = new AlertDialog.Builder(mainAct);
            builder.setTitle("Download has failed");
            builder.setMessage("404 Not Found");
            AlertDialog alert = builder.create();
            alert.show();

        }
        return null;
    }
    @Override
    protected void onPostExecute(String s){
        //Stock stock = parseJSON(s);
             parseJSON(s);
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
        if (stock.getPrice() == 0){
            return;
        }
        else{
            mainAct.addData(stock);
        }

    }
}
