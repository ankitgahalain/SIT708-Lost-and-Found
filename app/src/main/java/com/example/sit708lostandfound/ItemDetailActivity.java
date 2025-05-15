package com.example.sit708lostandfound;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ItemDetailActivity extends AppCompatActivity {

    TextView name, desc, location, date, contact;
    Button deleteBtn;
    DatabaseHelper db;
    int itemId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_item_detail);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        name = findViewById(R.id.detailName);
        desc = findViewById(R.id.detailDesc);
        location = findViewById(R.id.detailLocation);
        date = findViewById(R.id.detailDate);
        contact = findViewById(R.id.detailContact);
        deleteBtn = findViewById(R.id.btnDeleteItem);
        db = new DatabaseHelper(this);
        Intent intent = getIntent();
        itemId = intent.getIntExtra("itemId", -1);

        if (itemId == -1) {
            Toast.makeText(this, "Invalid item", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        Log.d("ItemDetail", "Loading item with ID: " + itemId);

        Cursor cursor = db.getItemById(itemId);
        if (cursor.moveToFirst()) {
            name.setText(cursor.getString(cursor.getColumnIndexOrThrow("status"))+' '+cursor.getString(cursor.getColumnIndexOrThrow("name")));
            desc.setText("Description: " + cursor.getString(cursor.getColumnIndexOrThrow("description")));
            location.setText("at " + cursor.getString(cursor.getColumnIndexOrThrow("location")));
            date.setText("On " + cursor.getString(cursor.getColumnIndexOrThrow("date")));
            contact.setText("Please Contact: " + cursor.getString(cursor.getColumnIndexOrThrow("contact")));
        } else {
            Toast.makeText(this, "Item not found"+itemId, Toast.LENGTH_SHORT).show();
            finish();
        }

        deleteBtn.setOnClickListener(v -> {
            db.deleteItem(itemId);
            Toast.makeText(this, "Item deleted", Toast.LENGTH_SHORT).show();
            finish(); // Go back to list
        });
    }
}