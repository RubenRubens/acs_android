package com.rubenarriazu.acs.ui.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;
import com.rubenarriazu.acs.R;
import com.rubenarriazu.acs.api.APIClient;
import com.rubenarriazu.acs.api.Endpoints;
import com.rubenarriazu.acs.api.responses.UserResponse;
import com.rubenarriazu.acs.credentials.Credentials;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileFragment extends Fragment {

    private TextView name;
    private TextView username;
    private MaterialButton followersButton;
    private MaterialButton followingButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile, container, false);
        return v;
    }

    private void assignViews(View v) {
        name = v.findViewById(R.id.text_name);
        username = v.findViewById(R.id.text_username);
        followersButton = v.findViewById(R.id.button_followers);
        followingButton = v.findViewById(R.id.button_following);
    }

    // Set the view name and username
    private void setProfileInfo(UserResponse userResponse) {
        String firstName = userResponse.getFirstName();
        String lastName = userResponse.getLastName();
        name.setText(firstName + " " + lastName);
        username.setText("@" + userResponse.getUsername());
    }

    // Get the username, first name and last name from the API
    private void initProfileInfo() {
        var credentials = new Credentials(getContext());
        String token = credentials.getToken();
        Endpoints endpoints = APIClient.retrofit.create(Endpoints.class);
        Call<UserResponse> call = endpoints.getUser("Token " + token);
        call.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if (response.isSuccessful()) {
                    UserResponse userResponse = response.body();
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            setProfileInfo(userResponse);
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
            }
        });
    }

}
