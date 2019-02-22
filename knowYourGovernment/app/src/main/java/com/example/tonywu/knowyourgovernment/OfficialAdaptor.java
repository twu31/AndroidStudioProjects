package com.example.tonywu.knowyourgovernment;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class OfficialAdaptor extends RecyclerView.Adapter<OfficialViewHolder> {


    private MainActivity mainActivity;
    private List<Official> officiallist;

    public OfficialAdaptor(List<Official> oList, MainActivity ma){

        this.officiallist = oList;
        mainActivity = ma;
    }

    @Override
    public OfficialViewHolder onCreateViewHolder(final ViewGroup viewGroup, int viewType){
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.officiallistrow, viewGroup, false);

        view.setOnLongClickListener(mainActivity);
        view.setOnClickListener(mainActivity);
        return new OfficialViewHolder(view);

    }
    @Override
    public void onBindViewHolder(OfficialViewHolder holder, int pos) {
        Official official = officiallist.get(pos);
        holder.name.setText(String.format("%s (%s)", official.getName(), official.getParty()));
        holder.office.setText(official.getOffice());
    }

    @Override
    public int getItemCount(){ return officiallist.size();}
}
