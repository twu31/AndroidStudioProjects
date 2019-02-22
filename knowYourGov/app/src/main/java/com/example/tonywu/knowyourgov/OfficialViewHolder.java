package com.example.tonywu.knowyourgov;


import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;


public class OfficialViewHolder extends RecyclerView.ViewHolder {

    public TextView office;
    public TextView name;


    public OfficialViewHolder(View view){
        super(view);

        office = view.findViewById(R.id.officeID);
        name = view.findViewById(R.id.nameID);

    }

}