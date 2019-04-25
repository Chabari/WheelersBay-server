package com.tocydoludoge.swheelersbay.ViewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.tocydoludoge.swheelersbay.Interface.ItemClickListener;
import com.tocydoludoge.swheelersbay.R;

import info.hoang8f.widget.FButton;

public class HireViewHolder extends RecyclerView.ViewHolder  {

    public TextView txtOrderId, txtOrderStatus, txtOrderphone, txtOrderAddress;
    public Button btnEdit,btnRemove,btnDetail,btnDirection;




    public HireViewHolder(@NonNull View itemView) {
        super(itemView);



        txtOrderAddress = itemView.findViewById(R.id.order_address);
        txtOrderId = itemView.findViewById(R.id.order_id);
        txtOrderStatus = itemView.findViewById(R.id.order_status);
        txtOrderphone = itemView.findViewById(R.id.order_address);


        btnEdit=(FButton)itemView.findViewById(R.id.btnEdit);
        btnRemove=(FButton)itemView.findViewById(R.id.btnRemove);
        btnDetail=(FButton)itemView.findViewById(R.id.btnDetails);
        btnDirection=(FButton)itemView.findViewById(R.id.btnDirection);



    }


}
