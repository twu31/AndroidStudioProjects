package com.example.tonywu.newsgateway;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;



public class MyFragment extends Fragment implements View.OnClickListener {


    public static MainActivity mainActivity;
    public static ArrayList<String> fragmentUrls;

    public TextView articleTitle;
    public TextView articleDate;
    public TextView articleAuthor;
    public ImageView articleImage;
    public TextView articleDescription;
    public TextView articleCount;

    public static final MyFragment newInstance(MainActivity ma, String title, String date, String author, String image, String description, int i, int n, String url) {
        mainActivity = ma;
        //Log.d("myFrag", "url: " + url);
        //fragmentUrls.add(url);

        //Log.d("frag", "title " + title);
        MyFragment f = new MyFragment();
        Bundle bdl = new Bundle(5);
        bdl.putString("title", title);
        bdl.putString("date", date);
        bdl.putString("author", author);
        bdl.putString("image", image);
        bdl.putString("description", description);
        bdl.putInt("i", i);
        bdl.putInt("n", n);

        f.setArguments(bdl);
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //final Articile art;
        //if(savedInstanceState == null){
            //art = (Article) getArguments().getSerializable("art");
            //else{
              //  art = (Article) savedInstanceState.getSerializable("art");
            //}
        //}

        View v = inflater.inflate(R.layout.fragment_layout, container, false);
        articleTitle = v.findViewById(R.id.titleID);
        articleDate = v.findViewById(R.id.dateID);
        articleAuthor = v.findViewById(R.id.authorID);
        articleImage = v.findViewById(R.id.imageID);
        articleDescription = v.findViewById(R.id.descriptionID);
        articleCount = v.findViewById(R.id.countID);

        articleTitle.setOnClickListener(this);
        articleImage.setOnClickListener(this);
        articleDescription.setOnClickListener(this);


        //articleTitle.setText(title);
        //articleDate.setText(date);
        //articleAuthor.setText(author);
        //articleDescription.setText(description);
        //articleCount.setText((i + 1) + " of " + n);


        String title = (getArguments().getString("title") != null ? getArguments().getString("title") : "");
        String date = getArguments().getString("date");
        if (date == null || date.equals("null")) {
            articleDate.setVisibility(View.GONE);
        }

        String author = getArguments().getString("author");
        if (author == null || author.equals("null")) {
            articleAuthor.setVisibility(View.GONE);
        }

        String image = getArguments().getString("image");

        String description = getArguments().getString("description");
        if (description == null || description.equals("null") || description.isEmpty()) {
            description = getString(R.string.no_description);

        }


        Log.d("frag", "description: " + description);

        int i = getArguments().getInt("i");
        int n = getArguments().getInt("n");

        //Log.d("frag", "oncreateView");

        articleTitle.setText(title);
        articleDate.setText(date);
        articleAuthor.setText(author);
        articleDescription.setText(description);
        articleCount.setText((i + 1) + " of " + n); // so start at 1

        articleDescription.setMovementMethod(new ScrollingMovementMethod());
        articleImage.setImageResource(R.drawable.hourglass_img);

        if (connected()) {

            if (image == null) {
                Log.d("frag", "onCreateView: null image url");
                articleImage.setVisibility(View.GONE); // ???? What do here
            } else {
                final String photoUrl = image;
                Picasso picasso = new Picasso.Builder(mainActivity).listener(new Picasso.Listener() {
                    @Override
                    public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) { final String changedUrl = photoUrl.replace("http:", "https:");
                    picasso.load(changedUrl).error(R.drawable.broken_img).placeholder(R.drawable.hourglass_img).into(articleImage);

                    }
                }).build();

                picasso.load(photoUrl).error(R.drawable.broken_img).placeholder(R.drawable.hourglass_img).into(articleImage);
            }
        } else {
            Log.d("frag", "no internet");
        }


        return v;
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.titleID:
            case R.id.imageID:
            case R.id.descriptionID:
                Log.d("frag", "onClick: clicked on title, image, or description");
                View q = (View) v.getParent();

                //Intent intent = new Intent(Intent.ACTION_VIEW);
                //intent.setData(Uri.parse(articleURL));
                //startActivity(intent);
                //startActivity(intent)
                break;
            default:
                Log.d("frag", "click");
        }
    }


    private boolean connected() {
        ConnectivityManager cm = (ConnectivityManager) mainActivity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnected();
    }
}