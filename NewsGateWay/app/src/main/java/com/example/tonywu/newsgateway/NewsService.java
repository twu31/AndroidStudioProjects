package com.example.tonywu.newsgateway;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import java.util.ArrayList;


public class NewsService extends Service {


    private boolean running = true;
    private ArrayList<Article> storylist = new ArrayList<>();
    private ServiceReceiver serviceReceiver;
    private NewsService newsService = this;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    /*
    public NewsService(){
        Log.d("service","gen");
        newsService.this;
    }*/

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


        IntentFilter intentFilter = new IntentFilter("AMTS");
        serviceReceiver = new ServiceReceiver();
        registerReceiver(serviceReceiver, intentFilter);
        new Thread(new Runnable() {
            @Override
            public void run() {

                while (running) {
                    while (storylist.isEmpty()) {
                        try {
                            Thread.sleep(250); //delay 250 milliseconds
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    Intent intent1 = new Intent();
                    intent1.setAction("ANS");
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("storylist", storylist);
                    intent1.putExtras(bundle);
                    //intent1.putExtra("storylist", storylist);

                    sendBroadcast(intent1);
                    storylist.clear(); //clear article list
                }
            }
        }).start();
        return Service.START_STICKY;
    }


    public void setArticles(ArrayList<Article> articles) {
        storylist.clear();  // clear article list
        for (int i = 0; i < articles.size(); i++) {
            storylist.add(articles.get(i));    // fill the article list
        }

    }

    @Override
    public void onDestroy() {
        unregisterReceiver(serviceReceiver);// unregister the service receiver
        running = false;// set running flag to false
        super.onDestroy();
    }


    class ServiceReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {

            Log.d("newsService", "intent.action " + intent.getAction());

            switch (intent.getAction()) {
                case "AMTS":
                    String sourceID = intent.getStringExtra("sourceID");
                    Log.d("newsService", "sourceID " + sourceID);
                    new NewsArticleDownloader(newsService, sourceID).execute();
                    break;
            }
        }
    }
}