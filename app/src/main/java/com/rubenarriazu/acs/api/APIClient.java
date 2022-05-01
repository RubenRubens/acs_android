package com.rubenarriazu.acs.api;

import com.rubenarriazu.acs.config.Config;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIClient {

    public static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(Config.WEB_SERVER)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

}
