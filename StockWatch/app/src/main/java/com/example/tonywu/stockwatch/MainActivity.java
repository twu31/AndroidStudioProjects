package com.example.tonywu.stockwatch;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener{

    private RecyclerView recyclerView;
    private List<Stock> stockList = new ArrayList<>();
    private StockAdaptor sAdaptor;
    private ConnectivityManager connectivityManager;
    private static MainActivity mainAct;
    private DatabaseHelper databaseHelper;
    private SwipeRefreshLayout swipeRefreshLayout;

    private static String marketURL = "http://www.marketwatch.com/investing/stock/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        mainAct = this;

        recyclerView = (RecyclerView)  findViewById(R.id.recycler);
        sAdaptor = new StockAdaptor(this, stockList);
        recyclerView.setAdapter(sAdaptor);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        sAdaptor.notifyDataSetChanged();

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swiper);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Toast.makeText(MainActivity.this, "refreshed", Toast.LENGTH_SHORT).show();

                if (!netCheck() || stockList.size() == 0){
                    swipeRefreshLayout.setRefreshing(false);
                    return;
                }
                for(Stock stock: stockList){
                    //Asyncypdate
                    AsyncUpdate asyncUpdate = new AsyncUpdate(mainAct);
                    asyncUpdate.execute(stock);
                }
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        if (!netCheck()){
            return;
        }

        databaseHelper = new DatabaseHelper(this);
      ArrayList<String[]> stocks = databaseHelper.loadStocks();
        for(String[] tmp: stocks){
            Stock stemp = new Stock(tmp[0],tmp[1]);
            AsyncData asyncData = new AsyncData(mainAct);
            asyncData.execute(stemp);
            //
        }
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        //Intent intent;
        switch(item.getItemId()){
            case R.id.menuAdd:

               /* Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse("http://google.com"));
                startActivity(intent);*/
                addStockbutton();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    public void addStockbutton(){
        LayoutInflater inflater = LayoutInflater.from(this);
        final View view = inflater.inflate(R.layout.stock_dialog, null);
        //final View view = inflater.inflate(R.layout.stock_dialog, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Stock");
        builder.setMessage("Enter a stock");
        builder.setView(view);

        builder.setPositiveButton("SEARCH", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                EditText text = (EditText) view.findViewById(R.id.inputTextView);
                text.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
                String input = text.getText().toString();
                if(netCheck()){
                    //  Async
                    //}
                    AsyncSymbol asyncSymbol = new AsyncSymbol(mainAct);
                    asyncSymbol.execute(input);
                }

            }
        });
        builder.setNegativeButton("Exit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //leave

            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }

    @Override
    public void onClick(View view){
        final int pos = recyclerView.getChildLayoutPosition(view);
        Stock stk = stockList.get(pos);
        String url = marketURL+stk.getSymbol();
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        startActivity(intent);
    }



    @Override
    public boolean onLongClick(View view){
        final int pos = recyclerView.getChildLayoutPosition(view);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete");
        builder.setMessage("Delete this stock?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.d("position: ", ""+pos);
                databaseHelper.deleteStock(stockList.get(pos).getSymbol());
                stockList.remove(pos);
                sAdaptor.notifyDataSetChanged();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //do nothing
                return;
            }
        });
        AlertDialog alert = builder.create();
        alert.show();

        return false;
    }

    public boolean netCheck(){
        //NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager == null){
            Toast.makeText(this,"cannot access connectivitymanager",Toast.LENGTH_SHORT).show();
        }
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if(networkInfo !=null && networkInfo.isConnected()){
            return true;
        }
        else{
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Network error");
            builder.setMessage("No connection to internet");
            builder.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //leave
                }
            });
            AlertDialog alert = builder.create();
            alert.show();

            return false;
        }
    }


    public void addData(Stock stock){
        for(Stock each: stockList){
            if(each.getSymbol().equals(stock.getSymbol())) return;
        }
        stockList.add(stock);
        Collections.sort(stockList, new Comparator<Stock>() {
            @Override
            public int compare(Stock s1, Stock s2) {
                return s1.getSymbol().compareTo(s2.getSymbol());
            }
        });

        databaseHelper.addStock(stock);
        sAdaptor.notifyDataSetChanged();
    }

    public void updateData(Stock stock){
        int i=0;
        for(Stock each: stockList){
            if (each.getSymbol().equals(stock.getSymbol())) break;
            i++;
        }
        stockList.remove(i);
        stockList.add(i,stock);
        sAdaptor.notifyDataSetChanged();
    }
}
