package com.rubenarriazu.paranoid.ui.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.rubenarriazu.paranoid.R;
import com.rubenarriazu.paranoid.api.APIClient;
import com.rubenarriazu.paranoid.api.Endpoints;
import com.rubenarriazu.paranoid.api.requests.LoginRequest;
import com.rubenarriazu.paranoid.api.responses.LoginResponse;
import com.rubenarriazu.paranoid.credentials.Credentials;
import com.rubenarriazu.paranoid.ui.BaseNavigation;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Login extends AppCompatActivity {

    private TextInputLayout loginUsernameLayout = findViewById(R.id.login_username_layout);
    private TextInputEditText loginUsernameEdit;
    private TextInputLayout loginPasswdLayout;
    private TextInputEditText loginPasswdEdit;
    private MaterialButton loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        assignLabels();

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = obtainUsername();
                String password = obtainPassword();
                apiLogin(username, password);
            }
        });
    }

    private void assignLabels() {
        loginUsernameEdit = findViewById(R.id.login_username_edit);
        loginPasswdLayout = findViewById(R.id.login_passwd_layout);
        loginPasswdEdit = findViewById(R.id.login_passwd_edit);
        loginButton = findViewById(R.id.login_button);
    }

    private String obtainUsername() {
        return loginUsernameEdit.getText().toString();
    }

    private String obtainPassword() {
        return loginPasswdEdit.getText().toString();
    }

    private void apiLogin(String username, String password) {
        LoginRequest loginRequest = new LoginRequest(username, password);
        Endpoints endpoints = APIClient.retrofit.create(Endpoints.class);
        Call<LoginResponse> call = endpoints.login(loginRequest);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful()) {
                    LoginResponse loginResponse = response.body();
                    String token = loginResponse.getToken();
                    storeCredentials(username, password, token);
                    goToActivity(BaseNavigation.class);
                } else {
                    showErrorMessage();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Log.e("retrofit", t.getMessage());
            }
        });
    }

    private void storeCredentials(String username, String password, String token) {
        Credentials credentials = new Credentials(getApplicationContext());
        credentials.storeUsername(username);
        credentials.storePassword(password);
        credentials.storeToken(token);
    }

    private void showErrorMessage() {
        loginUsernameLayout.setError("Incorrect username or password");
        loginPasswdLayout.setError("Incorrect username or password");
        loginUsernameEdit.setText("");
        loginPasswdEdit.setText("");
    }

    private void goToActivity(Class<?>  activity) {
        Intent intent = new Intent(getApplicationContext(), activity);
        startActivity(intent);
    }

}
