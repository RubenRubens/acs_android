package com.rubenarriazu.paranoid.ui.profile;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.button.MaterialButton;
import com.rubenarriazu.paranoid.R;
import com.rubenarriazu.paranoid.api.APIClient;
import com.rubenarriazu.paranoid.api.Endpoints;
import com.rubenarriazu.paranoid.api.responses.AccountResponse;
import com.rubenarriazu.paranoid.api.responses.UserResponse;
import com.rubenarriazu.paranoid.credentials.Credentials;
import com.rubenarriazu.paranoid.ui.followers.FollowersFragment;
import com.rubenarriazu.paranoid.ui.following.FollowingFragment;

import java.io.InputStream;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Given a user PK, inflates a profile with the information
 * of that user.
 */
public class ProfileFragment extends Fragment {

    private static final String USER_PK = "user_pk";

    private int userPK;
    private TextView name;
    private TextView username;
    private TextView bio;
    private MaterialButton followersButton;
    private MaterialButton followingButton;
    private ImageView profilePicture;
    private ImageView postImage;

    // extracted from https://programmer.group/android-confusion-why-pass-parameters-with-fragment.setarguments-bundle-bundle.html
    public static Fragment newInstance(int userPK) {
        var profileFragment = new ProfileFragment();
        var bundle = new Bundle();
        bundle.putInt(USER_PK, userPK);
        profileFragment.setArguments(bundle);
        return profileFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile, container, false);
        assignUserPK(getArguments());
        assignViews(v);
        initListenersToButtons(userPK);
        initProfileInfo(userPK);
        return v;
    }

    private void assignUserPK(Bundle bundle) {
        this.userPK = bundle.getInt(USER_PK);
    }

    private void assignViews(View v) {
        name = v.findViewById(R.id.text_name);
        username = v.findViewById(R.id.text_username);
        bio = v.findViewById(R.id.text_bio);
        followersButton = v.findViewById(R.id.button_followers);
        followingButton = v.findViewById(R.id.button_following);
        profilePicture = v.findViewById(R.id.image_profile);
    }

    // Set the information of the user and its account
    private void setProfileInfo(UserResponse userResponse) {
        String firstName = userResponse.getFirstName();
        String lastName = userResponse.getLastName();
        name.setText(firstName + " " + lastName);
        username.setText("@" + userResponse.getUsername());
        setProfilePicture(userPK);
        setBio(userPK);
    }

    // Set a listener to followers and following buttons
    private void initListenersToButtons(int userPK) {
        followersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                var followersFragment = FollowersFragment.newInstance(userPK);
                setFragment(followersFragment);
            }
        });
        followingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                var followingFragment = FollowingFragment.newInstance(userPK);
                setFragment(followingFragment);
            }
        });
    }

    public void setFragment(Fragment fragment) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, fragment);
        fragmentTransaction.commit();
    }

    // Get the username, first name and last name from the API
    private void initProfileInfo(int userPK) {
        var credentials = new Credentials(getContext());
        String token = credentials.getToken();
        Endpoints endpoints = APIClient.retrofit.create(Endpoints.class);
        Call<UserResponse> call = endpoints.getUser("Token " + token, userPK);
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

    // Sets the profile bio from the userPK
    private void setBio(int userPK) {
        var credentials = new Credentials(getContext());
        String token = credentials.getToken();
        Endpoints endpoints = APIClient.retrofit.create(Endpoints.class);
        Call<AccountResponse> call = endpoints.getAccount("Token " + token, userPK);
        call.enqueue(new Callback<AccountResponse>() {
            @Override
            public void onResponse(Call<AccountResponse> call, Response<AccountResponse> response) {
                if (response.isSuccessful()) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            bio.setText(response.body().getBio());
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<AccountResponse> call, Throwable t) {
            }
        });
    }

    // Given the userPK, inflates the profile picture for his/her account
    private void setProfilePicture(int userPK) {
        var credentials = new Credentials(getContext());
        String token = credentials.getToken();
        Endpoints endpoints = APIClient.retrofit.create(Endpoints.class);
        Call<ResponseBody> call = endpoints.getProfilePicture("Token " + token, userPK);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    InputStream bytes = response.body().byteStream();
                    Bitmap bitmap = BitmapFactory.decodeStream(bytes);
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            profilePicture.setImageBitmap(bitmap);
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
            }
        });
    }

}
