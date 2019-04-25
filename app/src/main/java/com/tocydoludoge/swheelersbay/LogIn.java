package com.tocydoludoge.swheelersbay;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tocydoludoge.swheelersbay.Common.Common;
import com.tocydoludoge.swheelersbay.Model.User;

import org.w3c.dom.Text;

import info.hoang8f.widget.FButton;

public class LogIn extends AppCompatActivity {


    FirebaseDatabase database;
    DatabaseReference users;
    Button btnLogIn;
    TextView txtSlogan, edtphone, edtpassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);


        txtSlogan = (TextView) findViewById(R.id.txtSlogan);
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/Nabila.ttf");
        txtSlogan.setTypeface(typeface);

        btnLogIn = (FButton) findViewById(R.id.btnSignIn);

        edtphone = (TextView) findViewById(R.id.edtPhone);
        edtpassword = (TextView) findViewById(R.id.edtPassword);

        ///firebase connection
        database = FirebaseDatabase.getInstance();
        users = database.getReference("User");


        btnLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                logInUser(edtphone.getText().toString(), edtpassword.getText().toString());
            }

            private void logInUser(String phone, String password) {

                final ProgressDialog mDialog = new ProgressDialog(LogIn.this);
                mDialog.setMessage("Please Wait...");
                mDialog.show();


                final String localPhone = phone;
                final String localPassword = password;
                users.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.child(localPhone).exists()) {

                            mDialog.dismiss();
                            User user = dataSnapshot.child(localPhone).getValue(User.class);
                            user.setPhone(localPhone);
                            //check is  user isstaff=true
                            if (Boolean.parseBoolean(user.getIsStaff())) {
                                if (user.getPassword().equals(localPassword)) {
                                    //login
                                    Intent home = new Intent(LogIn.this, Home.class);
                                    Common.currentUser = user;
                                    startActivity(home);
                                    finish();
                                } else

                                    Toast.makeText(LogIn.this, "Wrong Password", Toast.LENGTH_SHORT).show();

                            } else

                                Toast.makeText(LogIn.this, "Please LogIn with Staff Account", Toast.LENGTH_SHORT).show();
                        } else{
                            mDialog.dismiss();
                        Toast.makeText(LogIn.this, "User Does Not Exist", Toast.LENGTH_SHORT).show();
                    }
                }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

    }
}
