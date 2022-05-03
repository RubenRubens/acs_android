package com.rubenarriazu.paranoid.ui.search;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.rubenarriazu.paranoid.R;
import com.rubenarriazu.paranoid.api.responses.UserResponse;

import java.util.List;

public class UserAdapter extends ArrayAdapter<UserResponse> {

    private List<UserResponse> users;

    public UserAdapter(@NonNull Context context, List<UserResponse> users) {
        super(context, R.layout.element_user, users);
        this.users = users;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View element = inflater.inflate(R.layout.element_user, null, false);
        TextView name = element.findViewById(R.id.name);
        TextView username = element.findViewById(R.id.username);
        UserResponse user = users.get(position);
        name.setText(user.getFirstName() + " " + user.getLastName());
        username.setText("@" + user.getUsername());
        return element;
    }
}
