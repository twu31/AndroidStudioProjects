package com.example.tonywu.newsgateway;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


public class NewsArticleDownloader extends AsyncTask<String, Void, String> {

    private static final String URLpreffix = "https://newsapi.org/v1/articles?source=";
    private static final String URLend = "&apiKey=f4d9182f45d74ed79d68cbb0cd6547f8";


    private NewsService newsService;
    private String newsSource;


    public NewsArticleDownloader(NewsService newsService, String newsSource){
        this.newsService = newsService;
        this.newsSource = newsSource;
    }

    @Override
    protected void onPreExecute() {
        Log.d("newsarticledownloader", "preExec");

    }

    @Override
    protected void onPostExecute(String s) {
        //Log.d("newsarticledownloader", "postExec ");
        try{
            newsService.setArticles(parseJSON(s));
        } catch (Exception e){
            Log.d("newsarticledownloader", "error message: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    protected String doInBackground(String... strings) {
        String dataURL = URLpreffix + newsSource + URLend;
        Log.d("newsarticle", "url " + dataURL);

        Uri dataUri = Uri.parse(dataURL);
        String urlToUse = dataUri.toString();
        Log.d("newsarticle", "doInBackground: " + urlToUse);

        StringBuilder stringBuilder = new StringBuilder();
        try {
            URL url = new URL(urlToUse);

            HttpURLConnection httpURLConnectionconn = (HttpURLConnection) url.openConnection();
            httpURLConnectionconn.setRequestMethod("GET");
            InputStream inputStream = httpURLConnectionconn.getInputStream();
            BufferedReader reader = new BufferedReader((new InputStreamReader(inputStream)));

            //Log.d("newsarticledownloader", "doInBackground: A");
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line).append('\n');
            }

            //Log.d("newsarticledownloader", "toString " + sb.toString());

        }
        catch (FileNotFoundException e){
            return null;
        }
        catch (Exception e) {
            Log.e("newsarticle download", "doInBackground: ", e);
            return null;
        }

        //Log.d("newsarticledownal, "doInBackground: " + sb.toString());
        //Log.d("newsarticledownloader", "doInBackground: returning");
        return stringBuilder.toString();
    }


    private ArrayList<Article> parseJSON(String s){
        Log.d("newsarticledownloader", "parseJSON: parsing articles ");

        ArrayList<Article> articleList = new ArrayList<>();



        Log.d("newsarticle downloader", "parseJSON: " + s);
        try{
            JSONObject entire = new JSONObject(s);
            JSONArray articles = entire.getJSONArray("articles");

            for(int i = 0; i < articles.length(); i++){
                JSONObject obj = articles.getJSONObject(i);
                String author = obj.getString("author");
                String title = obj.getString("title");
                String description = obj.getString("description");
                String urlToImage = obj.getString("urlToImage");
                String publishedAt = obj.getString("publishedAt");

                //EC
                String urlToArticle = obj.getString("url");
                Log.d("newsarticledownloader", "parseJSON: title " + title);
                Log.d("newsarticledownloader", "parseJSON: url " + urlToArticle);

                articleList.add( new Article(author,title,description,urlToImage,publishedAt, urlToArticle));
            }

            return articleList;

        }catch (Exception e){
            Log.d("newsarticledownloader", "parseJSON: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
}
