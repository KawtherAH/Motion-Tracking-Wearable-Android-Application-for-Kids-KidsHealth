package com.example.kidshealthv3;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AdapterStepTable extends RecyclerView.Adapter<AdapterStepTable .ViewHolder> {

    List<StatRecordModal> statRecordModalList;

    public AdapterStepTable (List<StatRecordModal> list) {
        this.statRecordModalList=list;
    }

    @NonNull
    @Override
    public AdapterStepTable .ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.stat_card,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterStepTable .ViewHolder holder, int position) {
        StatRecordModal modal=statRecordModalList.get(position);
        {
            {
                holder.date.setText(modal.getDate());
                holder.steps.setText(modal.getSteps() + " Steps");
                if (modal.getStreakAchieved()) {
                    holder.streatStatus.setVisibility(View.VISIBLE);
                } else {
                    holder.streatStatus.setVisibility(View.INVISIBLE);
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return statRecordModalList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView date,steps;
        ImageButton streatStatus;
        CoordinatorLayout statViewCard;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            date=itemView.findViewById(R.id.date);
            steps=itemView.findViewById(R.id.statSteps);

            statViewCard=itemView.findViewById(R.id.statViewCard);
            statViewCard.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

        }
    }
}