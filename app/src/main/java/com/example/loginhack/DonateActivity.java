package com.example.loginhack;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

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

public class DonateActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMapLongClickListener {

    private GoogleMap mMap;
    private String email;

    LocationManager locationManager;
    LocationListener locationListener;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    public void onMapLongClick(LatLng latLng) {
        if(ContextCompat.checkSelfPermission(DonateActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            updateMapLocation(latLng);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donate2);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Intent intent = getIntent();
        email = intent.getStringExtra("Email");

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setOnMapLongClickListener(this);

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION},1);

        db.collection("Help").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()) {
                    for(QueryDocumentSnapshot documentSnapshot : Objects.requireNonNull(task.getResult())) {
                        Double lat = (Double) documentSnapshot.getData().get("latitude");
                        Double lon = (Double) documentSnapshot.getData().get("longitude");
                        LatLng user = new LatLng(lat,lon);
                        mMap.addMarker(new MarkerOptions().position(user).title("Needer"));
                    }
                }
            }
        });
    }

    public void updateMapLocation(LatLng latLng) {
        mMap.addMarker(new MarkerOptions().position(latLng).title("Users Location"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,10));

        LatLng userLocation = new LatLng(latLng.latitude, latLng.longitude);
        mMap.addMarker(new MarkerOptions().position(userLocation).title("Your Location").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation,10));

        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
        try {
            List<Address> listAddress = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);

            if (listAddress != null && listAddress.size() > 0) {
                String address = "";

                if (listAddress.get(0).getAdminArea() != null)
                    address += listAddress.get(0).getAdminArea() + " ";

                if (listAddress.get(0).getLocality() != null)
                    address += listAddress.get(0).getLocality() + " ";

                if (listAddress.get(0).getThoroughfare() != null)
                    address += listAddress.get(0).getThoroughfare() + " ";

                if (listAddress.get(0).getPostalCode() != null)
                    address += listAddress.get(0).getPostalCode() + " ";

                if (listAddress.get(0).getSubLocality() != null)
                    address += listAddress.get(0).getSubLocality() + " ";

                if (listAddress.get(0).getSubThoroughfare() != null)
                    address += listAddress.get(0).getSubThoroughfare() + " ";

                if (listAddress.get(0).getSubAdminArea() != null)
                    address += listAddress.get(0).getSubAdminArea() + " ";

                Log.i("Address", address);

                Map<String, Object> donate = new HashMap<>();
                donate.put("latitude",latLng.latitude);
                donate.put("longitude",latLng.longitude);
                donate.put("email",email);


                db.collection("Donate").add(donate);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}