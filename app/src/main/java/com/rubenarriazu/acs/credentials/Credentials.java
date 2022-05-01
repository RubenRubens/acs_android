package com.rubenarriazu.acs.credentials;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;

import com.rubenarriazu.acs.config.Config;

import java.io.IOException;
import java.security.GeneralSecurityException;

/**
 * Android guide to store data securely:
 * https://developer.android.com/topic/security/data
 */
public class Credentials {

    private final String USERNAME_KEY = "username";
    private final String PASSWORD_KEY = "password";
    private final String TOKEN_KEY = "token";

    private Context context;

    private SharedPreferences getSharedPreferences() {
        SharedPreferences sharedPreferences = null;
        try {
            var keyGenParameterSpec = MasterKeys.AES256_GCM_SPEC;
            var mainKeyAlias = MasterKeys.getOrCreate(keyGenParameterSpec);
            var sharedPrefsFile = Config.CREDENTIALS;
            sharedPreferences = EncryptedSharedPreferences.create(
                    sharedPrefsFile,
                    mainKeyAlias,
                    context,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            );
        } catch (GeneralSecurityException | IOException e) {
            e.printStackTrace();
        }
        return sharedPreferences;
    }

    public Credentials(Context context) {
        this.context = context;
    }

    public void storeToken(String token) {
        var editor = getSharedPreferences().edit();
        editor.putString(TOKEN_KEY, token);
        editor.apply();
    }

    public String getToken() {
        var sharedPreferences = getSharedPreferences();
        return sharedPreferences.getString(TOKEN_KEY, "");
    }

    public void storeUsername(String username) {
        var editor = getSharedPreferences().edit();
        editor.putString(USERNAME_KEY, username);
        editor.apply();
    }

    public String getUsername() {
        var sharedPreferences = getSharedPreferences();
        return sharedPreferences.getString(USERNAME_KEY, "");
    }

    public void storePassword(String password) {
        var editor = getSharedPreferences().edit();
        editor.putString(PASSWORD_KEY, password);
        editor.apply();
    }

    public String getPassword() {
        var sharedPreferences = getSharedPreferences();
        return sharedPreferences.getString(PASSWORD_KEY, "");
    }

}
