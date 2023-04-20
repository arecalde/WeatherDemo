package com.example.weather.helpers;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.provider.Settings;

import androidx.core.util.Consumer;

public class LocationHelper {
    public static final int PERMISSION_REQUEST = 10;
    public static final String[] permissions = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };

    @SuppressLint("MissingPermission")
    public static void getLocation(Context context, Consumer<Location> updateLocation) {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        boolean hasGps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean hasNetwork = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if (hasGps) {
            Location localGpsLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            updateLocation.accept(localGpsLocation);
        } else if (hasNetwork) {
            Location locationNetworkLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            updateLocation.accept(locationNetworkLocation);
        } else {
            context.startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
        }

    }
    public static boolean checkPermission(Context context) {
        boolean allSuccess = true;
        for (String permission : permissions) {
            if (context.checkCallingOrSelfPermission(permission) == PackageManager.PERMISSION_DENIED)
                allSuccess = false;
        }
        return allSuccess;
    }
}
