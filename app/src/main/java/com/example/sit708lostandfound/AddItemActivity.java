package com.example.sit708lostandfound;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class AddItemActivity extends AppCompatActivity {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 100;
    private static final int AUTOCOMPLETE_REQUEST_CODE = 200;
    EditText name, desc, location, date, contact;
    RadioGroup statusGrp;
    Button saveBtn, getLocation;

    private double selectedLat = 0.0, selectedLng = 0.0;

    private FusedLocationProviderClient fusedLocationProviderClient;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        name = findViewById(R.id.name);
        desc = findViewById(R.id.desc);
        location = findViewById(R.id.location);
        getLocation = findViewById(R.id.getCurrentLocation);
        date = findViewById(R.id.date);
        contact = findViewById(R.id.contact);
        saveBtn = findViewById(R.id.saveBtn);
        statusGrp = findViewById(R.id.statusGrp);

        db = new DatabaseHelper(this);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        Places.initialize(getApplicationContext(), "AIzaSyBygUcXXKUkcZkp_b8iETsegQp0O9oBpng");

        location.setFocusable(false);
        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG);
                Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fields)
                        .build(AddItemActivity.this);
                startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);
            }
        });
        getLocation.setOnClickListener(v -> getCurrentLocation());
        date.setOnClickListener(v -> showDatePicker());

        saveBtn.setOnClickListener(view -> saveItem());
    }

    private void showDatePicker() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    // Create a new Calendar object with selected date
                    Calendar selectedDate = Calendar.getInstance();
                    selectedDate.set(selectedYear, selectedMonth, selectedDay);

                    // Format as DD MMM YYYY (e.g., 08 Apr 2025)
                    String formattedDate = new java.text.SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
                            .format(selectedDate.getTime());

                    // Set the formatted date
                    date.setText(formattedDate);
                },
                year, month, day
        );
        datePickerDialog.show();
    }

    private void getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
            return;
        }

        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location getlocation) {
                if (getlocation != null) {
                    selectedLat = getlocation.getLatitude();
                    selectedLng = getlocation.getLongitude();
                    location.setText("(" + selectedLat + ", " + selectedLng + ")");
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == AUTOCOMPLETE_REQUEST_CODE && resultCode == RESULT_OK) {
            Place place = Autocomplete.getPlaceFromIntent(data);
            location.setText(place.getName());
            if (place.getLatLng() != null) {
                selectedLat = place.getLatLng().latitude;
                selectedLng = place.getLatLng().longitude;
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            getCurrentLocation();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void saveItem() {
        ItemModel item = new ItemModel();
        item.setName(name.getText().toString());
        item.setDescription(desc.getText().toString());
        item.setLocation(location.getText().toString());
        item.setDate(date.getText().toString());
        item.setContact(contact.getText().toString());
        item.setLatitude(selectedLat);
        item.setLongitude(selectedLng);

        if (statusGrp.getCheckedRadioButtonId() == -1) {
            Toast.makeText(this, "Please select Lost or Found status", Toast.LENGTH_SHORT).show();
            return;
        }
        item.setStatus(((RadioButton) findViewById(statusGrp.getCheckedRadioButtonId())).getText().toString());

        Log.d("SAVE_ITEM", "Saving item: Lat = " + item.getLatitude() + ", Lng = " + item.getLatitude());

        if (db.insertItem(item)) {
            Toast.makeText(this, "Item saved!", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Error saving item.", Toast.LENGTH_SHORT).show();
        }
    }

}
