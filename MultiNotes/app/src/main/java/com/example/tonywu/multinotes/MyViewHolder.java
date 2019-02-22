package com.example.tonywu.multinotes;

import android.support.v7.widget.RecyclerView;
import android.widget.TextView;
import android.view.View;

public class MyViewHolder extends RecyclerView.ViewHolder {

    public TextView nDate;
    public TextView nContent;
    public TextView nTitle;


    public MyViewHolder(View view){

        super(view);

        nDate = (TextView) view.findViewById(R.id.nDate);
        nContent = (TextView) view.findViewById(R.id.nContent);
        nTitle = (TextView) view.findViewById(R.id.nTitle);

    }
}
