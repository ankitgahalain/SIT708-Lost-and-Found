package com.example.sit708lostandfound;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.Arrays;

public class ViewAllItemsActivity extends AppCompatActivity {

    DatabaseHelper db;
    ListView listView;
    ArrayList<ItemModel> itemList;
    ItemAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_view_all_items);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        db = new DatabaseHelper(this);
        listView = findViewById(R.id.listView);
        itemList = new ArrayList<>();

        loadItems();

    }
    @Override
    protected void onResume() {
        super.onResume();
        loadItems(); // Refresh the list
    }

    private void loadItems() {

        Cursor cursor = db.getAllItems();
        itemList.clear();

        while (cursor.moveToNext()) {
            Log.d("ItemList", "Loaded item with id: " + cursor.getInt(cursor.getColumnIndexOrThrow("id")));
            ItemModel item = new ItemModel();
            item.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
            item.setName(cursor.getString(cursor.getColumnIndexOrThrow("name")));
            item.setDescription(cursor.getString(cursor.getColumnIndexOrThrow("description")));
            item.setLocation(cursor.getString(cursor.getColumnIndexOrThrow("location")));
            item.setDate(cursor.getString(cursor.getColumnIndexOrThrow("date")));
            item.setContact(cursor.getString(cursor.getColumnIndexOrThrow("contact")));
            item.setStatus(cursor.getString(cursor.getColumnIndexOrThrow("status")));
            itemList.add(item);
        }
        Log.d("ItemList", "Column names:  " + Arrays.toString(cursor.getColumnNames()));

        adapter = new ItemAdapter(this, itemList);
        listView.setAdapter(adapter);
    }
}