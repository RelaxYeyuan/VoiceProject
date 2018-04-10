package com.semisky.voicereceiveclient.manager;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;

public class GPSManager {

    private static final String TAG = "GPSManager";

    private static volatile GPSManager manager;
    private Context mContext;
    private double longitude;
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
                Log.d(TAG, "getLngAndLat: " + latitude);
                Log.d(TAG, "getLngAndLat: " + longitude);
            }
        }
    }
}
