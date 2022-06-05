package com.rubenarriazu.paranoid.ui.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.rubenarriazu.paranoid.R;
import com.rubenarriazu.paranoid.api.APIClient;
import com.rubenarriazu.paranoid.api.Endpoints;
import com.rubenarriazu.paranoid.api.requests.LoginRequest;
import com.rubenarriazu.paranoid.api.responses.LoginResponse;
import com.rubenarriazu.paranoid.api.responses.UserResponse;
import com.rubenarriazu.paranoid.credentials.Credentials;
import com.rubenarriazu.paranoid.errors.ErrorCodes;
import com.rubenarriazu.paranoid.ui.BaseNavigation;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;

public class Login extends AppCompatActivity {

    private TextInputLayout loginUsernameLayout;
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
                new LoginAttempt().execute(username, password);
            }
        });
    }

    private void assignLabels() {
        loginUsernameLayout = findViewById(R.id.login_username_layout);
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

    private void storeCredentials(String username, String password, String token, int userPK) {
        Credentials credentials = new Credentials(getApplicationContext());
        credentials.storeUsername(username);
        credentials.storePassword(password);
        credentials.storeToken(token);
        credentials.storeUserPK(userPK);
    }

    private void showAuthErrorMessage() {
        loginUsernameLayout.setError("Incorrect username or password");
        loginPasswdLayout.setError("Incorrect username or password");
        loginUsernameEdit.setText("");
        loginPasswdEdit.setText("");
    }

    private void goToActivity(Class<?>  activity) {
        Intent intent = new Intent(getApplicationContext(), activity);
        startActivity(intent);
    }

    // Given an username and password, returns a token
    public String getToken(String username, String password) {
        LoginRequest loginRequest = new LoginRequest(username, password);
        Endpoints endpoints = APIClient.retrofit.create(Endpoints.class);
        Call<LoginResponse> call = endpoints.login(loginRequest);
        try {
            Response<LoginResponse> response = call.execute();
            if (response.isSuccessful()) {
                var loginResponse = response.body();
                return loginResponse.getToken();
            }
            return ErrorCodes.NOT_VALID_AUTH;
        } catch (IOException e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

    // Given an authentication token, returns the user PK
    private int getUserPK(String token) {
        Endpoints endpoints = APIClient.retrofit.create(Endpoints.class);
        Call<UserResponse> call = endpoints.getUser("Token " + token);
        try {
            Response<UserResponse> response = call.execute();
            if (response.isSuccessful()) {
                return response.body().getId();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ErrorCodes.NULL_USER_PK;
    }

    // Attempt to login given an username and password
    private class LoginAttempt extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... strings) {
            String username = strings[0];
            String password = strings[1];
            var token = getToken(username, password);
            if (token.equals(ErrorCodes.NOT_VALID_AUTH)) {
                showAuthErrorMessage();
                return null;
            }
            var userPK = getUserPK(token);
            storeCredentials(username, password, token, userPK);
            goToActivity(BaseNavigation.class);
            return null;
        }
    }

}
