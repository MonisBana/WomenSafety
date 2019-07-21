package com.mab.womensafety;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener {
    private GoogleApiClient gac;
    private Location loc;
    private Button mCancelBtn, mSubmitBtn;
    private double lat,lon;
    private EditText mFatherName,mFatherMobile,mMotherName, mMotherMobile;
    private SQLiteHelper db;
    private boolean flag;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        GoogleApiClient.Builder builder = new GoogleApiClient.Builder(this);
        builder.addApi(LocationServices.API);
        builder.addConnectionCallbacks(this);
        builder.addOnConnectionFailedListener(this);
        gac = builder.build();
        db = new SQLiteHelper(this);
        mCancelBtn = findViewById(R.id.cancelBtn);
        mSubmitBtn = findViewById(R.id.submitBtn);
        mFatherName = findViewById(R.id.fatherName);
        mFatherMobile = findViewById(R.id.fatherMobile);
        mMotherName = findViewById(R.id.motherName);
        mMotherMobile = findViewById(R.id.motherMobile);
        try{
            mFatherName.setText(db.getName("FATHER"));
            mMotherName.setText(db.getName("MOTHER"));
            mFatherMobile.setText(db.getMobile("FATHER"));
            mMotherMobile.setText(db.getMobile("MOTHER"));
        }
        catch (Exception e){
            Log.e("Edit Text",e.toString());
        }
        if(mFatherName.getText().equals("")){
            flag= Boolean.TRUE;
        }
        final MyThread t = new MyThread();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                t.run();
            }
        },3000);
        mCancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                t.terminate();
            }
        });
        mSubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(flag)
                {
                    db.addDetails(mFatherName.getText().toString(),"FATHER",mFatherMobile.getText().toString());
                    db.addDetails(mMotherName.getText().toString(),"MOTHER",mMotherMobile.getText().toString());
                }
                else
                    db.updateDetails(mFatherName.getText().toString(),"FATHER",mFatherMobile.getText().toString());
                    db.updateDetails(mMotherName.getText().toString(),"MOTHER",mMotherMobile.getText().toString());
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(gac !=null)
            gac.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(gac!= null)
        {
            gac.disconnect();
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if ((ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) && (ActivityCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED)) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
        }
        loc = LocationServices.FusedLocationApi.getLastLocation(gac);
        if(loc!=null) {
             lat = loc.getLatitude();
             lon = loc.getLongitude();
            //CurrentLocation = new LatLng(lat,lon);
            Toast.makeText(this, lat+"  "+lon+"", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(this, "Please Enable Gps/Come In Open Area", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Toast.makeText(this, "Connectin Suspended", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this, "Connection Failed", Toast.LENGTH_SHORT).show();
    }
    class MyThread implements Runnable{
        private volatile boolean running = true;
        public void terminate() {
            running = false;
        }
        @Override
        public void run() {
            while(running){
                String fatherMobile = db.getMobile("FATHER");
                String motherMobile = db.getMobile("MOTHER");
                String message = "http://www.google.com/maps/place/"+lat+","+lon ;
                SmsManager sm = SmsManager.getDefault();
               /* sm.sendTextMessage(fatherMobile, null, message, null, null);
                sm.sendTextMessage(motherMobile, null, message, null, null);*/
                terminate();
            }
        }
    }
}


