package com.example.ujian;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AddEditDriverActivity extends AppCompatActivity {
    EditText etName, etPhone;
    Spinner spVehicle, spStatus;
    Button btnSave;
    DatabaseHelper db;
    int driverId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_driver);

        db = new DatabaseHelper(this);
        etName = findViewById(R.id.etDriverName);
        etPhone = findViewById(R.id.etDriverPhone);
        spVehicle = findViewById(R.id.spVehicle);
        spStatus = findViewById(R.id.spStatus);
        btnSave = findViewById(R.id.btnSaveDriver);

        String[] vehicles = {"Motor", "Mobil", "Motor & Mobil", "Angkot"};
        ArrayAdapter<String> va = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, vehicles);
        spVehicle.setAdapter(va);

        String[] statuses = {"offline", "online"};
        spStatus.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, statuses));

        Intent i = getIntent();
        if (i != null && i.hasExtra("driver_id")) {
            driverId = i.getIntExtra("driver_id", -1);
            Driver d = db.getDriverById(driverId);
            if (d != null) {
                etName.setText(d.getName());
                etPhone.setText(d.getPhone());
                int posV = va.getPosition(d.getVehicle());
                if (posV >= 0) spVehicle.setSelection(posV);
                spStatus.setSelection(d.getStatus().equals("online") ? 1 : 0);
            }
        }

        btnSave.setOnClickListener(v -> {
            String name = etName.getText().toString().trim();
            String phone = etPhone.getText().toString().trim();
            String vehicle = spVehicle.getSelectedItem().toString();
            String status = spStatus.getSelectedItem().toString();

            if (name.isEmpty() || phone.isEmpty()) {
                Toast.makeText(this, "lengkapi data driver", Toast.LENGTH_SHORT).show();
                return;
            }

            if (driverId == -1) {
                Driver d = new Driver(name, vehicle, phone, status);
                long id = db.addDriver(d);
                if (id > 0) Toast.makeText(this, "driver ditambahkan", Toast.LENGTH_SHORT).show();
                else Toast.makeText(this, "gagal menambah", Toast.LENGTH_SHORT).show();
            } else {
                Driver d = new Driver(name, vehicle, phone, status);
                d.setId(driverId);
                boolean ok = db.updateDriver(d);
                if (ok) Toast.makeText(this, "driver diperbarui", Toast.LENGTH_SHORT).show();
                else Toast.makeText(this, "gagal memperbarui", Toast.LENGTH_SHORT).show();
            }
            setResult(RESULT_OK);
            finish();
        });
    }
}
