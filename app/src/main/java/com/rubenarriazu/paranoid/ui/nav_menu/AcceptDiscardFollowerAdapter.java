package com.rubenarriazu.paranoid.ui.nav_menu;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.button.MaterialButton;
import com.rubenarriazu.paranoid.R;
import com.rubenarriazu.paranoid.api.APIClient;
import com.rubenarriazu.paranoid.api.Endpoints;
import com.rubenarriazu.paranoid.api.requests.AcceptPetitionRequest;
import com.rubenarriazu.paranoid.api.responses.PetitionResponse;
import com.rubenarriazu.paranoid.api.responses.UserResponse;
import com.rubenarriazu.paranoid.credentials.Credentials;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AcceptDiscardFollowerAdapter extends ArrayAdapter<PetitionResponse> {

    private List<PetitionResponse> followerPetitions;
    TextView name;
    TextView username;
    MaterialButton acceptButton;
    MaterialButton discardButton;

    public AcceptDiscardFollowerAdapter(@NonNull Context context, List<PetitionResponse> followerPetitions) {
        super(context, R.layout.element_user, followerPetitions);
        this.followerPetitions = followerPetitions;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View element = inflater.inflate(R.layout.element_possible_follower, null, false);
        name = element.findViewById(R.id.name);
        username = element.findViewById(R.id.username);
        acceptButton = element.findViewById(R.id.button_accept);
        discardButton = element.findViewById(R.id.button_discard);
        PetitionResponse followerPetition = followerPetitions.get(position);
        Log.i("murchante", "" + acceptButton);
        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                acceptFollowingPetition(followerPetition.getPossibleFollower());
            }
        });
        discardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                discardFollowingPetition(followerPetition.getId());
            }
        });
        inflateUserData(followerPetition.getPossibleFollower());
        return element;
    }

    private void  inflateUserData(int userPK) {
        Endpoints endpoints = APIClient.retrofit.create(Endpoints.class);
        var credentials = new Credentials(getContext());
        String token = credentials.getToken();
        Call<UserResponse> call = endpoints.getUser("Token " + token, userPK);
        call.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if (response.isSuccessful()) {
                    UserResponse user = response.body();
                    name.setText(user.getFirstName() + " " + user.getLastName());
                    username.setText("@" + user.getUsername());

                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {

            }
        });
    }

    private void acceptFollowingPetition(int userPK) {
        Endpoints endpoints = APIClient.retrofit.create(Endpoints.class);
        var credentials = new Credentials(getContext());
        String token = credentials.getToken();
        Call<PetitionResponse> call = endpoints.acceptPetition(
                "Token " + token, new AcceptPetitionRequest(userPK));
        call.enqueue(new Callback<PetitionResponse>() {
            @Override
            public void onResponse(Call<PetitionResponse> call, Response<PetitionResponse> response) {
                occultButtons();
                Toast.makeText(getContext(), "The petition has been accepted", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<PetitionResponse> call, Throwable t) {
            }
        });
    }

    private void discardFollowingPetition(int petitionPK) {
        Endpoints endpoints = APIClient.retrofit.create(Endpoints.class);
        var credentials = new Credentials(getContext());
        String token = credentials.getToken();
        Call<PetitionResponse> call = endpoints.discardPetition("Token " + token, petitionPK);
        call.enqueue(new Callback<PetitionResponse>() {
            @Override
            public void onResponse(Call<PetitionResponse> call, Response<PetitionResponse> response) {
                if (response.isSuccessful()) {
                    occultButtons();
                    Toast.makeText(getContext(), "The petition has been discarded", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PetitionResponse> call, Throwable t) {
            }
        });
    }

    private void occultButtons() {
        acceptButton.setVisibility(View.GONE);
        discardButton.setVisibility(View.GONE);
    }

}
