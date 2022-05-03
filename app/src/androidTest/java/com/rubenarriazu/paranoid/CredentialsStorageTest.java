package com.rubenarriazu.acs;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

import com.rubenarriazu.acs.credentials.Credentials;

@RunWith(AndroidJUnit4.class)
public class CredentialsStorageTest {

    @Test
    public void credentialsStorage() {
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        Credentials credentials = new Credentials(context);

        // Token
        credentials.storeToken("token_001");
        String storedToken = credentials.getToken();
        assertEquals("token_001", storedToken);

        // Username
        credentials.storeUsername("user_001");
        String storedUsername = credentials.getUsername();
        assertEquals("user_001", storedUsername);

        // Password
        credentials.storePassword("password_001");
        String storedPassword = credentials.getPassword();
        assertEquals("password_001", storedPassword);
    }

}