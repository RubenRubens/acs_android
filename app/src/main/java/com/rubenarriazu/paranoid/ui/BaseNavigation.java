package com.rubenarriazu.paranoid.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.button.MaterialButton;
import com.rubenarriazu.paranoid.R;
import com.rubenarriazu.paranoid.credentials.Credentials;
import com.rubenarriazu.paranoid.ui.feed.FeedFragment;
import com.rubenarriazu.paranoid.ui.nav_menu.Configuration;
import com.rubenarriazu.paranoid.ui.profile.ProfileFragment;
import com.rubenarriazu.paranoid.ui.search.SearchFragment;

public class BaseNavigation extends AppCompatActivity {

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_configuration:
                var intent = new Intent(getApplicationContext(), Configuration.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_navigation);

        MaterialButton buttonHome = findViewById(R.id.button_home);
        MaterialButton buttonSearch = findViewById(R.id.button_search);
        MaterialButton buttonProfile = findViewById(R.id.button_profile);

        buttonHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FeedFragment feedFragment = new FeedFragment();
                setFragment(feedFragment);
            }
        });
        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchFragment searchFragment = new SearchFragment();
                setFragment(searchFragment);
            }
        });
        buttonProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int userPK = getUserPK();
                var profileFragment = ProfileFragment.newInstance(userPK);
                setFragment(profileFragment);
            }
        });
    }

    public void setFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, fragment);
        fragmentTransaction.commit();
    }

    public int getUserPK() {
        var credentials = new Credentials(getApplicationContext());
        return credentials.getUserPK();
    }

}
