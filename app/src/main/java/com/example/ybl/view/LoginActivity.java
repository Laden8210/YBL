package com.example.ybl.view;


import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ybl.R;

public class LoginActivity extends AppCompatActivity {

    private EditText editTextUsername, editTextPassword;
    private Spinner spinnerUserType;
    private Button buttonLogin, buttonRegister;
    private String selectedUserType = "Passenger";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initializeViews();
        setupSpinner();
        setupClickListeners();
    }

    private void initializeViews() {
        editTextUsername = findViewById(R.id.editTextUsername);
        editTextPassword = findViewById(R.id.editTextPassword);
        spinnerUserType = findViewById(R.id.spinnerUserType);
        buttonLogin = findViewById(R.id.buttonLogin);
        buttonRegister = findViewById(R.id.buttonRegister);
    }

    private void setupSpinner() {
        // Create user type options based on your requirements
        String[] userTypes = {"Passenger", "Driver", "Conductor", "Supervisor", "Admin"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                userTypes
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerUserType.setAdapter(adapter);

        spinnerUserType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedUserType = userTypes[position];
                updateUIForUserType();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedUserType = "Passenger";
            }
        });
    }

    private void updateUIForUserType() {
        // Show/hide register button based on user type
        if (selectedUserType.equals("Passenger")) {
            buttonRegister.setVisibility(View.VISIBLE);
        } else {
            buttonRegister.setVisibility(View.GONE);
        }

        // Update hint texts based on user type
        switch (selectedUserType) {
            case "Admin":
                editTextUsername.setHint("Admin ID");
                break;
            case "Driver":
                editTextUsername.setHint("Driver ID");
                break;
            case "Conductor":
                editTextUsername.setHint("Conductor ID");
                break;
            case "Supervisor":
                editTextUsername.setHint("Supervisor ID");
                break;
            default:
                editTextUsername.setHint("Username/Email");
        }
    }

    private void setupClickListeners() {
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptLogin();
            }
        });

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Only for Passenger registration
                if (selectedUserType.equals("Passenger")) {
                   // startActivity(new Intent(LoginActivity.this, PassengerRegisterActivity.class));
                }
            }
        });
    }

    private void attemptLogin() {
        String username = editTextUsername.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter both username and password", Toast.LENGTH_SHORT).show();
            return;
        }

        // Here you would typically validate credentials with your backend
        if (validateCredentials(username, password, selectedUserType)) {
            navigateToDashboard(selectedUserType);
        } else {
            Toast.makeText(this, "Invalid credentials", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean validateCredentials(String username, String password, String userType) {
        // This is a mock validation - replace with actual authentication logic
        // You would typically make an API call to your backend here

        // Mock validation for demo purposes
        return !username.isEmpty() && password.length() >= 4;
    }

    private void navigateToDashboard(String userType) {
//        Intent intent;
//        switch (userType) {
//            case "Admin":
//                intent = new Intent(this, AdminDashboardActivity.class);
//                break;
//            case "Driver":
//                intent = new Intent(this, DriverDashboardActivity.class);
//                break;
//            case "Conductor":
//                intent = new Intent(this, ConductorDashboardActivity.class);
//                break;
//            case "Supervisor":
//                intent = new Intent(this, SupervisorDashboardActivity.class);
//                break;
//            case "Passenger":
//                intent = new Intent(this, PassengerDashboardActivity.class);
//                break;
//            default:
//                intent = new Intent(this, PassengerDashboardActivity.class);
//        }
//
//        // Pass user type and username to dashboard
//        intent.putExtra("USER_TYPE", userType);
//        intent.putExtra("USERNAME", editTextUsername.getText().toString());
//
//        startActivity(intent);
        finish();
    }
}