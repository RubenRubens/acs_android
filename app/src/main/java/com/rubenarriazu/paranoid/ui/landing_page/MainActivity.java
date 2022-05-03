package com.rubenarriazu.acs.ui.landing_page;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.button.MaterialButton;
import com.rubenarriazu.acs.R;
import com.rubenarriazu.acs.ui.login.Login;
import com.rubenarriazu.acs.ui.registration.RegistrationView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
                Intent registrationIntent = new Intent(MainActivity.this, Login.class);
                startActivity(registrationIntent);
            }
        });

    }

}
