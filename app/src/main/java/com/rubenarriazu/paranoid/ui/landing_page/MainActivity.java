package com.rubenarriazu.paranoid.ui.landing_page;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.button.MaterialButton;
import com.rubenarriazu.paranoid.R;
import com.rubenarriazu.paranoid.credentials.Credentials;
import com.rubenarriazu.paranoid.ui.BaseNavigation;
import com.rubenarriazu.paranoid.ui.login.Login;
import com.rubenarriazu.paranoid.ui.registration.RegistrationView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (credentialsAreStored()) {
            Intent baseIntent = new Intent(MainActivity.this, BaseNavigation.class);
            startActivity(baseIntent);
        }

        MaterialButton signUp = findViewById(R.id.sign_up);
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registrationIntent = new Intent(MainActivity.this, RegistrationView.class);
                startActivity(registrationIntent);
            }
        });

        MaterialButton login = findViewById(R.id.sign_in);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginIntent = new Intent(MainActivity.this, Login.class);
                startActivity(loginIntent);
            }
        });

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (credentialsAreStored()) {
            Intent baseIntent = new Intent(MainActivity.this, BaseNavigation.class);
            startActivity(baseIntent);
        }
    }

    private boolean credentialsAreStored() {
        Credentials credentials = new Credentials(MainActivity.this);
        var username = credentials.getUsername();
        var password = credentials.getPassword();
        var token = credentials.getPassword();
        return !username.isEmpty() && !password.isEmpty() && !token.isEmpty();
    }

}
