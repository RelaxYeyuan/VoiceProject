package com.semisky.voicereceiveclient.manager;

import android.content.Context;
import android.location.Criteria;
import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.util.Log;

import java.util.Iterator;

public class GPSManager {

    private static final String TAG = "GPSManager";

    private static volatile GPSManager manager;
    private Context mContext;
    //经度
    private double longitude;
    //维度
    private double latitude;

    public static GPSManager getInstance(Context context) {
        if (manager == null) {
            synchronized (GPSManager.class) {
                if (manager == null) {
                    manager = new GPSManager(context);
                }
            }
        }
        return manager;
    }

    private GPSManager(Context context) {
        mContext = context;
        getLngAndLat();
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    private void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    private void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    /**
     * 获取经纬度
     */
    public void getLngAndLat() {
        LocationManager locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        if (locationManager != null && locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {  //从gps获取经纬度
            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (location != null) {
                latitude = location.getLatitude();
                longitude = location.getLongitude();
                setLatitude(latitude);
                setLongitude(longitude);
                Log.d(TAG, "setLatitude: " + latitude);
                Log.d(TAG, "setLongitude: " + longitude);
            }

        }
    }
}
