package com.example.tonywu.stockwatch;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class StockAdaptor extends RecyclerView.Adapter<StockHolder> {

    private List<Stock> stockList;
    private MainActivity mainAct;

    public StockAdaptor(MainActivity mainAct, List<Stock> stockList){
        this.mainAct = mainAct;
        this.stockList = stockList;
    }


    @Override
    public StockHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.stock_list_row, viewGroup, false);
        view.setOnClickListener(mainAct);
        view.setOnLongClickListener(mainAct);
        return new StockHolder(view);
    }

    @Override
    public void onBindViewHolder(StockHolder hold, int pos) {
        Stock stock = stockList.get(pos);
        hold.name.setText(stock.getName());
        hold.price.setText(String.valueOf(stock.getPrice()));
        hold.symbol.setText(stock.getSymbol());

        if (stock.getPchange() > 0) {

            hold.pchange.setText("▲ " + String.valueOf((stock.getPchange()) + "(" + String.valueOf(stock.getChangePercent()) + "%)"));
            hold.name.setTextColor(Color.GREEN);
            hold.price.setTextColor(Color.GREEN);
            hold.pchange.setTextColor(Color.GREEN);
            hold.symbol.setTextColor(Color.GREEN);

        } else {
            hold.pchange.setText("▼ " + String.valueOf((stock.getPchange()) + "(" + String.valueOf(stock.getChangePercent()) + "%)"));
            hold.name.setTextColor(Color.RED);
            hold.price.setTextColor(Color.RED);
            hold.pchange.setTextColor(Color.RED);
            hold.symbol.setTextColor(Color.RED);
        }
    }
    @Override
    public int getItemCount(){
        return stockList.size();
        }


}
