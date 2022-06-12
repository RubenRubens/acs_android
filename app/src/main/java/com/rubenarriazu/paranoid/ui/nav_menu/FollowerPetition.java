package com.rubenarriazu.paranoid.ui.nav_menu;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

import com.rubenarriazu.paranoid.R;
import com.rubenarriazu.paranoid.api.APIClient;
import com.rubenarriazu.paranoid.api.Endpoints;
import com.rubenarriazu.paranoid.api.responses.PetitionResponse;
import com.rubenarriazu.paranoid.api.responses.UserResponse;
import com.rubenarriazu.paranoid.credentials.Credentials;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FollowerPetition extends AppCompatActivity {

    ListView followerPetitions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follower_petition);
        followerPetitions = findViewById(R.id.petitions_list);
        inflateList();
    }

    private void inflateList() {
        Endpoints endpoints = APIClient.retrofit.create(Endpoints.class);
        var credentials = new Credentials(this);
        String token = credentials.getToken();
        Call<List<PetitionResponse>> call = endpoints.getPetitions("Token " + token);
        call.enqueue(new Callback<List<PetitionResponse>>() {
            @Override
            public void onResponse(Call<List<PetitionResponse>> call, Response<List<PetitionResponse>> response) {
                if (response.isSuccessful()) {
                    var followerPetitionsAdapter = new AcceptDiscardFollowerAdapter(FollowerPetition.this, response.body());
                    followerPetitions.setAdapter(followerPetitionsAdapter);
                }
            }

            @Override
            public void onFailure(Call<List<PetitionResponse>> call, Throwable t) {

            }
        });
    }

}