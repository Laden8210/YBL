package com.example.ybl.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ybl.R;
import com.example.ybl.interfaces.ApiCallback;
import com.example.ybl.model.LoginRequest;
import com.example.ybl.model.LoginResponse;
import com.example.ybl.model.User;
import com.example.ybl.repository.AuthRepository;
import com.example.ybl.util.SessionManager;

public class LoginActivity extends AppCompatActivity {

    private EditText editTextUsername, editTextPassword;
    private Button buttonLogin, buttonRegister;
    private ProgressBar progressBar;
    private AuthRepository authRepository;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sessionManager = SessionManager.getInstance(this);
        if (sessionManager.isLoggedIn()) {
            redirectToDashboard();
            return;
        }

        initializeViews();
        setupClickListeners();

        authRepository = new AuthRepository(this);
    }

    private void initializeViews() {
        editTextUsername = findViewById(R.id.editTextUsername);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonLogin = findViewById(R.id.buttonLogin);
        buttonRegister = findViewById(R.id.buttonRegister);
        progressBar = findViewById(R.id.progressBar);

        progressBar.setVisibility(View.GONE);
    }

    private void setupClickListeners() {
        buttonLogin.setOnClickListener(v -> attemptLogin());

        buttonRegister.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }

    private void attemptLogin() {
        String email = editTextUsername.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter both email and password", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Please enter a valid email address", Toast.LENGTH_SHORT).show();
            return;
        }

        // Show loading state
        setLoadingState(true);

        // Create login request
        String deviceName = android.os.Build.MODEL; // Use device model as device name
        LoginRequest loginRequest = new LoginRequest(email, password, deviceName);

        // Make API call
        authRepository.login(loginRequest, new ApiCallback<LoginResponse>() {
            @Override
            public void onSuccess(LoginResponse loginResponse) {
                runOnUiThread(() -> {
                    setLoadingState(false);
                    handleLoginSuccess(loginResponse.getUser());
                });
            }

            @Override
            public void onError(String errorMessage) {
                runOnUiThread(() -> {
                    setLoadingState(false);
                    handleLoginError(errorMessage);
                });
            }

            @Override
            public void onFailure(Throwable t) {
                runOnUiThread(() -> {
                    setLoadingState(false);
                    handleLoginFailure(t);
                });
            }
        });
    }

    private void handleLoginSuccess(User user) {
        // Show success message
        Toast.makeText(this, "Login successful! Welcome " + user.getName(), Toast.LENGTH_SHORT).show();
        redirectToDashboard();
    }

    private void handleLoginError(String errorMessage) {
        // Show the error message
        Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();

        // Set error on appropriate field
        if (errorMessage.toLowerCase().contains("email") || errorMessage.toLowerCase().contains("credentials")) {
            editTextUsername.setError(errorMessage);
            editTextUsername.requestFocus();
        } else if (errorMessage.toLowerCase().contains("password")) {
            editTextPassword.setError(errorMessage);
            editTextPassword.requestFocus();
        }
    }

    private void handleLoginFailure(Throwable t) {
        String errorMessage = "Network error. Please check your internet connection.";
        if (t != null && t.getMessage() != null) {
            if (t.getMessage().contains("Unable to resolve host")) {
                errorMessage = "No internet connection. Please check your network settings.";
            } else if (t.getMessage().contains("timeout")) {
                errorMessage = "Connection timeout. Please try again.";
            }
        }
        Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
    }

    private void redirectToDashboard() {
        User user = sessionManager.getUserDetails();
        if(user == null) {

            return;
        }
        String userRole = user.getRole();
        Intent intent;

        switch (userRole) {
            case "supervisor":
                intent = new Intent(LoginActivity.this, SupervisorHero.class);
                break;
            case "driver":
                intent = new Intent(LoginActivity.this, DriverHero.class);
                break;
            case "conductor":
                intent = new Intent(LoginActivity.this, ConductorHero.class);
                break;
            case "passenger":
                intent = new Intent(LoginActivity.this, PassengerHero.class);
                break;
            default:
                Toast.makeText(this, "Unknown user role", Toast.LENGTH_SHORT).show();
                return;
        }

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void setLoadingState(boolean isLoading) {
        if (isLoading) {
            progressBar.setVisibility(View.VISIBLE);
            buttonLogin.setEnabled(false);
            buttonRegister.setEnabled(false);
            editTextUsername.setEnabled(false);
            editTextPassword.setEnabled(false);
        } else {
            progressBar.setVisibility(View.GONE);
            buttonLogin.setEnabled(true);
            buttonRegister.setEnabled(true);
            editTextUsername.setEnabled(true);
            editTextPassword.setEnabled(true);
        }
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(1);
    }
}
