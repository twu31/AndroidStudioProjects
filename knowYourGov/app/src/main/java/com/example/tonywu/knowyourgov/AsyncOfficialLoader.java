package com.example.tonywu.knowyourgov;


import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


public class AsyncOfficialLoader extends AsyncTask<String, Void, String> {

    private MainActivity mainActivity;


    private static final String key = "AIzaSyCM_InZvyfdcq9ehC6TFysILPNdFvIR6CE";
    private final String dataURL = "https://www.googleapis.com/civicinfo/v2/representatives?key="+ key +"&address=";



    private String city;
    private String state;
    private String zip;




    public AsyncOfficialLoader(MainActivity ma){ mainActivity = ma;}

    @Override
    protected void onPreExecute(){
        //Toast.makeText(mainActivity, "load data", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onPostExecute(String string){
        Log.d("async", "post exec"+string);

        if(string == null || string.isEmpty()){
            Toast.makeText(mainActivity,"Info is not available",Toast.LENGTH_SHORT).show();
            mainActivity.setOfficialList(null);
            return;
        }

        //parseJSON(string)
        ArrayList<Official> officialList = parseJSON(string);
        Object [] results = new Object[2];
        results[0] = city + ", " + state + " " + zip;
        results[1] = officialList;
        mainActivity.setOfficialList(results);
        return;

    }

    @Override
    protected String doInBackground(String... params){


        String url = dataURL + params[0];
        Log.d("async", "doInBackground: URL is " + url);
        Uri uri = Uri.parse(url);
        String urlToUse = uri.toString();
        Log.d("async", "doInBackground: " + urlToUse);

        StringBuilder sb = new StringBuilder();
        try {
            URL url2 = new URL(urlToUse);

            HttpURLConnection httpURLConnection = (HttpURLConnection) url2.openConnection();
            httpURLConnection.setRequestMethod("GET");
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader reader = new BufferedReader((new InputStreamReader(inputStream)));

            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }

            Log.d("async", "doInBackground: " + sb.toString());

        }
        catch (FileNotFoundException e){
            return null;
        }
        catch (Exception e) {

            Log.e("async", "exeception ", e);
            return null;
        }

        //Log.d("async", "doInBackground: " + sb.toString());

        return sb.toString();

    }

    private ArrayList<Official> parseJSON(String string){
        Log.d("async", "parseJSON start");

        ArrayList<Official> officialList = new ArrayList<>();
        try{
            JSONObject wholeThing = new JSONObject(string);
            JSONObject normalizedInput = wholeThing.getJSONObject("normalizedInput");
            JSONArray offices = wholeThing.getJSONArray("offices");
            JSONArray officials = wholeThing.getJSONArray("officials");

            city = normalizedInput.getString("city");
            state = normalizedInput.getString("state");
            zip = normalizedInput.getString("zip");

            Log.d("async", "city, state, zip -> " + city + ", " + state + ", " + zip);


            for(int i = 0;i < offices.length(); i++){
                JSONObject obj = offices.getJSONObject(i);
                String officeName = obj.getString("name");
                String officialIndices = obj.getString("officialIndices");

                Log.d("async", "parseJSON: officialIndices: " + officialIndices);

                Log.d("async", "parseJSON: Officename: " + officeName);


                String temp = officialIndices.substring(1,officialIndices.length()-1);
                String [] temp2 = temp.split(",");
                //s
                int [] indices = new int [temp2.length];
                for(int j = 0; j < temp2.length; j++){
                    indices[j] = Integer.parseInt(temp2[j]);
                }


                for(int j = 0; j < indices.length; j++ ){
                    JSONObject Obj2 = officials.getJSONObject(indices[j]);
                    String name = Obj2.getString("name");
                    //Log.d("async", "indice"+indices[j]);

                    String address = "";
                    if(! Obj2.has("address")){
                        address = "no data provided";
                    }
                    else {
                        JSONArray addressArray = Obj2.getJSONArray("address");
                        JSONObject addressObject = addressArray.getJSONObject(0);


                        if (addressObject.has("line1")) {
                            address += addressObject.getString("line1") + "\n";

                        }
                        if (addressObject.has("line2")) {
                            address += addressObject.getString("line2") + "\n";
                        }
                        if (addressObject.has("city")) {
                            address += addressObject.getString("city") + " ";
                        }
                        if (addressObject.has("state")) {
                            address += addressObject.getString("state") + ", ";
                            //Log.d("async", "parseJSON: address currently is " + address);
                        }
                        if (addressObject.has("zip")) {
                            address += addressObject.getString("zip");
                        }

                    }

                    String party = (Obj2.has("party") ? Obj2.getString("party") : "Unknown" );
                    //Log.d("async, "JSON: party: " + party);

                    String phones = ( Obj2.has("phones") ? Obj2.getJSONArray("phones").getString(0) : "no data provided" );
                    //Log.d("async", "JSON: phone #: " + phones);

                    String urls = ( Obj2.has("urls") ? Obj2.getJSONArray("urls").getString(0) : "no data provided" );
                    //Log.d("async", "JSON: urls: " + urls);

                    String emails = (Obj2.has("emails") ? Obj2.getJSONArray("emails").getString(0) : "no data provided" );
                    //Log.d("async", "JSON: emails: " + emails);

                    String photoURL = (Obj2.has("photoUrl") ? Obj2.getString("photoUrl") : "no data provided");
                    //Log.d("async", "JSON: photoUrl: " + photoURL);


                    JSONArray channels = ( Obj2.has("channels") ? Obj2.getJSONArray("channels") : null );
                    String googleplus = ""; String facebook = ""; String twitter = ""; String youtube = "";

                    if(channels != null){
                        for(int k = 0; k < channels.length(); k++ ){
                            String type = channels.getJSONObject(k).getString("type");

                            switch (type){
                                case "GooglePlus":
                                    googleplus = channels.getJSONObject(k).getString("id");
                                    break;
                                case "Facebook":
                                    facebook = channels.getJSONObject(k).getString("id");
                                    break;
                                case "Twitter":
                                    twitter = channels.getJSONObject(k).getString("id");
                                    break;
                                case "YouTube":
                                    youtube = channels.getJSONObject(k).getString("id");
                                    break;
                                default:
                                    //Log.d("async", "rip");
                                    break;
                            }
                        }
                    }
                    else{
                        googleplus = "no data provided"; facebook = "no data provided";
                        twitter = "no data provided"; youtube = "no data provided";
                    }


                    Official officialPut = new Official(name, officeName, party,
                            address, phones, urls, emails, photoURL,
                            googleplus, facebook, twitter, youtube);
                    officialList.add(officialPut);
                }
            }

            return officialList;

        }catch(Exception e){
            Log.d("async", "exception end" + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
}
