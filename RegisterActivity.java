package com.example.ujian;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {
    EditText etUser, etPass;
    Button btnRegister;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        db = new DatabaseHelper(this);
        etUser = findViewById(R.id.etRegUsername);
        etPass = findViewById(R.id.etRegPassword);
        btnRegister = findViewById(R.id.btnDoRegister);

        btnRegister.setOnClickListener(v -> {
            String u = etUser.getText().toString().trim();
            String p = etPass.getText().toString().trim();
            if (u.isEmpty() || p.isEmpty()) {
                Toast.makeText(this, "lengkapi data", Toast.LENGTH_SHORT).show();
                return;
            }
            boolean ok = db.registerUser(u, p);
            if (ok) {
                Toast.makeText(this, "registrasi sukses. silahkan login", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "username sudah ada atau error", Toast.LENGTH_SHORT).show();
            }
        });
    }
}