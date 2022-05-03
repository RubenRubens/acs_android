package com.rubenarriazu.acs.ui.search;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.rubenarriazu.acs.R;
import com.rubenarriazu.acs.api.APIClient;
import com.rubenarriazu.acs.api.Endpoints;
import com.rubenarriazu.acs.api.requests.SearchUserRequest;
import com.rubenarriazu.acs.api.responses.UserResponse;
import com.rubenarriazu.acs.credentials.Credentials;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchFragment extends Fragment {

    private ListView usersList;
    private EditText queryText;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_search, container, false);
        usersList = v.findViewById(R.id.users_list);
        queryText = v.findViewById(R.id.search);
        queryText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                search(queryText.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        return v;
    }

    private void search(String query) {
        SearchUserRequest searchUserRequest = new SearchUserRequest(query);
        Endpoints endpoints = APIClient.retrofit.create(Endpoints.class);
        var credentials = new Credentials(getContext());
        String token = credentials.getToken();
        Call<List<UserResponse>> call = endpoints.searchUser("Token " + token, searchUserRequest);
        call.enqueue(new Callback<List<UserResponse>>() {
            @Override
            public void onResponse(Call<List<UserResponse>> call, Response<List<UserResponse>> response) {
                if (response.isSuccessful()) {
                    List<UserResponse> users = response.body();
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            inflateList(users);
                        }
                    });
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
