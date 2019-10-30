package com.example.gongtia.lifestyle.activity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gongtia.lifestyle.R;
import com.example.gongtia.lifestyle.Room.StepCounterData;

import java.util.LinkedList;
import java.util.List;
public class StepCounterRVAdapter extends RecyclerView.Adapter<StepCounterRVAdapter.ViewHolder> {
    List<StepCounterData> mStepsList;
    private Context mContext;

    public StepCounterRVAdapter(List<StepCounterData> input) {
        mStepsList = input;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView stID, date , steps;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            stID = itemView.findViewById(R.id.stepcounter_id);
            date = itemView.findViewById(R.id.stepcounter_date);
            steps = itemView.findViewById(R.id.stepcounter_steps);
        }
    }


    @NonNull
    @Override
    public StepCounterRVAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        LayoutInflater lf = LayoutInflater.from(mContext);
        View myView = lf.inflate(R.layout.step_counter_rv_itemview, parent, false);
        ViewHolder holder = new ViewHolder(myView);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull StepCounterRVAdapter.ViewHolder holder, int position) {
        holder.stID.setText(""+(position+1));
        holder.date.setText(mStepsList.get(position).date);
        holder.steps.setText(""+mStepsList.get(position).steps);
    }

    @Override
    public int getItemCount() {
        return mStepsList.size();
    }
}
