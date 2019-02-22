package com.example.tonywu.multinotes;


import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class noteAdaptor extends RecyclerView.Adapter<MyViewHolder>{

    private List<notedetails> noteList;
    private MainActivity mainAct;

    public noteAdaptor(List<notedetails> noteList, MainActivity main){
        this.noteList = noteList;
        this.mainAct = main;
    }

    @Override
    public MyViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        Log.d("Note Adaptor", "onCreateViewHolder: Making New");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_list_row, parent, false);

        view.setOnClickListener(mainAct);
        view.setOnLongClickListener(mainAct);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        notedetails note = noteList.get(position);
        holder.nTitle.setText(note.getTitle());
        holder.nDate.setText(note.getDate());
        String cardlength = note.getNote();

        if (cardlength.length() > 80){
            cardlength = cardlength.substring(0, 80)+"...";
        }
        holder.nContent.setText(cardlength);
    }

    @Override
    public int getItemCount() {
        return noteList.size();
    }



}

