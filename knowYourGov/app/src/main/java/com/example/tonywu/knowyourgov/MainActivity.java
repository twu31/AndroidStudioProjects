package com.example.tonywu.knowyourgov;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.JsonWriter;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static android.graphics.Color.WHITE;
import static android.text.InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS;

public class MainActivity extends AppCompatActivity
        implements View.OnClickListener, View.OnLongClickListener {

    private MainActivity mainActivity = this;
    private RecyclerView recyclerView;
    private List<Official> officialList = new ArrayList<>();
    private OfficialAdapter officialAdapter;
    //Location loc;

    private TextView locationView;
    private Locator locator;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recycler);
        officialAdapter = new OfficialAdapter(officialList, this);
        recyclerView.setAdapter(officialAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        locationView = findViewById(R.id.locationID);
        locationView.setTextColor(ContextCompat.getColor(this,R.color.white));

        if(connected()) {
            locator = new Locator(this);
            locator.shutdown();
        } else{
            locationView.setText("No Data For Location");
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("No internet.");
            builder.setTitle("No Network Connection");
            AlertDialog dialog = builder.create();
            dialog.show();;
        }
        new AsyncOfficialLoader(mainActivity).execute("60616");
        //Location loc = new Location(this);
        /*Geocoder geocoder = new Geocoder(this);
        Address addr = new Address(Locale.getDefault());
         String str = "";
        try {
            List<Address>startAddresses = geocoder.getFromLocation(loc.getLatitude(), loc.getLongitude(), 1);
            addr = startAddresses.get(0);
            str = addr.getPostalCode();
        } catch(IOException e){
            Log.d("main","start zip");
        }


        new AsyncOfficialLoader(mainActivity).execute(str);*/


    }


    @Override
    protected void onResume(){
        super.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop(){
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

/*
    public void addOfficial(Official official1){
        officialList.add(official1);
        officialAdaptor.notifyDataSetChanged();
    }

    public void clearOfficial(){
        officialList.clear();
    }*/


    public void setOfficialList(Object[] res){

        if(res == null){
            locationView.setText("No Data For Location");
            officialList.clear();
        }
        else{
            locationView.setText(res[0].toString());
            officialList.clear();
            @SuppressWarnings("unchecked")
            ArrayList<Official> offList = (ArrayList<Official>) res[1];
            for(int i = 0; i < offList.size(); i++){
                officialList.add( offList.get(i));
            }
        }
        officialAdapter.notifyDataSetChanged();

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {


        if (requestCode == 5) {
            Log.d("mainactivity", "onRequestPermissionsResult: permissions.length: " + permissions.length);
            for (int i = 0; i < permissions.length; i++) {
                if (permissions[i].equals(Manifest.permission.ACCESS_FINE_LOCATION)) {
                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        //Log.d(mainactivity, "yes permission");
                        locator.setUpLocationManager();
                        locator.determineLocation();
                        super.onRequestPermissionsResult(requestCode,permissions,grantResults);
                    } else {
                        Toast.makeText(this, "NO location permission", Toast.LENGTH_LONG).show();
                        //Log.d(mainacitivty, "no permission");
                    }
                }
            }
        }
        Log.d("mainactivity", "Exit");
    }

    public void doLocationWork(double latitude, double longitude) {

        //Log.d("mainactivity", "doAddress: Lat: " + latitude + ", Lon: " + longitude);

        List<Address> addresses = null;

        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            Log.d("mainactivity", "try:getaddress");
            //????
            addresses = geocoder.getFromLocation(latitude, longitude, 1);
            Log.d("mainactivity", "address size: " + addresses.size());

            Address addr = addresses.get(0);
            String zipcode = addr.getPostalCode();

            new AsyncOfficialLoader(mainActivity).execute(zipcode);

        } catch (IOException e) {
            Log.d("mainactivity", "doAddress: " + e.getMessage());
            Toast.makeText(mainActivity,"Address cannot be acquired from provided latitude/longitude", Toast.LENGTH_SHORT).show();

        }
        Toast.makeText(this, "location finish", Toast.LENGTH_SHORT).show();

    }



    public void noLocationAvailable() {
        //Toast.makeText(this, "No location avaialbe", Toast.LENGTH_LONG).show();
    }


    @Override
    public void onClick(View view){
        //Toast.makeText(this, "onclick", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(MainActivity.this, OfficialActivity.class);
        int pos = recyclerView.getChildLayoutPosition(view);
        Official off1 = officialList.get(pos);
        intent.putExtra("header", locationView.getText().toString() );
        Bundle bundle = new Bundle();
        bundle.putSerializable("official", off1);
        intent.putExtras(bundle);

        startActivity(intent);

    }
    @Override
    public boolean onLongClick(View view){
        //Toast.makeText(this, "Long Clicked", Toast.LENGTH_SHORT).show();
        //int pos = recyclerView.getChildLayoutPosition(v);
        onClick(view);

        return false;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case R.id.location:
                Log.d("mainacitivy", "search: press ");
                if(connected()){
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    final EditText editText = new EditText(this);
                    editText.setInputType(InputType.TYPE_CLASS_TEXT );
                    editText.setGravity(Gravity.CENTER_HORIZONTAL);

                    builder.setView(editText);


                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            String input = editText.getText().toString();
                            new AsyncOfficialLoader(mainActivity).execute(input);

                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //Log.d(TAG, "onClick: Cancel clicked, do nothing");


                        }
                    });

                    builder.setMessage("Enter a City, State, or Zip Code:");

                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
                else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setMessage("Data cannot be accessed/ no internet.");
                    builder.setTitle("No Network Connection");
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
                return true;
            case R.id.about:
                Intent intent = new Intent(MainActivity.this, AboutActivity.class);
                startActivity(intent);
            default:
                return super.onOptionsItemSelected(item);
        }
    }



    private boolean connected(){
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnected();
    }

}