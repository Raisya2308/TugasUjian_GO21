package com.example.ujian;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class TripHistoryActivity extends AppCompatActivity {
    DatabaseHelper db;
    LinearLayout container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_history);
        db = new DatabaseHelper(this);
        container = findViewById(R.id.tripContainer);

        loadAllTrips();
    }

    private void loadAllTrips() {
        container.removeAllViews();
        // fetch all drivers and show trips per driver
        ArrayList<Driver> drivers = db.getAllDrivers();
        for (Driver d : drivers) {
            TextView header = new TextView(this);
            header.setText("Driver: " + d.getName());
            header.setTextSize(16);
            container.addView(header);

            ArrayList<Trip> trips = db.getTripsByDriver(d.getId());
            if (trips.isEmpty()) {
                TextView tv = new TextView(this);
                tv.setText("  - belum ada riwayat");
                container.addView(tv);
            } else {
                for (Trip t : trips) {
                    TextView tv = new TextView(this);
                    tv.setText("  • " + t.getFrom() + " -> " + t.getTo() + " @ " + t.getTime());
                    container.addView(tv);
                }
            }
        }
    }
}
