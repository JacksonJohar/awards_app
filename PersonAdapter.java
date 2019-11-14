package com.johar_rewards.johar_rewards;

import android.content.Context;
import android.view.View;

import java.text.DecimalFormat;
import java.util.*;

import android.widget.ImageView;
import android.widget.TextView;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.view.LayoutInflater;

import com.johar_rewards.johar_rewards.R;


public class PersonAdapter extends RecyclerView.Adapter <
        PersonAdapter.MyViewHolder > {
    private List <Person>
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
        public TextView name, position, department, points;
        public ImageView pic;
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
            position = (TextView) view.findViewById(R.id.position);
            points = (TextView) view.findViewById(R.id.value);
            pic = (ImageView) view.findViewById(R.id.pic);
        }
    }

    public PersonAdapter(List <Person> stockList) {
        this.stockList = stockList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_row, parent, false);

        return new MyViewHolder(itemView);
    }



    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        DecimalFormat df = new DecimalFormat("0.00");
        Person person = stockList.get(position);
        holder.name.setText(person.getLastName() + ", " + person.getFirstName());
        String contOut;
        holder.position.setText(person.getPosition() + ", " + person.getDepartment());
        holder.points.setText("" + person.getPoints());
        holder.pic.setImageBitmap(person.getImage());

    }

    @Override
    public int getItemCount() {
        return stockList.size();
    }

    public void updateList(List<Person> data) {
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

