package com.example.ujian;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements DriverAdapter.DriverListener {
    RecyclerView rv;
    DriverAdapter adapter;
    DatabaseHelper db;
    SharedPreferences prefs;
    Button btnAdd, btnTrips;

    // ✅ ganti startActivityForResult dengan ActivityResultLauncher
    private ActivityResultLauncher<Intent> addEditDriverLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new DatabaseHelper(this);
        prefs = getSharedPreferences("uts_pref", MODE_PRIVATE);
        boolean logged = prefs.getBoolean("is_logged_in", false);
        if (!logged) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        rv = findViewById(R.id.rvDrivers);
        btnAdd = findViewById(R.id.btnAddDriver);
        btnTrips = findViewById(R.id.btnAllTrips);

        rv.setLayoutManager(new LinearLayoutManager(this));
        adapter = new DriverAdapter(new ArrayList<>(), this);
        rv.setAdapter(adapter);

        loadDrivers();

        // ✅ launcher buat Add/Edit Driver
        addEditDriverLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> loadDrivers()
        );

        btnAdd.setOnClickListener(v -> {
            Intent i = new Intent(this, AddEditDriverActivity.class);
            addEditDriverLauncher.launch(i);
        });

        btnTrips.setOnClickListener(v -> startActivity(new Intent(this, TripHistoryActivity.class)));
    }

    private void loadDrivers() {
        adapter.setList(db.getAllDrivers());
    }

    @Override
    public void onEdit(Driver d) {
        Intent i = new Intent(this, AddEditDriverActivity.class);
        i.putExtra("driver_id", d.getId());
        addEditDriverLauncher.launch(i); // ✅ ganti
    }

    @Override
    public void onDelete(Driver d) {
        boolean ok = db.deleteDriver(d.getId());
        if (ok) {
            Toast.makeText(this, "driver dihapus", Toast.LENGTH_SHORT).show();
            loadDrivers();
        } else {
            Toast.makeText(this, "gagal menghapus", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onStartTrip(Driver d) {
        db.addTrip(d.getId(), "Lokasi A", "Lokasi B", String.valueOf(System.currentTimeMillis()));
        d.setStatus("online");
        db.updateDriver(d);
        loadDrivers();
        Toast.makeText(this, "perjalanan dimulai & tercatat di riwayat", Toast.LENGTH_SHORT).show();
    }

    // ✅ onActivityResult sudah ga dipake lagi, jadi bisa dihapus
    // @Override
    // protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    //     super.onActivityResult(requestCode, resultCode, data);
    //     loadDrivers();
    // }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add("logout");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if ("logout".equals(item.getTitle())) {
            prefs.edit().clear().apply();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
