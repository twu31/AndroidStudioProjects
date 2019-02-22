package com.example.tonywu.knowyourgovernment;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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

import org.xml.sax.Locator;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements View.OnLongClickListener, View.OnClickListener {


    private OfficialAdaptor officialAdaptor;
    private RecyclerView recyclerView;
    private List<Official> officialList = new ArrayList<>();
    private Locate locate;
    private TextView locationView;
    private MainActivity mainActivity = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        recyclerView = findViewById(R.id.recycler);
        officialAdaptor = new OfficialAdaptor(officialList, this);
        recyclerView.setAdapter(officialAdaptor);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        locationView = findViewById(R.id.locationID);
        //locationView.setText("hello");
        //locationView.setTextColor(getResources().getColor(R.color.colorAccent));


        if(connection()){
            locate = new Locate(this);
            locate.shutdown();

        } else{
            locationView.setText("No data for this location");
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("cannot access");
            //builder.setMessage("data cannot be accessed");
            AlertDialog dialog = builder.create();
            dialog.show();
        }



    }

    private void saveData(){
        Log.d("mainACt", "saveData: ");

        try {
            FileOutputStream fos = getApplicationContext().openFileOutput(getString(R.string.file_name), Context.MODE_PRIVATE);

            JsonWriter writer = new JsonWriter(new OutputStreamWriter(fos, getString(R.string.encoding)));
            writer.setIndent(("  "));

            // save normalized input header
            writer.beginObject();
            writer.name("norminput").value(locationView.getText().toString());
            writer.endObject();

            // save list items
            writer.beginArray(); // array of officials
            for(int i = 0; i < officialList.size(); i++){
                writer.beginObject();
                writer.endObject();
            }

            writer.endArray();

            writer.close();
            Log.d("mainact", "saveData: saved");

        } catch (Exception e){
            e.getStackTrace();
        }
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
                Log.d("mainAct", "menu location");
                if(connection()){
                    Toast.makeText(this, "dialog is working", Toast.LENGTH_SHORT).show();
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    final EditText editText = new EditText(this);
                    editText.setInputType(InputType.TYPE_CLASS_TEXT);
                    editText.setGravity(Gravity.CENTER_HORIZONTAL);
                    builder.setView(editText);

                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String string = editText.getText().toString();
                            new AsyncOfficial(mainActivity).execute(string);
                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //close will occur
                            //Log.d("Mainactivity, "Builder cancel");
                        }
                    });
                    builder.setMessage("Enter city, state or zipcode");
                    AlertDialog dialog = builder.create();
                    dialog.show();
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setMessage("Data cannot be accessed without internet");
                    builder.setTitle("NO Network");
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

    @Override
    public void onClick(View view){
        Toast.makeText(this, "click", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(MainActivity.this, OfficialActivity.class);
        int pos = recyclerView.getChildLayoutPosition(view);
        Official ofl = officialList.get(pos);
        intent.putExtra("header", locationView.getText().toString());
        Bundle bundle = new Bundle();
        bundle.putSerializable("official", ofl);
        intent.putExtras(bundle);

        startActivity(intent);
    }

    @Override
    public boolean onLongClick(View view){

        Toast.makeText(this, "Long click", Toast.LENGTH_SHORT).show();
        int pos = recyclerView.getChildLayoutPosition(view);
        onClick(view);
        return false;
    }

    public void doLocation (double latitude, double longitude){

        List<Address> addresses = null;
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try{
            Log.d("mainACt","try geocode");
            addresses = geocoder.getFromLocation(latitude,longitude,1);
            Address address = addresses.get(0);
            String zipCode = address.getPostalCode();
            new AsyncOfficial(mainActivity).execute(zipCode);
        } catch (IOException e){
            //Log.d("mainActivity", "get address");
            Toast.makeText(mainActivity, "Could not access address", Toast.LENGTH_SHORT);
        }
    }

    public void setOfficialList(Object[] list1){

        if(list1 == null){
            locationView.setText("no data for this location");
        } else{
            Log.d("mainactivity","list1toString"+list1[0].toString());
            locationView.setText(list1[0].toString());
            officialList.clear();
            @SuppressWarnings("unchecked")
            ArrayList<Official> offList = (ArrayList<Official>) list1[1];
            //Log.d("mainactivity","size of officiallise"+offList.get(0));
            for(int i = 0; i < offList.size(); i++){
                officialList.add(offList.get(i));
            }
        }
        officialAdaptor.notifyDataSetChanged();
    }

/*
    public void addOfficial(Official official1){
        officialList.add(official1);
        officialAdaptor.notifyDataSetChanged();
    }

    public void clearOfficial(){
        officialList.clear();
    }*/

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[]results){
        if (requestCode ==5 ){

            for(int i = 0; i<permissions.length; i++){
                if(permissions[i].equals(Manifest.permission.ACCESS_FINE_LOCATION)){
                    if(results[i]== PackageManager.PERMISSION_GRANTED){
                        locate.setUpLocation();
                        locate.lookLocation();
                        super.onRequestPermissionsResult(requestCode,permissions,results);
                    } else {
                        Toast.makeText(this,"Permission denied",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }

    private boolean connection(){
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }
}
