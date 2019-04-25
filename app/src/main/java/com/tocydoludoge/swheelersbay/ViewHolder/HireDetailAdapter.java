package com.tocydoludoge.swheelersbay.ViewHolder;


import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tocydoludoge.swheelersbay.Model.Hire;
import com.tocydoludoge.swheelersbay.R;

import java.util.List;

class MyViewHolder extends RecyclerView.ViewHolder
{
        public TextView name,rate,duration,discount;

    public MyViewHolder(@NonNull View itemView) {
        super(itemView);

        name=(TextView)itemView.findViewById(R.id.car_name);
        rate=(TextView)itemView.findViewById(R.id.car_rate);
        duration=(TextView)itemView.findViewById(R.id.no_days);
        discount=(TextView)itemView.findViewById(R.id.car_discount);
    }
}
public class HireDetailAdapter extends RecyclerView.Adapter<MyViewHolder> {

    List<Hire> myHires;

    public HireDetailAdapter(List<Hire> myHires) {
        this.myHires = myHires;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.hire_detail_layout,viewGroup,false);


        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {


        Hire hire=myHires.get(i);
        myViewHolder.name.setText(String.format("Name : %s",hire.getCarName()));
        myViewHolder.duration.setText(String.format("Duration : %s",hire.getDuration()));
        myViewHolder.rate.setText(String.format("Rate : %s",hire.getRate()));
        myViewHolder.discount.setText(String.format("Discount : %s",hire.getDiscount()));

    }

    @Override
    public int getItemCount() {
        return myHires.size();
    }
}
