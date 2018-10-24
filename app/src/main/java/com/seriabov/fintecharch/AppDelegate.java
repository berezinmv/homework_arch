package com.seriabov.fintecharch;

import android.app.Application;
import android.content.Context;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.seriabov.fintecharch.api.Api;
import com.seriabov.fintecharch.api.CoinsDataSource;
import com.seriabov.fintecharch.domain.datasource.CoinInfoDataSource;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import timber.log.Timber;

public class AppDelegate extends Application {

    private Api apiService;

    public static AppDelegate from(Context context) {
        return (AppDelegate) context.getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initRetrofit();
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
    }

    private Api getApiService() {
        return apiService;
    }

    public CoinInfoDataSource getCoinDataSource() {
        return new CoinsDataSource(getApiService());
    }

    private void initRetrofit() {
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .build();

        apiService = new Retrofit.Builder()
                .baseUrl(BuildConfig.API_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
                .create(Api.class);
    }
}

