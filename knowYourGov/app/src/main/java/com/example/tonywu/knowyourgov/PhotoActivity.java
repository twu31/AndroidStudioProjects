package com.example.tonywu.knowyourgov;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;



public class PhotoActivity extends AppCompatActivity {

    public TextView locationView;
    public TextView officeView;
    public TextView nameView;
    public ImageView imageView;

     private Official official;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setBackgroundColor(Color.BLACK);
        setContentView(R.layout.activity_photo);

        locationView = findViewById(R.id.locationID);
        officeView = findViewById(R.id.officeID);
        nameView = findViewById(R.id.nameID);
        imageView = findViewById(R.id.imageID);

/*
       Intent intent = getIntent();
        official = (Official) intent.getSerializableExtra("official");
        //Log.d("after go to pa", locationTextView.getText().toString());
        CharSequence ch = intent.getCharSequenceExtra("location");*/

        Intent intent = this.getIntent();

        official = (Official) intent.getSerializableExtra("official");

        String header = intent.getStringExtra("header");
        Log.d("photoact", "onCreate: header" + header);
        //Log.d("photoact", "onCreate: header" + header.toString());

        if(locationView == null){
            Log.d("photoact", "onCreate: is null");
        }
        //null object reference
        locationView.setText(header.toString());
        locationView.setText(header);
        officeView.setText(intent.getStringExtra("office"));
        nameView.setText(intent.getStringExtra("name"));

        String color = intent.getStringExtra("color");
        if (color.equals("red")) {
            getWindow().getDecorView().setBackgroundColor(ContextCompat.getColor(this,R.color.maroon));
        }
        if (color.equals("blue")) {
            getWindow().getDecorView().setBackgroundColor(ContextCompat.getColor(this,R.color.navy));
        }
        if(color.equals("black")){
        }

        if(connected()) {

            //Log.d("photoact", "onCreate: img");
            final String photoUrl = intent.getStringExtra("photoUrl");
            Log.d("photoact", "url is " + photoUrl);
            Picasso picasso = new Picasso.Builder(this).listener(new Picasso.Listener() {
                @Override
                public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
                    // Here we try https if the http image attempt failed
                    final String changedUrl = photoUrl.replace("http:", "https:");
                    //Log.d("photoact", "failed ");
                    picasso.load(changedUrl)
                            .error(R.drawable.brokenimage)
                            .placeholder(R.drawable.placeholder)
                            .into(imageView);

                }
            }).build();

            //Log.d("photoact", "placeholder");
            picasso.load(photoUrl)
                    .error(R.drawable.brokenimage)
                    .placeholder(R.drawable.placeholder)
                    .into(imageView);
        } else{
            imageView.setImageResource(R.drawable.placeholder);
        }

    }


    private boolean connected(){
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnected();
    }

}