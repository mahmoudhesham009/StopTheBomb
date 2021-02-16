package com.target.runningapp.adapter;


import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.target.runningapp.R;
import com.target.runningapp.model.HistoryMission;

import java.util.ArrayList;

public class HistoryRecyclerViewAdapter extends RecyclerView.Adapter<HistoryRecyclerViewAdapter.HistoryViewHolder> {
    ArrayList<HistoryMission> historyMissions=new ArrayList<>();
    Context mContext;

    public HistoryRecyclerViewAdapter(ArrayList<HistoryMission> historyMissions) {
        this.historyMissions = historyMissions;
    }

    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.history_item, parent, false);

        return new HistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryViewHolder holder, int position) {
        holder.distance.setText("Km\n"+String.format("%.2f", historyMissions.get(position).getDistance() / 1000));
        if(historyMissions.get(position).getTime()/60<=9&&historyMissions.get(position).getTime()%60<=9){
            holder.time.setText("Time\n"+"0"+historyMissions.get(position).getTime()/60+" : 0"+historyMissions.get(position).getTime()%60);
        }else if(historyMissions.get(position).getTime()/60<=9){
            holder.time.setText("Time\n"+"0"+historyMissions.get(position).getTime()/60+" : "+historyMissions.get(position).getTime()%60);
        }else if(historyMissions.get(position).getTime()%60<=9){
            holder.time.setText("Time\n"+historyMissions.get(position).getTime()/60+" : 0"+historyMissions.get(position).getTime()%60);
        }else{
            holder.time.setText("Time\n"+historyMissions.get(position).getTime()/60+" : "+historyMissions.get(position).getTime()%60);
        }
        if(historyMissions.get(position).isWin()){
            holder.state.setText("Win");
            holder.state.setTextColor(Color.GREEN);
        }
        else{
            holder.state.setText("Loss");
            holder.state.setTextColor(Color.RED);
        }
    }

    @Override
    public int getItemCount() {
        return historyMissions.size();
    }

    class HistoryViewHolder extends RecyclerView.ViewHolder {
        TextView distance,time,state;

        public HistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            distance=itemView.findViewById(R.id.km);
            time=itemView.findViewById(R.id.time);
            state=itemView.findViewById(R.id.winOrNot);
        }
    }
}
