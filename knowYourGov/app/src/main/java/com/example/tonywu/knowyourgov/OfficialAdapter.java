package com.example.tonywu.knowyourgov;



import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;


public class OfficialAdapter extends RecyclerView.Adapter<OfficialViewHolder> {


    private List<Official> officialList;
    private MainActivity mainActivity;


    public OfficialAdapter(List<Official> offList, MainActivity ma){
        this.officialList = offList;
        mainActivity = ma;
    }

    @Override
    public OfficialViewHolder onCreateViewHolder(final ViewGroup viewGroup, int viewType){
        Log.d("OfficialAdapter", "onCreate");
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.officiallistrow, viewGroup, false);

        itemView.setOnClickListener(mainActivity);
        itemView.setOnLongClickListener(mainActivity);

        return new OfficialViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(OfficialViewHolder holder, int pos){
        Official official = officialList.get(pos);
        holder.name.setText(String.format("%s (%s)",official.getName(),official.getParty()));
        holder.office.setText(official.getOffice());
    }

    @Override
    public int getItemCount(){ return officialList.size();}
}