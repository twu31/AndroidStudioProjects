package com.example.tonywu.knowyourgovernment;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class AsyncOfficial extends AsyncTask<String, Void, String> {

    private MainActivity mainActivity;

    private static final String key = "AIzaSyCM_InZvyfdcq9ehC6TFysILPNdFvIR6CE"; //h
    //private static final String key = "AIzaSyB1XpTJwiGhD3OW905AS2Q8-rnOQ6vDsKU"; //mine
    private final String dataURL = "https://www.googleapis.com/civicinfo/v2/representatives?key="+key+"&address=";

    private String state;
    private String city;
    private String zip;

    public AsyncOfficial(MainActivity mainAct){

        mainActivity = mainAct;
    }

    @Override
    protected  void onPostExecute(String string){
    /*
        if(string == null || string.isEmpty()) {
            Toast.makeText(mainActivity, "No info available", Toast.LENGTH_SHORT).show();
            mainActivity.setOfficialList(null);
            return;
        }*/



        ArrayList<Official> officialList = parseJSON(string);
        Object[] results = new Object[2];
        results[0]=city+", "+state+" "+zip;
        results[1] = officialList;
        mainActivity.setOfficialList(results);
        return;
        //parseJSON(string);
    }

    @Override
    protected String doInBackground(String ... params){

        String dataURLsearch = dataURL +params[0];
        Log.d("asyncOfficial","URL is: "+dataURLsearch);

        Uri dataUri = Uri.parse(dataURLsearch);
        String str = dataUri.toString();
        StringBuilder stringBuilder = new StringBuilder();
        try{
            URL url = new URL(str);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader =new BufferedReader((new InputStreamReader(inputStream)));

            String line;
            while ((line = bufferedReader.readLine()) != null){
                stringBuilder.append(line).append('\n');
            }
        } catch (FileNotFoundException foe){
            return null;
        } catch (Exception e){
            return null;
        }

        Log.d("AsyncOfficial", "doinBackgorund");


        //return string builder
        return stringBuilder.toString();
    }

/*
    private void parseJSON(String string){
        try {
            JSONObject jObjMain = new JSONObject(s);

            JSONObject jNormalInput = jObjMain.getJSONObject("normalizedInput");

            String locationText = jNormalInput.getString("city")+", "+jNormalInput.getString("state")+" "+jNormalInput.getString("zip");
            mainActivity.setLocationText(locationText);
            JSONArray jArrayOffices = jObjMain.getJSONArray("offices");
            JSONArray jArrayOfficials = jObjMain.getJSONArray("officials");

            int length = jArrayOffices.length();
            mainActivity.clearOfficial();

            for (int i = 0; i<length; i++){
                JSONObject jObj = jArrayOffices.getJSONObject(i);
                String officeName = jObj.getString("name");

                JSONArray indicesStr = jObj.getJSONArray("officialIndices");
                ArrayList<Integer> indices = new ArrayList<>();

                for (int j = 0; j<indicesStr.length(); j++){
                    int pos = Integer.parseInt(indicesStr.getString(j));
                    Official official = new Official(officeName);
                    JSONObject jOfficial = jArrayOfficials.getJSONObject(pos);

                    official.setName(jOfficial.getString("name"));

                    JSONArray jAddresses = jOfficial.getJSONArray("address");
                    JSONObject jAddress = jAddresses.getJSONObject(0);

                    String address="";

                    if (jAddress.has("line1")) address+=jAddress.getString("line1")+'\n';
                    if (jAddress.has("line2")) address+=jAddress.getString("line2")+'\n';
                    if (jAddress.has("line3")) address+=jAddress.getString("line3")+'\n';
                    if (jAddress.has("city")) address+=jAddress.getString("city")+", ";
                    if (jAddress.has("state")) address+=jAddress.getString("state")+' ';
                    if (jAddress.has("zip")) address+=jAddress.getString("zip");

                    official.setAddress(address);

                    if (jOfficial.has("party")) official.setParty(jOfficial.getString("party"));
                    if (jOfficial.has("phones")) official.setPhone(jOfficial.getJSONArray("phones").getString(0));
                    if (jOfficial.has("urls")) official.setURL(jOfficial.getJSONArray("urls").getString(0));
                    if (jOfficial.has("emails")) official.setEmail(jOfficial.getJSONArray("emails").getString(0));

                    if (jOfficial.has("channels")){
                        Channel channel = new Channel();

                        JSONArray jChannels = jOfficial.getJSONArray("channels");
                        for (int k = 0; k<jChannels.length(); k++){
                            JSONObject jChannel = jChannels.getJSONObject(k);
                            if (jChannel.getString("type").equals("GooglePlus")) channel.setGooglePlusId(jChannel.getString("id"));
                            if (jChannel.getString("type").equals("Facebook")) channel.setFacebookId(jChannel.getString("id"));
                            if (jChannel.getString("type").equals("Twitter")) channel.setTwitterId(jChannel.getString("id"));
                            if (jChannel.getString("type").equals("YouTube")) channel.setYoutubeId(jChannel.getString("id"));
                        }
                        official.setChannel(channel);
                    }

                    if (jOfficial.has("photoUrl")) official.setPhotoUrl(jOfficial.getString("photoUrl"));
                    mainActivity.addOfficial(official);
                }

            }

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (NullPointerException e){
            return;
        }
    }
    }*/


    private ArrayList<Official> parseJSON(String string){
        Log.d("ASYNC", "parseJSON");

        ArrayList<Official> officials = new ArrayList<>();
        try{

            JSONObject jsonObject = new JSONObject(string);
            JSONObject jsonInput = jsonObject.getJSONObject("JsonInput");
            JSONArray offices = jsonObject.getJSONArray("offices");
            JSONArray ofc = jsonObject.getJSONArray("officials");

            Log.d("Async","parsing Json:1");
            state = jsonInput.getString("state");
            city = jsonInput.getString("city");
            zip = jsonInput.getString("zip");

            for(int i = 0; i<offices.length(); i++){

                JSONObject object = offices.getJSONObject(i);
                String officeName = object.getString("name");
                String officialIndices = object.getString("officialIndices");

                String temp = officialIndices.substring(1,officialIndices.length()-1);
                String [] tempA = temp.split(",");
                int [] ints = new int [tempA.length];
                for(int z =0; z < tempA.length; z ++){
                    ints[z] = Integer.parseInt(tempA[z]);
                }

                for(int x = 0; x<ints.length; x++){
                    JSONObject jsonObject1 = ofc.getJSONObject(ints[x]);
                    String name = jsonObject1.getString("name");

                    String address="";
                    if(!jsonObject1.has("address")){
                        address= "No address available";
                    } else{
                        JSONArray addressArray = jsonObject1.getJSONArray("address");
                        JSONObject addressOjbect = addressArray.getJSONObject(0);

                        if(addressOjbect.has("line1")){
                            address += addressOjbect.getString("line1")+"\n";
                        }
                        if(addressOjbect.has("line2")){
                            address += addressOjbect.getString("line2")+"\n";
                        }
                        if(addressOjbect.has("city")){
                            address += addressOjbect.getString("city")+" ";
                        }
                        if(addressOjbect.has("state")){
                            address += addressOjbect.getString("state")+", ";
                        }
                        if(addressOjbect.has("zip")){
                            address += addressOjbect.getString("zip");
                        }

                    }

                    String party = (jsonObject1.has("party") ? jsonObject1.getString("party") : "unknown" );
                    Log.d("asyncOfficial", "parseJSON: party: " + party);

                    String phones = ( jsonObject1.has("phones") ? jsonObject1.getJSONArray("phones").getString(0) : "no data provided" );
                    Log.d("asyncOfficial", "parseJSON: phone number: " + phones);

                    String urls = ( jsonObject1.has("urls") ? jsonObject1.getJSONArray("urls").getString(0) : "no data provided"  );
                    Log.d("asyncOfficial", "parseJSON: urls: " + urls);

                    String emails = (jsonObject1.has("emails") ? jsonObject1.getJSONArray("emails").getString(0) : "no data provided"  );
                    Log.d("asyncOfficial", "parseJSON: emails: " + emails);

                    String photoURL = (jsonObject1.has("photoUrl") ? jsonObject1.getString("photoUrl") :"no data provided" );
                    //Log.d(TAG, "parseJSON: photoUrl: " + photoURL);

                    //String googleplus = (innerObj.getJSONArray("channels").getJSONObject(0) ? : );

                    JSONArray channels = ( jsonObject1.has("channels") ? jsonObject1.getJSONArray("channels") : null );
                    String googleplus = ""; String facebook = ""; String twitter = ""; String youtube = "";

                    if(channels != null){
                        for(int k = 0; k < channels.length(); k++ ){
                            String type = channels.getJSONObject(k).getString("type");
                            //Log.d(TAG, "parseJSON: type at index " + k + " is " + type );
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
                                    //Log.d(TAG, "parseJSON: non recognized social media");
                                    break;
                            }
                        }
                    }
                    else{ // is null
                        googleplus = "No data provided"; facebook ="No data provided";
                        twitter = "No data provided"; youtube = "No data provided";
                    }

                    Official official = new Official(name, officeName, party, address, phones, urls,emails,photoURL,googleplus,facebook,twitter,youtube);
                    officials.add(official);

                }
            }


            return officials;
        }catch(Exception e){
            Log.d("ASYNC","rip json");

        }
        return null;
    }

}
