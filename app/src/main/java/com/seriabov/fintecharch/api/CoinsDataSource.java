package com.seriabov.fintecharch.api;

import com.seriabov.fintecharch.domain.datasource.CoinInfoDataSource;
import com.seriabov.fintecharch.domain.entity.CoinInfo;

import java.util.List;
import java.util.Timer;

import retrofit2.Call;
import retrofit2.Response;
import timber.log.Timber;

public class CoinsDataSource implements CoinInfoDataSource {
    private Api api;

    public CoinsDataSource(Api api) {
        this.api = api;
    }

    @Override
    public void getCoinInfoList(Callback callback) {
        api.getCoinsList().enqueue(new retrofit2.Callback<List<CoinInfo>>() {
            @Override
            public void onResponse(Call<List<CoinInfo>> call, Response<List<CoinInfo>> response) {
                callback.onSuccess(response.body());
                callback.onComplete();
            }

            @Override
            public void onFailure(Call<List<CoinInfo>> call, Throwable t) {
                Timber.d(t);
                callback.onError();
                callback.onComplete();
            }
        });
    }
}
