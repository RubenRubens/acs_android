package com.rubenarriazu.paranoid.ui.following;

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
import com.rubenarriazu.paranoid.ui.search.UserAdapter;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FollowingFragment extends Fragment {

    private ListView usersList;

    private static final String USER_PK_KEY = "user_pk";

    public static FollowingFragment newInstance(int userPK) {
        var followingFragment = new FollowingFragment();
        var bundle = new Bundle();
        bundle.putInt(USER_PK_KEY, userPK);
        followingFragment.setArguments(bundle);
        return followingFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_following, container, false);
        usersList = v.findViewById(R.id.users_list);
        int userPK = getArguments().getInt(USER_PK_KEY);
        apiCallFollowing(userPK);
        return v;
    }
    
    private void apiCallFollowing(int userPK) {
        Endpoints endpoints = APIClient.retrofit.create(Endpoints.class);
        var credentials = new Credentials(getContext());
        String token = credentials.getToken();
        Call<List<UserResponse>> call = endpoints.getFollowing("Token " + token, userPK);
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
