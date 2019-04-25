package com.tocydoludoge.swheelersbay;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.tocydoludoge.swheelersbay.Common.Common;
import com.tocydoludoge.swheelersbay.ViewHolder.HireDetailAdapter;


public class HireDetails extends AppCompatActivity {

    TextView hire_id,hire_phone,hire_address,hire_total,hire_comment;
    String hire_id_value="";
    RecyclerView lstCars;
    RecyclerView.LayoutManager layoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hire_details);
        hire_id=(TextView)findViewById(R.id.order_id);
        hire_phone=(TextView)findViewById(R.id.order_phone);
        hire_address=(TextView)findViewById(R.id.order_address);
        hire_total=(TextView)findViewById(R.id.order_total);
        hire_comment=(TextView)findViewById(R.id.order_comment);


            lstCars=(RecyclerView)findViewById(R.id.lstCars);
            lstCars.setHasFixedSize(true);
            layoutManager=new LinearLayoutManager(this);
            lstCars.setLayoutManager(layoutManager);

        if(getIntent()!=null)
            hire_id_value=getIntent().getStringExtra("HireId");

        //setValue
        hire_id.setText(hire_id_value);
        hire_phone.setText(Common.currentRequest.getPhone());
        hire_address.setText(Common.currentRequest.getAddress());
        hire_total.setText(Common.currentRequest.getTotal());
        hire_comment.setText(Common.currentRequest.getComment());

        HireDetailAdapter adapter= new HireDetailAdapter(Common.currentRequest.getCars());
        adapter.notifyDataSetChanged();
        lstCars.setAdapter(adapter);
    }
}
