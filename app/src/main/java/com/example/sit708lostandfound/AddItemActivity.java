package com.example.sit708lostandfound;

import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Calendar;
import java.util.Locale;

public class AddItemActivity extends AppCompatActivity {

    EditText name, desc, location, date, contact;
    RadioGroup statusGrp;
    Button saveBtn;
    DatabaseHelper db;
    int userId;

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
        date = findViewById(R.id.date);
        contact = findViewById(R.id.contact);
        saveBtn = findViewById(R.id.saveBtn);
        statusGrp = findViewById(R.id.statusGrp);

        db = new DatabaseHelper(this);

        date.setOnClickListener(v -> showDatePicker());

        saveBtn.setOnClickListener(view -> {
            ItemModel item = new ItemModel();
            item.setName(name.getText().toString());
            item.setDescription(desc.getText().toString());
            item.setLocation(location.getText().toString());
            item.setDate(date.getText().toString());
            item.setContact(contact.getText().toString());
            item.setUserId(userId);

            if (statusGrp.getCheckedRadioButtonId() == -1) {
                Toast.makeText(this, "Please select Lost or Found status", Toast.LENGTH_SHORT).show();
                return;
            }
            item.setStatus(((RadioButton) findViewById(statusGrp.getCheckedRadioButtonId())).getText().toString());


            if (db.insertItem(item)) {
                Toast.makeText(this, "Item saved!", Toast.LENGTH_SHORT).show();
                finish(); // Go back to main
            } else {
                Toast.makeText(this, "Error saving item.", Toast.LENGTH_SHORT).show();
            }
        });
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
}
