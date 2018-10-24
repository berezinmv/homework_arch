package com.seriabov.fintecharch.domain.datasource;

import com.seriabov.fintecharch.domain.entity.CoinInfo;

import java.util.List;

public interface CoinInfoDataSource {

    void getCoinInfoList(Callback callback);

    public static interface Callback {
        void onSuccess(List<CoinInfo> coinInfoList);
        void onError();
        void onComplete();
    }
}
