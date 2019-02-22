package com.example.tonywu.stockwatch;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;
import android.widget.TextView;

public class StockHolder extends RecyclerView.ViewHolder {
    public TextView symbol;
    public TextView name;

    public TextView price;
    public TextView pchange;

    public StockHolder(View item){
        super(item);
        symbol = (TextView) itemView.findViewById(R.id.symbol);
        name = (TextView) itemView.findViewById(R.id.name);
        price = (TextView) itemView.findViewById(R.id.price);
        pchange = (TextView) itemView.findViewById(R.id.pchange);
    }
}
