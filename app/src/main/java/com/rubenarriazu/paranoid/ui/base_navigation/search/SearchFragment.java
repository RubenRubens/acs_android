package com.rubenarriazu.paranoid.ui.base_navigation.search;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.rubenarriazu.paranoid.R;
import com.rubenarriazu.paranoid.api.APIClient;
import com.rubenarriazu.paranoid.api.Endpoints;
import com.rubenarriazu.paranoid.api.requests.SearchUserRequest;
import com.rubenarriazu.paranoid.api.responses.AccountResponse;
import com.rubenarriazu.paranoid.api.responses.UserResponse;
import com.rubenarriazu.paranoid.credentials.Credentials;
import com.rubenarriazu.paranoid.ui.base_navigation.profile.LimitedAccessProfileFragment;
import com.rubenarriazu.paranoid.ui.base_navigation.profile.ProfileFragment;

import java.util.List;

import okhttp3.ResponseBody;
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
        usersList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i("murchante", "" + users.get(position).getId());
                Log.i("murchante", users.get(position).getFirstName());
                int userPK = users.get(position).getId();
                profileApiCall(userPK);
            }
        });
    }

    private void profileApiCall(int userPK) {
        Endpoints endpoints = APIClient.retrofit.create(Endpoints.class);
        var credendials = new Credentials(getContext());
        String token = credendials.getToken();
        Call<ResponseBody> call = endpoints.iAmAFollower("Token " + token, userPK);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    var profileFragment = ProfileFragment.newInstance(userPK);
                    setFragmentFromFragment(profileFragment);
                } else {
                    var limitedAccessProfileFragment = LimitedAccessProfileFragment.newInstance(userPK);
                    setFragmentFromFragment(limitedAccessProfileFragment);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    public void setFragmentFromFragment(Fragment fragment) {
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, fragment);
        fragmentTransaction.commit();
    }

}
