package com.example.deryk.ibeaconreader;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.RemoteException;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.support.design.widget.Snackbar;


import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.Identifier;
import org.altbeacon.beacon.MonitorNotifier;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import java.util.Collection;

public class BeaconActivity extends AppCompatActivity implements BeaconConsumer {

    private static final int PERMISSION_REQUEST_COARSE_LOCATION = 0;
    public static final String TAG = "BeaconsEverywhere";
    private BeaconManager beaconManager;
    private View mLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beacon);
//        mLayout = findViewById(R.id.main_layout);

        beaconManager = BeaconManager.getInstanceForApplication(this);

        beaconManager.getBeaconParsers().add(new BeaconParser()
                .setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24,d:25-25"));

        beaconManager.bind(this);

        TextView mSampleText = (TextView) findViewById(R.id.sampleText);
        mSampleText.setText("hi");


    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        beaconManager.unbind(this);
    }

    private void requestLocationPermission() {
        // Permission has not been granted and must be requested.
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.ACCESS_COARSE_LOCATION)) {
            // Provide an additional rationale to the user if the permission was not granted
            // and the user would benefit from additional context for the use of the permission.
            // Display a SnackBar with cda button to request the missing permission.
//            Snackbar.make(mLayout, R.string.camera_access_required,
//                    Snackbar.LENGTH_INDEFINITE).setAction(R.string.ok, new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
                    // Request the permission
                    ActivityCompat.requestPermissions(BeaconActivity.this,
                            new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                            PERMISSION_REQUEST_COARSE_LOCATION);
//                }

        } else {
//            Snackbar.make(mLayout, R.string.camera_unavailable, Snackbar.LENGTH_SHORT).show();
            // Request the permission. The result will be received in onRequestPermissionResult().
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_REQUEST_COARSE_LOCATION);
        }
    }

    @Override
    public void onBeaconServiceConnect() {
        final Region region = new Region("F7826DA6-4FA2-4E98-8024-BC5B71E0893E", null, null, null);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            Log.d(TAG, "permission not granted, requesting");
            requestLocationPermission();
        } else {
            Log.d(TAG, "permission granted");
        }

//        beaconManager.addMonitorNotifier(new MonitorNotifier() {
//            @Override
//            public void didEnterRegion(Region region) {
//
//                Log.i(TAG, "beacon found");
//                try {
//                    Log.d(TAG, "didEnterRegion");
//                    beaconManager.startRangingBeaconsInRegion(region);
//                } catch (RemoteException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void didExitRegion(Region region) {
//                Log.i(TAG, "beacon lost");
//
//                try {
//                    Log.d(TAG, "didExitRegion");
//                    beaconManager.stopRangingBeaconsInRegion(region);
//                } catch (RemoteException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void didDetermineStateForRegion(int state, Region region) {
//                Log.i(TAG, "I have just switched from seeing/not seeing beacons: "+state);
//
//            }
//        });

        beaconManager.addRangeNotifier(new RangeNotifier() {
            @Override
            public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {
                for(Beacon oneBeacon : beacons) {
                    Log.d(TAG, "distance: " + oneBeacon.getDistance() + " id:" + oneBeacon.getId1() + "/" + oneBeacon.getId2() + "/" + oneBeacon.getId3());
                }
            }
        });

        try {
            beaconManager.startRangingBeaconsInRegion(region);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
