package com.example.sit708lostandfound;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    Button newItem, viewAllItems, showMap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        newItem = findViewById(R.id.newItem);
        viewAllItems = findViewById(R.id.viewAllItems);
        showMap = findViewById(R.id.showMap);

        newItem.setOnClickListener(view -> {
            Intent intent = new Intent(this, AddItemActivity.class);
            startActivity(intent);
        });
        viewAllItems.setOnClickListener(view -> {
            Intent intent = new Intent(this, ViewAllItemsActivity.class);
            startActivity(intent);
        });

        showMap.setOnClickListener(view -> {
            Intent intent = new Intent(this, MapActivity.class);
            startActivity(intent);
        });


    }
}