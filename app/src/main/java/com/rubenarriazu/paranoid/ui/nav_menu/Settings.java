package com.rubenarriazu.paranoid.ui.nav_menu;

import static com.rubenarriazu.paranoid.credentials.CredentialsUtils.flushStoredCredentials;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.rubenarriazu.paranoid.R;
import com.rubenarriazu.paranoid.api.APIClient;
import com.rubenarriazu.paranoid.api.Endpoints;
import com.rubenarriazu.paranoid.api.requests.BioRequest;
import com.rubenarriazu.paranoid.api.requests.FirstNameRequest;
import com.rubenarriazu.paranoid.api.requests.LastNameRequest;
import com.rubenarriazu.paranoid.api.requests.PasswordRequest;
import com.rubenarriazu.paranoid.api.responses.AccountResponse;
import com.rubenarriazu.paranoid.api.responses.UserResponse;
import com.rubenarriazu.paranoid.credentials.Credentials;
import com.rubenarriazu.paranoid.ui.landing_page.MainActivity;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Settings extends AppCompatActivity {

    private TextInputLayout firstNameLayout;
    private TextInputEditText firstNameEditText;
    private TextInputLayout lastNameLayout;
    private TextInputEditText lastNameEditText;
    private TextInputLayout password1Layout;
    private TextInputEditText password1EditText;
    private TextInputLayout password2Layout;
    private TextInputEditText password2EditText;
    private TextInputLayout bioLayout;
    private TextInputEditText bioEditText;
    private MaterialButton cancelButton;
    private MaterialButton saveButton;
    private MaterialButton logoutButton;
    private MaterialButton deleteAccountButton;

    private String originalFirstName;
    private String originalLastName;
    private String originalBio;

    private final Credentials credentials = new Credentials(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuration);

        // Assign elements
        assignElements();

        // Populate with the current values the fields
        populateInitialValues();

        // Save changes
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveChanges();
                Toast.makeText(getApplicationContext(), "Settings changed", Toast.LENGTH_SHORT).show();
            }
        });

        // Logout
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });

        // Delete account
        deleteAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteAccount();
            }
        });
    }

    private void assignElements() {
        firstNameLayout = findViewById(R.id.first_name_layout);
        firstNameEditText = findViewById(R.id.first_name_edit);
        lastNameLayout = findViewById(R.id.last_name_layout);
        lastNameEditText = findViewById(R.id.last_name_edit);
        password1Layout = findViewById(R.id.password1_layout);
        password1EditText = findViewById(R.id.password1_edit);
        password2Layout = findViewById(R.id.password2_layout);
        password2EditText = findViewById(R.id.password2_edit);
        bioLayout = findViewById(R.id.bio_layout);
        bioEditText = findViewById(R.id.bio_edit);
        cancelButton = findViewById(R.id.cancel_button);
        saveButton = findViewById(R.id.save_button);
        logoutButton = findViewById(R.id.logout_button);
        deleteAccountButton = findViewById(R.id.delete_account_button);
    }

    private void populateInitialValues() {
        populateInitialUser();
        populateInitialProfile();
    }

    private void populateInitialUser() {
        Endpoints endpoints = APIClient.retrofit.create(Endpoints.class);
        Call<UserResponse> call = endpoints.getUser("Token " + credentials.getToken());
        call.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if (response.isSuccessful()) {
                    var userResponse = response.body();
                    firstNameEditText.setText(userResponse.getFirstName());
                    lastNameEditText.setText(userResponse.getLastName());
                    originalFirstName = userResponse.getFirstName();
                    originalLastName = userResponse.getLastName();
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {

            }
        });
    }

    private void populateInitialProfile() {
        Endpoints endpoints = APIClient.retrofit.create(Endpoints.class);
        Call<AccountResponse> call = endpoints.getAccount(
                "Token " + credentials.getToken(),
                credentials.getUserPK()
        );
        call.enqueue(new Callback<AccountResponse>() {
            @Override
            public void onResponse(Call<AccountResponse> call, Response<AccountResponse> response) {
                if (response.isSuccessful()) {
                    var accountResponse = response.body();
                    bioEditText.setText(accountResponse.getBio());
                    originalBio = accountResponse.getBio();
                }
            }

            @Override
            public void onFailure(Call<AccountResponse> call, Throwable t) {

            }
        });
    }

    private void saveChanges() {
        if (userSettingsChanged()) {
            var newFirstName = firstNameEditText.getText().toString();
            changeFirstName(newFirstName);
            var newLastName = lastNameEditText.getText().toString();
            changeLastName(newLastName);
        }
        if (passwordChanged()) {
            var newPassword = password1EditText.getText().toString();
            changePassword(newPassword);
        }
        if (accountSettingsChanged()) {
            var newBio = bioEditText.getText().toString();
            changeBio(newBio);
        }
    }

    private boolean userSettingsChanged() {
        var firstName = firstNameEditText.getText().toString();
        if (!originalFirstName.equals(firstName)) {
            return true;
        }
        var lastName = lastNameEditText.getText().toString();
        if (!originalLastName.equals(lastName)) {
            return true;
        }
        return false;
    }

    private boolean accountSettingsChanged() {
        var bio = bioEditText.getText().toString();
        if (!originalBio.equals(bio)) {
            return true;
        }
        return false;
    }

    private boolean passwordChanged() {
        var password1 = password1EditText.getText().toString();
        var password2 = password2EditText.getText().toString();
        if (password1.equals("") && password2.equals("")) {
            return false;
        }
        if (!password1.equals(password2)) {
            var errorMessage = "Passwords do not match";
            password1Layout.setError(errorMessage);
            password2Layout.setError(errorMessage);
            return false;
        }
        return true;
    }

    private void changeFirstName(String newFirstName) {
        Endpoints endpoints = APIClient.retrofit.create(Endpoints.class);
        Call<ResponseBody> call = endpoints.patchUser(
                "Token " + credentials.getToken(),
                new FirstNameRequest(newFirstName)
        );
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    private void changeLastName(String newLastName) {
        Endpoints endpoints = APIClient.retrofit.create(Endpoints.class);
        Call<ResponseBody> call = endpoints.patchUser(
                "Token " + credentials.getToken(),
                new LastNameRequest(newLastName)
        );
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    private void changePassword(String newPassword) {
        Endpoints endpoints = APIClient.retrofit.create(Endpoints.class);
        Call<ResponseBody> call = endpoints.patchUser(
                "Token " + credentials.getToken(),
                new PasswordRequest(newPassword)
        );
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    private void changeBio(String newBio) {
        Endpoints endpoints = APIClient.retrofit.create(Endpoints.class);
        Call<AccountResponse> call = endpoints.patchAccount(
                "Token " + credentials.getToken(),
                credentials.getUserPK(),
                new BioRequest(newBio)
        );
        call.enqueue(new Callback<AccountResponse>() {
            @Override
            public void onResponse(Call<AccountResponse> call, Response<AccountResponse> response) {

            }

            @Override
            public void onFailure(Call<AccountResponse> call, Throwable t) {

            }
        });
    }

    // Make an API to logout the user, delete the device stored credentials and go to the landing page
    private void logout() {
        Endpoints endpoints = APIClient.retrofit.create(Endpoints.class);
        Call<ResponseBody> call = endpoints.logout("Token " + credentials.getToken());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                flushStoredCredentials(getApplicationContext());
                var landingPageIntent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(landingPageIntent);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    // Delete the account of the user
    private void deleteAccount() {
        Endpoints endpoints = APIClient.retrofit.create(Endpoints.class);
        Call<ResponseBody> call = endpoints.deleteUser(
                "Token " + credentials.getToken(),
                credentials.getUserPK());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    flushStoredCredentials(getApplicationContext());
                    var landingPageIntent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(landingPageIntent);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

}