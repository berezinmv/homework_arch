package com.seriabov.fintecharch.view;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.seriabov.fintecharch.domain.datasource.CoinInfoDataSource;
import com.seriabov.fintecharch.domain.entity.CoinInfo;

import java.util.List;

public class MainViewModel extends ViewModel {
    private final CoinInfoDataSource dataSource;

    private MutableLiveData<List<CoinInfo>> listItems;
    private MutableLiveData<Boolean> contentVisible;
    private MutableLiveData<Boolean> loadingVisible;
    private MutableLiveData<Boolean> errorVisible;

    public MainViewModel(CoinInfoDataSource dataSource) {
        this.dataSource = dataSource;
        listItems = new MutableLiveData<>();
        contentVisible = new MutableLiveData<>();
        loadingVisible = new MutableLiveData<>();
        errorVisible = new MutableLiveData<>();
    }

    public LiveData<List<CoinInfo>> getListItems() {
        return listItems;
    }

    public LiveData<Boolean> getContentVisible() {
        return contentVisible;
    }

    public LiveData<Boolean> getLoadingVisible() {
        return loadingVisible;
    }

    public LiveData<Boolean> getErrorVisible() {
        return errorVisible;
    }

    public void updateData() {
        loadingStart();
        dataSource.getCoinInfoList(new CoinInfoDataSource.Callback() {
            @Override
            public void onSuccess(List<CoinInfo> list) {
                loadingSuccess(list);
            }

            @Override
            public void onError() {
                loadingFailure();
            }

            @Override
            public void onComplete() {
                loadingEnd();
            }
        });
    }

    private void loadingStart() {
        loadingVisible.postValue(true);
    }

    private void loadingEnd() {
        loadingVisible.postValue(false);
    }

    private void loadingSuccess(List<CoinInfo> coinInfoList) {
        listItems.postValue(coinInfoList);
        contentVisible.postValue(true);
        errorVisible.postValue(false);
    }

    private void loadingFailure() {
        contentVisible.postValue(false);
        errorVisible.postValue(true);
    }
}
