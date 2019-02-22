package com.example.tonywu.knowyourgovernment;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

public class OfficialViewHolder extends RecyclerView.ViewHolder {


    public TextView office;
    public TextView name;

    public OfficialViewHolder(View v){
        super(v);

        office = v.findViewById(R.id.officeID);
        name = v.findViewById(R.id.nameID);

    }
}
