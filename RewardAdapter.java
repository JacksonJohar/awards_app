package com.johar_rewards.johar_rewards;

import android.content.Context;
import android.view.View;

import java.text.DecimalFormat;
import java.util.*;
import android.widget.TextView;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.view.LayoutInflater;

import com.johar_rewards.johar_rewards.R;


public class RewardAdapter extends RecyclerView.Adapter <
        RewardAdapter.MyViewHolder > {
    private List <Reward>
            stockList;
    private OnItemClickListener mListener;
    private OnLongItemClickListener mlListener;
    public interface OnItemClickListener {
        void onItemClick(int position);
    }
    public interface OnLongItemClickListener {
        void onLongItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }
    public void setOnLongItemClickListener(OnLongItemClickListener listener) {
        mlListener = listener;
    }
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, value, comment, date;
        private Context context;

        public MyViewHolder(View view) {
            super(view);
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    if (mlListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION){
                            mlListener.onLongItemClick(position);
                            //deleteItem(view, position);
                        }
                    }
                    return true;
                }
            });
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            mListener.onItemClick(position);
                        }
                    }
                }
            });
            name = (TextView) view.findViewById(R.id.title);
            comment = (TextView) view.findViewById(R.id.position);
            value = (TextView) view.findViewById(R.id.value);
            date = (TextView) view.findViewById(R.id.date);
        }
    }

    public RewardAdapter(List <Reward> stockList) {
        this.stockList = stockList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.reward_row, parent, false);

        return new MyViewHolder(itemView);
    }



    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        DecimalFormat df = new DecimalFormat("0.00");
        Reward reward = stockList.get(position);
        holder.name.setText(reward.getName());
        String contOut;
        holder.comment.setText(reward.getComment());
        holder.value.setText("" + reward.getValue());
        holder.date.setText(reward.getDate());

    }

    @Override
    public int getItemCount() {
        return stockList.size();
    }

    public void updateList(List<Reward> data) {
        stockList = data;
        notifyDataSetChanged();
    }

    public void deleteItem(View v, int position) {
        MyViewHolder holder =(MyViewHolder)v.getTag();
        stockList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, stockList.size());
        holder.itemView.setVisibility(View.GONE);
    }

}
