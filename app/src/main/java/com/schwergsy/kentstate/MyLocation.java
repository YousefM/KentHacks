package com.schwergsy.kentstate;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;

public class MyLocation {

    Location location;

    public MyLocation(double latitude, double longitude) throws YouFuckedUpException {


        location = new Location("");

        if (latitude > 90 || latitude < -90) {
            throw new YouFuckedUpException();
        } else {
            location.setLatitude(latitude);
        }

        if (longitude > 180 || longitude < -180) {
            throw new YouFuckedUpException();
        } else {
            location.setLongitude(longitude);
        }
    }

    public MyLocation(Location location) throws YouFuckedUpException {
        this(location.getLatitude(), location.getLongitude());
    }

    public Location getLocation() {
        return location;
    }

    //returns the distance in kilometers from this location to the input location
    public double getDistance(MyLocation myLocation) {

        Location loc = new Location("");

        loc.setLatitude(myLocation.getLocation().getLatitude());

        loc.setLongitude(myLocation.getLocation().getLongitude());

        return location.distanceTo(loc)/1000f;
    }

//    public MyLocation getCurrentLocation() {
//
//        //Acquire a reference to the system Location Manager
//        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
//
//        // Define a listener that responds to location updates
//        LocationListener locationListener = new LocationListener() {
//            public void onLocationChanged(Location location) {
//                // Called when a new location is found by the network location provider.
//                makeUseOfNewLocation(location);
//            }
//
//            public void onStatusChanged(String provider, int status, Bundle extras) {}
//
//            public void onProviderEnabled(String provider) {}
//
//            public void onProviderDisabled(String provider) {}
//        };
//
//        // Register the listener with the Location Manager to receive location updates
//        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
//    }
//
//
//    public void makeUseOfNewLocation(Location location) {
//
//    }



}
