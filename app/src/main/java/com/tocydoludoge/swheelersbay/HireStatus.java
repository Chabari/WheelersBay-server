package com.tocydoludoge.swheelersbay;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.tocydoludoge.swheelersbay.Common.Common;
import com.tocydoludoge.swheelersbay.Interface.ItemClickListener;
import com.tocydoludoge.swheelersbay.Model.Request;
import com.tocydoludoge.swheelersbay.ViewHolder.HireViewHolder;

public class HireStatus extends AppCompatActivity {
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    MaterialSpinner spinner;

    FirebaseRecyclerAdapter<Request, HireViewHolder> adapter;

    FirebaseDatabase db;
    DatabaseReference requests;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hire_status);

        //Init Firebase
        db = FirebaseDatabase.getInstance();
        requests = db.getReference("Requests");

        //Init
        recyclerView = findViewById(R.id.listOrders);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        loadOrders(); // Memuat semua orderan
    }

    private void loadOrders() {
        adapter = new FirebaseRecyclerAdapter<Request, HireViewHolder>(
                Request.class,
                R.layout.hire_layout,
                HireViewHolder.class,
                requests
        ) {
            @Override
            protected void populateViewHolder(HireViewHolder viewHolder, final Request model, final int position) {
                viewHolder.txtOrderId.setText(adapter.getRef(position).getKey());
                viewHolder.txtOrderStatus.setText(Common.convertCodeToStatus(model.getStatus()));
           //     viewHolder.txtOrderAddres.setText(model.getAddress());
            //    viewHolder.txtOrderPhone.setText(model.getPhone());


                viewHolder.btnEdit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showUpdateDialog(adapter.getRef(position).getKey(),
                                adapter.getItem(position));


                    }
                });

                viewHolder.btnRemove.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        deleteOrder(adapter.getRef(position).getKey());
                    }
                });
                        viewHolder.btnDetail.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                Intent hireDetails = new Intent(HireStatus.this,HireDetails.class);
                                Common.currentRequest = model;
                                hireDetails.putExtra("HireId",adapter.getRef(position).getKey());
                                startActivity(hireDetails);

                            }
                        });
                        viewHolder.btnDirection.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent trackingOrder = new Intent(HireStatus.this,HireTrack.class);
                                Common.currentRequest = model;
                                startActivity(trackingOrder);

                            }
                        });


                    }
        };
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
    }



    private void deleteOrder(String key) {
        requests.child(key).removeValue();
        adapter.notifyDataSetChanged();
    }

    private void showUpdateDialog(String key, final Request item) {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(HireStatus.this);
        alertDialog.setTitle("Update Hire ");
        alertDialog.setMessage("Please Choose Status..");

        LayoutInflater inflater = this.getLayoutInflater();
        final View view = inflater.inflate(R.layout.update_hire_layout, null);

        spinner = view.findViewById(R.id.statusSpinner);
        spinner.setItems("Hire Placed", "Wait for Approval for Payment", "Come Pick Vehicle");

        alertDialog.setView(view);

        final String localKey = key;
        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                item.setStatus(String.valueOf(spinner.getSelectedIndex()));

                requests.child(localKey).setValue(item);
                adapter.notifyDataSetChanged(); //add to item size
            }
        });

        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        alertDialog.show();

    }
}