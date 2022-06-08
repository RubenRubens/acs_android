package com.rubenarriazu.paranoid.ui.base_navigation;

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
import com.rubenarriazu.paranoid.ui.base_navigation.feed.FeedFragment;
import com.rubenarriazu.paranoid.ui.nav_menu.Settings;
import com.rubenarriazu.paranoid.ui.base_navigation.profile.ProfileFragment;
import com.rubenarriazu.paranoid.ui.base_navigation.search.SearchFragment;

public class BaseNavigation extends AppCompatActivity {

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_menu, menu);
        MenuItem configuration = menu.findItem(R.id.menu_configuration);
        var configurationIntent = new Intent(this, Settings.class);
        configuration.setIntent(configurationIntent);
        return true;
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
