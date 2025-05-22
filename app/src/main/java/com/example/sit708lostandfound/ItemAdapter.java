package com.example.sit708lostandfound;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ItemAdapter extends BaseAdapter {

    Context context;
    ArrayList<ItemModel> itemList;
    LayoutInflater inflater;
    DatabaseHelper db;


    public ItemAdapter(Context context, ArrayList<ItemModel> itemList) {
        this.context = context;
        this.itemList = itemList;
        this.inflater = LayoutInflater.from(context);
        db = new DatabaseHelper(context);
    }

    @Override
    public int getCount() {
        return itemList.size();
    }

    @Override
    public Object getItem(int i) {
        return itemList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return itemList.get(i).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = inflater.inflate(R.layout.item_row, null);

        TextView name = view.findViewById(R.id.itemName);
        TextView desc = view.findViewById(R.id.itemDesc);
        TextView location = view.findViewById(R.id.itemLocation);
        TextView date = view.findViewById(R.id.itemDate);
        TextView contact = view.findViewById(R.id.itemContact);
        TextView status = view.findViewById(R.id.itemStatus);

        ItemModel item = itemList.get(position);

        name.setText(item.getName());
        desc.setText("Desc: " + item.getDescription());
        location.setText("Location: " + item.getLocation());
        date.setText("Date: " + item.getDate());
        contact.setText("Contact: " + item.getContact());
        status.setText("Status: " + item.getStatus());


        Log.d("Status", "statusIs: " + item.getStatus());

        view.setOnClickListener(v -> {
            Intent intent = new Intent(context, ItemDetailActivity.class);
            intent.putExtra("itemId", itemList.get(position).getId());
            Log.d("ItemAdapter", "Starting ItemDetailActivity with itemId: " + itemList.get(position).getId());
            context.startActivity(intent);
        });

        return view;
    }
}
