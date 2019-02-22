package com.example.tonywu.newsgateway;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import static android.graphics.Color.BLACK;


public class MainActivity extends AppCompatActivity {



    private MainActivity mainActivity = this;

    private NewsReceiver newsReceiver;


    private HashMap<String,Source> sourceHashMap = new HashMap<>(); //keys, values
    private ArrayList<String> sourceNamesList = new ArrayList<>();
    private HashMap<String, ArrayList<String> > categoryHashMap = new HashMap<>();


    // PageViewer & Fragments
    private MyPageAdapter pageAdapter;
    private List<Fragment> fragments;
    private ViewPager pager;
    private DrawerLayout drawerLayout;
    private ListView drawerList;
    private ActionBarDrawerToggle drawerToggle;
    private Menu opt_menu;
    private String currentCategory = "";
    private String currentSource = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setTitle("News Gateway");


        Intent intent = new Intent(MainActivity.this, NewsService.class);
        startService(intent);


        IntentFilter intentFilter = new IntentFilter("ANS");


        newsReceiver = new NewsReceiver();
        registerReceiver(newsReceiver, intentFilter);



        drawerLayout = findViewById(R.id.drawer_layout);
        drawerList = findViewById(R.id.drawer_list);

        drawerList.setAdapter(new ArrayAdapter<>(this,
                R.layout.drawer, sourceNamesList));

        drawerList.setBackgroundResource(R.color.secondaryColor);


        // Drawer Item Selected
        drawerList.setOnItemClickListener(
                new ListView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        // set Viewpage background to null during the transition
                        pager.setBackground(null);

                        //set the current name of the source
                        String current = sourceNamesList.get(position);
                        getSupportActionBar().setTitle(current);

                        Intent intentClick = new Intent();
                        intentClick.setAction("AMTS");
                        Log.d("mainactivity", "intent to service");

                        Source source = sourceHashMap.get(sourceNamesList.get(position));

                        Log.d("mainAct", "add select source " + source.getId());
                        intentClick.putExtra("sourceID", source.getId());


                        sendBroadcast(intentClick);
                        //Log.d("mainACt", " broadcast sent");


                        drawerLayout.closeDrawer(drawerList);

                        Log.d("mainActivity", "sourceClicked " + sourceNamesList.get(position));
                    }
                }
        );


        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.drawer_open, R.string.drawer_close);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        //page vieweer
        fragments = new ArrayList<>();
        pageAdapter = new MyPageAdapter(getSupportFragmentManager());
        pager = findViewById(R.id.viewpager);
        pager.setAdapter(pageAdapter);
        //async task
        new NewsSourceDownloader(mainActivity, "").execute();

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume(){

        super.onResume();
    }



    @Override
    protected void onDestroy() {
        unregisterReceiver(newsReceiver);
        Intent intent = new Intent(mainActivity, NewsService.class);
        stopService(intent);
        super.onDestroy();
    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // configure change
        drawerToggle.onConfigurationChanged(newConfig);
    }


    public boolean onOptionsItemSelected(MenuItem item) {

        if (drawerToggle.onOptionsItemSelected(item)) {
            Log.d("mainactivity", "onOptionsItemSelected" + item);
            return true;
        }



        String category = (String) item.getTitle(); // This is a category title
        //Log.d("mainactivity, "title " + category);
        currentCategory = category;

        ArrayList<String> temp = new ArrayList<>(categoryHashMap.get(category)); // what does this return? do I need to cast it for addAll


        sourceNamesList.clear();
        sourceNamesList.addAll(temp);
        ((ArrayAdapter) drawerList.getAdapter()).notifyDataSetChanged();
        return super.onOptionsItemSelected(item);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        opt_menu = menu;
        return true;
    }


    public void reDoFragments(ArrayList<Article> articleList){

        for(int i = 0; i < pageAdapter.getCount(); i++ ){
            pageAdapter.notifyChangeInPosition(i);
        }


        fragments.clear();


        int totalArticles = articleList.size();
        for(int i = 0; i < totalArticles; i++){
            Article article = articleList.get(i);

            fragments.add(MyFragment.newInstance( mainActivity, article.getTitle(), article.getPublishedAt(), article.getAuthor(), article.getUrlToImage(), article.getDescription(), i, totalArticles, article.getUrlArticle() ));
        }

        pageAdapter.notifyDataSetChanged();
        pager.setCurrentItem(0);


    }


    public void setSources(ArrayList<Source> sources, ArrayList<String> categories){


        sourceHashMap.clear();
        sourceNamesList.clear();
        categoryHashMap.clear();


        Source source; String category; String name;

        for (int i = 0; i < sources.size(); i++ ){
            source = sources.get(i);
            category = source.getCategory();
            //Log.d("mainAct", "catergory: " + category);
            name = source.getName();
            sourceNamesList.add(name);
            sourceHashMap.put(name, source);

            if(! categoryHashMap.containsKey(category)){
                //Log.d("mainActivity, "key catergory:  " + category);
                categoryHashMap.put(category, new ArrayList<String>());
            }
            categoryHashMap.get(category).add(name);
        }


        Collections.sort(categories);
        for(String s : categories){
            opt_menu.add(s);
        }

        ((ArrayAdapter) drawerList.getAdapter()).notifyDataSetChanged();


    }






    public class NewsReceiver extends BroadcastReceiver {


        @Override
        public void onReceive(Context context, Intent intent){

            switch (intent.getAction()){
                case "ANS":
                    try{
                        Bundle bundle = intent.getExtras();
                        ArrayList<Article> articles = (ArrayList<Article>) bundle.getSerializable("storylist");
                        Log.d("newsreciever", "work");
                        for(int i = 0; i <articles.size(); i++){
                            Log.d("newsrec", "title: " + articles.get(i).getTitle());
                        }

                        // call reDoFragments, passing list of articles
                        reDoFragments(articles);

                    }
                    catch (Exception e){
                        Log.d("newsrec", "error: " + e.getMessage());
                        e.printStackTrace();
                    }
                    break;

            }

        }


    }

    private class MyPageAdapter extends FragmentPagerAdapter {
        private long baseId = 0;


        public MyPageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getItemPosition(@NonNull Object object) {
            return POSITION_NONE;
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Override
        public long getItemId(int position) {
            // new id for reassignement
            return baseId + position;
        }


        public void notifyChangeInPosition(int n) {
            //shift id out of fragments
            baseId += getCount() + n;
        }

    }
}