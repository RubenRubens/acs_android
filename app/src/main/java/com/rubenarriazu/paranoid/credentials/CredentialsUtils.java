package com.rubenarriazu.paranoid.credentials;

import android.content.Context;

import com.rubenarriazu.paranoid.errors.ErrorCodes;

public class CredentialsUtils {
    public static void flushStoredCredentials(Context context) {
        Credentials credentials = new Credentials(context);
        credentials.storeUserPK(ErrorCodes.NULL_USER_PK);
        credentials.storeUsername("");
        credentials.storePassword("");
        credentials.storeToken("");
    }
}
