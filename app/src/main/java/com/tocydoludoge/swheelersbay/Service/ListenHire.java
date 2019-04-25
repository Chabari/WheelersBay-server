package com.tocydoludoge.swheelersbay.Service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.tocydoludoge.swheelersbay.HireStatus;
import com.tocydoludoge.swheelersbay.Model.Hire;
import com.tocydoludoge.swheelersbay.Model.Request;
import com.tocydoludoge.swheelersbay.R;

import java.util.Date;
import java.util.Random;

public class ListenHire extends Service implements ChildEventListener {

    FirebaseDatabase db;
    DatabaseReference hires;

    @Override
    public void onCreate() {
        super.onCreate();
        db=FirebaseDatabase.getInstance();
        hires=db.getReference("Requests");

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        hires.addChildEventListener(this);
        return super.onStartCommand(intent, flags, startId);
    }

    public ListenHire() {
    }

    @Override
    public IBinder onBind(Intent intent) {
     return  null;
    }

    @Override
    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
        //trigger
        Request request= dataSnapshot.getValue(Request.class);
        if(request.getStatus().equals("0"));
            showNotification(dataSnapshot.getKey(),request);
    }

    private void showNotification(String key, Request request) {
        Intent intent=new Intent(getBaseContext(), HireStatus.class);
        PendingIntent contentIntent=PendingIntent.getActivity(getBaseContext(),0,intent,0);

        NotificationCompat.Builder builder=new NotificationCompat.Builder(getBaseContext());

        builder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setTicker("Wheelers Bay")
                .setContentInfo("New Hire")
                .setContentText("You have new Hire #"+key)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(contentIntent);
        NotificationManager manager=(NotificationManager)getBaseContext().getSystemService(Context.NOTIFICATION_SERVICE);

        int randomInt=new Random().nextInt(999-1)+1;
        manager.notify(randomInt,builder.build());
    }

    @Override
    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

    }

    @Override
    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

    }

    @Override
    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {

    }
}
