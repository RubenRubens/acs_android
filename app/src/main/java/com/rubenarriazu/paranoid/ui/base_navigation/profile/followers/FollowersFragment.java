package com.rubenarriazu.paranoid.ui.base_navigation.profile.followers;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.rubenarriazu.paranoid.R;
import com.rubenarriazu.paranoid.api.APIClient;
import com.rubenarriazu.paranoid.api.Endpoints;
import com.rubenarriazu.paranoid.api.responses.UserResponse;
import com.rubenarriazu.paranoid.credentials.Credentials;
import com.rubenarriazu.paranoid.ui.base_navigation.search.UserAdapter;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FollowersFragment extends Fragment {

    private ListView usersList;

    private static final String USER_PK_KEY = "user_pk";

    public static FollowersFragment newInstance(int userPK) {
        var followersFragment = new FollowersFragment();
        var bundle = new Bundle();
        bundle.putInt(USER_PK_KEY, userPK);
        followersFragment.setArguments(bundle);
        return followersFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_followers, container, false);
        usersList = v.findViewById(R.id.users_list);
        int userPK = getArguments().getInt(USER_PK_KEY);
        apiCallFollowers(userPK);
        return v;
    }

    private void apiCallFollowers(int userPK) {
        Endpoints endpoints = APIClient.retrofit.create(Endpoints.class);
        var credentials = new Credentials(getContext());
        String token = credentials.getToken();
        Call<List<UserResponse>> call = endpoints.getFollowers("Token " + token, userPK);
        call.enqueue(new Callback<List<UserResponse>>() {
            @Override
            public void onResponse(Call<List<UserResponse>> call, Response<List<UserResponse>> response) {
                if (response.isSuccessful()) {
                    inflateList(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<UserResponse>> call, Throwable t) {

            }
        });
    }

    private void inflateList(List<UserResponse> users) {
        UserAdapter userAdapter = new UserAdapter(getContext(), users);
        usersList.setAdapter(userAdapter);
    }

}
