package com.rubenarriazu.acs.ui.registration;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.rubenarriazu.acs.R;
import com.rubenarriazu.acs.api.APIClient;
import com.rubenarriazu.acs.api.requests.RegistrationRequest;
import com.rubenarriazu.acs.api.Endpoints;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistrationView extends AppCompatActivity {

    private TextInputLayout usernameLayout;
    private TextInputLayout password1Layout;
    private TextInputLayout password2Layout;
    private TextInputLayout firstNameLayout;
    private TextInputLayout lastNameLayout;

    private TextInputEditText usernameEdit;
    private TextInputEditText password1Edit;
    private TextInputEditText password2Edit;
    private TextInputEditText firstNameEdit;
    private TextInputEditText lastNameEdit;

    // Register bottom
    private MaterialButton button;

    // Progress bar
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        assignViews();
        buttonClick();
    }

    private void assignViews() {

        // TextInputLayout
        usernameLayout = findViewById(R.id.username_layout);
        password1Layout = findViewById(R.id.password1_layout);
        password2Layout = findViewById(R.id.password2_layout);
        firstNameLayout = findViewById(R.id.first_name_layout);
        lastNameLayout = findViewById(R.id.last_name_layout);

        // TextInputEditText
        usernameEdit = findViewById(R.id.username_edit);
        password1Edit = findViewById(R.id.password1_edit);
        password2Edit = findViewById(R.id.password2_edit);
        firstNameEdit = findViewById(R.id.first_name_edit);
        lastNameEdit = findViewById(R.id.last_name_edit);

        // Register bottom
        button = findViewById(R.id.button_registration);

        // Progress bar
        progressBar = findViewById(R.id.progressBar);

    }

    private void buttonClick() {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validatePasswords()) {
                    RegistrationRequest registrationRequest = getFormData();
                    progressBar.setVisibility(View.VISIBLE);
                    sendRequest(registrationRequest);
                }
            }
        });
    }

    private void sendRequest(RegistrationRequest registrationRequest) {
        Endpoints endpoints = APIClient.retrofit.create(Endpoints.class);
        Call<ResponseBody> call = endpoints.registration(registrationRequest);
        call.enqueue(new Callback<ResponseBody>() {

            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(
                            RegistrationView.this,
                            "User successfully signed in",
                            Toast.LENGTH_SHORT
                    ).show();
                } else {
                    usernameLayout.setError("The user already exist.");
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(RegistrationView.this, t.getMessage(), Toast.LENGTH_LONG).show();
                progressBar.setVisibility(View.GONE);
            }

        });
    }

    private RegistrationRequest getFormData() {
        return new RegistrationRequest(
                usernameEdit.getText().toString(),
                password1Edit.getText().toString(),
                firstNameEdit.getText().toString(),
                lastNameEdit.getText().toString()
        );
    }

    private boolean validatePasswords() {
        String passwd1 = password1Edit.getText().toString();
        String passwd2 = password2Edit.getText().toString();
        if (passwd1.equals(passwd2)) {
            return true;
        }
        password1Layout.setError("");
        password2Layout.setError("Passwords do not match");
        return false;
    }

}
