package com.example.tonywu.knowyourgov;


import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.media.Image;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.util.Linkify;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;


public class OfficialActivity extends AppCompatActivity {



    public static final String NO_DATA = "No Data Provided";
    public static final String UNKNOWN = "Unknown";
    public static final String DEM = "Democratic";
    public static final String DEM2 = "Democrat";
    public static final String GOP = "Republican";

    public TextView locationView;

    public TextView officeView;
    public TextView nameView;
    public TextView partyView;
    public ImageView imageView;
    public TextView addressView;
    public TextView phoneView;
    public TextView emailView;
    public TextView websiteView;

    public TextView addressLabel;
    public TextView phoneLabel;
    public TextView emailLabel;
    public TextView websiteLabel;

    public ImageView youtubeButton;
    public ImageView googleplusButton;
    public ImageView twitterButton;
    public ImageView facebookButton;

    public Official official;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_official);
        getWindow().getDecorView().setBackgroundColor(Color.BLACK);


        locationView = findViewById(R.id.locationID); locationView.setTextColor(Color.WHITE);
        officeView = findViewById(R.id.officeID);
        nameView = findViewById(R.id.nameID);
        partyView = findViewById(R.id.partyID);
        imageView = findViewById(R.id.imageID);
        addressView = findViewById(R.id.addressID);
        phoneView = findViewById(R.id.phoneID);
        emailView = findViewById(R.id.emailID);
        websiteView = findViewById(R.id.websiteID);

        this.addressLabel = findViewById(R.id.addressLabel);
        this.phoneLabel = findViewById(R.id.phoneLabel);
        this.emailLabel = findViewById(R.id.emailLabel);
        this.websiteLabel = findViewById(R.id.websiteLabel);
        addressLabel.setText(("Address:").toString());
        phoneLabel.setText(("Phone:").toString());
        emailLabel.setText(("Email:").toString());
        websiteLabel.setText(("Website:").toString());
        addressLabel.setTextColor(Color.WHITE);
        phoneLabel.setTextColor(Color.WHITE);
        emailLabel.setTextColor(Color.WHITE);
        websiteLabel.setTextColor(Color.WHITE);

        this.youtubeButton = findViewById(R.id.youtubeButton);
        this.googleplusButton = findViewById(R.id.googleplusButton);
        this.twitterButton = findViewById(R.id.twitterButton);
        this.facebookButton = findViewById(R.id.facebookButton);

        youtubeButton.setImageResource(R.drawable.youtube);
        googleplusButton.setImageResource(R.drawable.googleplus);
        twitterButton.setImageResource(R.drawable.twitter);
        facebookButton.setImageResource(R.drawable.facebook);

        Intent intent = this.getIntent();

        Bundle bundle = intent.getExtras();
        official = (Official) bundle.getSerializable("official");
        locationView.setText(intent.getStringExtra("header"));



        if( official.getOffice().equals(NO_DATA)){ hideView(officeView);}
        else{officeView.setText(official.getOffice());officeView.setTextColor(Color.WHITE);}
        if( official.getName().equals(NO_DATA)){ hideView(nameView);}
        else{nameView.setText(official.getName());nameView.setTextColor(Color.WHITE);}
        if( official.getParty().equals(UNKNOWN)){ hideView(partyView);}
        else{
            partyView.setText("(" + official.getParty() + ")"); partyView.setTextColor(Color.WHITE);
            if(official.getParty().equals(DEM) || official.getParty().equals(DEM2)){

                getWindow().getDecorView().setBackgroundColor(ContextCompat.getColor( this ,R.color.blue));
            }
            if(official.getParty().equals(GOP)){

                getWindow().getDecorView().setBackgroundColor( ContextCompat.getColor(  this,R.color.red));
            }
        }

        if(connected()) {


            /*Image Loading*/
            //Log.d(TAG, "onCreate: Image Loading");
            imageView.setImageResource(R.drawable.placeholder);

            if (official.getPhotoUrl().equals(NO_DATA)) {
                /* imageView.setImage to generic */
                imageView.setImageResource(R.drawable.missingimage);
            } else {
                /* download image */
                final String photoUrl = official.getPhotoUrl();
                Picasso picasso = new Picasso.Builder(this).listener(new Picasso.Listener() {
                    @Override
                    public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
                        // Here we try https if the http image attempt failed
                        final String changedUrl = photoUrl.replace("http:", "https:");
                        picasso.load(changedUrl)
                                .error(R.drawable.brokenimage)
                                .placeholder(R.drawable.placeholder)
                                .into(imageView);

                    }
                }).build();

                picasso.load(photoUrl)
                        .error(R.drawable.brokenimage)
                        .placeholder(R.drawable.placeholder)
                        .into(imageView);
            }

        } else {
            imageView.setImageResource(R.drawable.placeholder);
        }


        addressView.setText(official.getAddress()); addressView.setTextColor(Color.WHITE);
        phoneView.setText(official.getPhone()); phoneView.setTextColor(Color.WHITE);
        emailView.setText(official.getEmail()); emailView.setTextColor(Color.WHITE);
        websiteView.setText(official.getUrl()); websiteView.setTextColor(Color.WHITE);


        if(official.getYoutube().equals(NO_DATA)){
            hideView(youtubeButton);
        }
        if(official.getGoogleplus().equals(NO_DATA)){
            hideView(googleplusButton);
        }
        if(official.getTwitter().equals(NO_DATA)){
            hideView(twitterButton);
        }
        if(official.getFacebook().equals(NO_DATA)){
            hideView(facebookButton);
        }


        //Uri mapUri = Uri.parse("geo:0,0?q="+addressView.getText().toString());
        //Intent mapintent = new Intent(Intent.ACTION_VIEW, mapUri);
        //mapintent.setPackage("com.google.android.apps.maps");
        //@SuppressWarnings()
        Linkify.addLinks(addressView,Linkify.MAP_ADDRESSES);
        //Linkify.addLinks(addressView, startActivity(mapintent));

        Linkify.addLinks(phoneView,Linkify.PHONE_NUMBERS);
        Linkify.addLinks(emailView,Linkify.EMAIL_ADDRESSES);
        Linkify.addLinks(websiteView,Linkify.WEB_URLS);


    }

    private static void hideView(View v){
        v.setVisibility(View.GONE);
    }

    public void openPhotoActivity(View v){
        Toast.makeText(this, "Enlarge photo", Toast.LENGTH_SHORT).show();
        Log.d("officialact", "imageClicked: ");
        if(official.getPhotoUrl().equals(NO_DATA)){
            return;
        }
        Toast.makeText(this, "Enlarge photo: done", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(OfficialActivity.this, PhotoActivity.class);
        Log.d("officialact", "openPhotoActivity: putting location" + locationView.getText().toString());

        //Bundle bundle = intent.getExtras();
        intent.putExtra("header", locationView.getText().toString());
        //intent.putExtras(bundle);

        Log.d("officialact", "openPhotoActivity: putting office" + official.getOffice());
        intent.putExtra("office", official.getOffice());
        Log.d("officialact", "openPhotoActivity: putting name" + official.getName());
        intent.putExtra("name", official.getName());

        if(official.getParty().equals(DEM) || official.getParty().equals(DEM2)){
            //Log.d("officialact", "putting color blue");
            intent.putExtra("color","blue");
        }
        if(official.getParty().equals(GOP)){
            //Log.d("officialact", "putting color red");
            intent.putExtra("color","red");
        }
        if(official.getParty().equals(UNKNOWN)){
            //Log.d("officialact", "putting color black");
            intent.putExtra("color", "black");
        }
         //Log.d("officialact", "imageUrl" + official.getPhotoUrl());
        intent.putExtra("photoUrl", official.getPhotoUrl());


        startActivity(intent);

    }

    public void youtubeClicked(View v){
        Log.d("officialact", "youtubeClicked ");

        String name = official.getYoutube();
        Intent intent;
        try {
            intent = new Intent(Intent.ACTION_VIEW);
            intent.setPackage("com.google.android.youtube");
            intent.setData(Uri.parse("https://www.youtube.com/" + name));
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,

                    Uri.parse("https://www.youtube.com/" + name)));

        }
    }

    public void googleplusClicked(View v){
        Log.d("officialact", "googleplusClicked");
        String name = official.getGoogleplus();
        Intent intent;
        try {
            intent = new Intent(Intent.ACTION_VIEW);
            intent.setClassName("com.google.android.apps.plus",
                    "com.google.android.apps.plus.phone.UrlGatewayActivity");
            intent.putExtra("customAppUri", name);
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,

                    Uri.parse("https://plus.google.com/" + name)));
        }
    }

    public void twitterClicked(View v){
        Log.d("officialact", "twitterClicked");

        Intent intent;
        String id = official.getTwitter();
        try {
            getPackageManager().getPackageInfo("com.twitter.android",0);
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("twitter://user?screen_name=" + id));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }catch (Exception e){
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://twitter.com/" + id));
        }
        startActivity(intent);
    }
    public void facebookClicked(View v){
        Log.d("officialact", "facebookClicked");

        String FACEBOOK_URL = "https://www.facebook.com/" + official.getFacebook();
        String urlToUse;

        PackageManager packageManager = getPackageManager();
        try {
            //int versionCode = packageManager.getPackageInfo("com.facebook.katana", 0).versionCode;

            packageManager.getPackageInfo("com.facebook.katana", 0);


            //versioncode is deprecated
            int versionCode = 3002850;
            if (versionCode >= 3002850) { //newer versions of fb app
                urlToUse = "fb://facewebmodal/f?href=" + FACEBOOK_URL;
            } else { //older versions of fb app
                urlToUse = "fb://page/" + official.getFacebook();
            }
        } catch (PackageManager.NameNotFoundException e) {
            urlToUse = FACEBOOK_URL; //normal web url
        }
        Intent facebookIntent = new Intent(Intent.ACTION_VIEW);
        facebookIntent.setData(Uri.parse(urlToUse));
        startActivity(facebookIntent);
    }


    private boolean connected(){
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnected();
    }



}