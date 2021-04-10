package com.example.samajhit;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class NeedHelp extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private FirebaseFirestore db;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            {
                {
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
                        Location lastknownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        updateMapLocation(lastknownLocation);
                    }
                }
            }
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_need_help);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        db = FirebaseFirestore.getInstance();


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION},1);
        else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            Location lastknownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if(lastknownLocation!=null) {
                updateMapLocation(lastknownLocation);
            }
        }

        db.collection("Donate").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()) {
                    for(QueryDocumentSnapshot documentSnapshot : Objects.requireNonNull(task.getResult())) {
                        Double lat = (Double) documentSnapshot.getData().get("latitude");
                        Double lon = (Double) documentSnapshot.getData().get("longitude");
                        LatLng userLocation = new LatLng(lat,lon);
                        mMap.addMarker(new MarkerOptions().position(userLocation).title("U").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(userLocation));
                    }
                }
            }
        });
    }

    public void updateMapLocation(Location location)
    {
        mMap.clear();
        LatLng userLocation = new LatLng(location.getLatitude(),location.getLongitude());
        mMap.addMarker(new MarkerOptions().position(userLocation).title("Users Location"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(userLocation));

        Geocoder geocoder =  new Geocoder(getApplicationContext(), Locale.getDefault());
        try {
            List<Address> listAddress = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(),1);

            if(listAddress!=null && listAddress.size()>0) {
                String address = "";

                if (listAddress.get(0).getAdminArea()!=null)
                    address+=listAddress.get(0).getAdminArea() + " ";

                if (listAddress.get(0).getLocality()!=null)
                    address+=listAddress.get(0).getLocality() + " ";

                if (listAddress.get(0).getThoroughfare()!=null)
                    address+=listAddress.get(0).getThoroughfare() + " ";

                if (listAddress.get(0).getPostalCode()!=null)
                    address+=listAddress.get(0).getPostalCode() + " ";

                if (listAddress.get(0).getSubLocality()!=null)
                    address+=listAddress.get(0).getSubLocality() + " ";

                if (listAddress.get(0).getSubThoroughfare()!=null)
                    address+=listAddress.get(0).getSubThoroughfare() + " ";

                if (listAddress.get(0).getSubAdminArea()!=null)
                    address+=listAddress.get(0).getSubAdminArea() + " ";

                Log.i("Address",address);

                Map<String, Object> help = new HashMap<>();
                help.put("latitude",location.getLatitude());
                help.put("longitude",location.getLongitude());

                db.collection("Help").document(address).set(help);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}