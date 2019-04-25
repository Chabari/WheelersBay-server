package com.tocydoludoge.swheelersbay.ViewHolder;


import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tocydoludoge.swheelersbay.Common.Common;
import com.tocydoludoge.swheelersbay.Interface.ItemClickListener;
import com.tocydoludoge.swheelersbay.R;

public class CarViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
        View.OnCreateContextMenuListener



{

    public TextView carName;
    public ImageView carImage;

    private ItemClickListener itemClickListener;

    public CarViewHolder(View itemView){
        super(itemView);

        carName = itemView.findViewById(R.id.car_name);
        carImage = itemView.findViewById(R.id.car_image);


        itemView.setOnCreateContextMenuListener(this);
        itemView.setOnClickListener(this);

    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View view)
    {
        itemClickListener.onClick(view, getAdapterPosition(), false);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.setHeaderTitle("Select The Action");

        menu.add(0,0,getAdapterPosition(), Common.UPDATE);
        menu.add(0,1,getAdapterPosition(), Common.DELETE);
    }
}