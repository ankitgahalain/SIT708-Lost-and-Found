package com.example.sit708lostandfound;

import android.database.Cursor;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap map;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_map);

        databaseHelper = new DatabaseHelper(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

        Cursor cursor = databaseHelper.getAllItems();
        if (cursor != null && cursor.moveToFirst()) {
            boolean movedCamera = false;

            do {
                String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                String location = cursor.getString(cursor.getColumnIndexOrThrow("location"));
                double lat = cursor.getDouble(cursor.getColumnIndexOrThrow("latitude"));
                double lng = cursor.getDouble(cursor.getColumnIndexOrThrow("longitude"));

                if (lat != 0 && lng != 0) {
                    LatLng position = new LatLng(lat, lng);
                    map.addMarker(new MarkerOptions().position(position).title(name + " @ " + location));

                    // Move camera once only
                    if (!movedCamera) {
                        map.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 12));
                        movedCamera = true;
                    }
                }
            } while (cursor.moveToNext());

            cursor.close();
        }
    }
}