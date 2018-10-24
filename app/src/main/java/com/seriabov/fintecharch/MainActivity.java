package com.seriabov.fintecharch;

import android.arch.lifecycle.LiveData;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.seriabov.fintecharch.view.CoinsAdapter;
import com.seriabov.fintecharch.view.MainViewModel;

/*
 * TODO:
 * 1) Подключить ViewModel и LiveData из Android Architecture components
 * 2) Разделить классы по пакетам
 * 3) Внедрить в проект архитектуру MVVM, вынести бизнес-логику во вьюмодель.
 * В идеале вьюмодель не должна содержать в себе андроид-компонентов (таких как Context)
 * 4) Сделать так, чтобы при повороте экрана данные не перезапрашивались заново,
 * а использовались полученные ранее
 * 5) Don't repeat yourself - если найдете в коде одинаковые куски кода, выносите в утилитные классы
 */

public class MainActivity extends AppCompatActivity {

    private MainViewModel viewModel;

    private CoinsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initToolbar();
        initActionButton();
        initRecyclerView();
        initViewModel();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_refresh) {
            updateData();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public Object onRetainCustomNonConfigurationInstance() {
        return viewModel;
    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void initActionButton() {
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> updateData());
    }

    private void initRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.main_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CoinsAdapter(coinInfo -> DetailsActivity.start(MainActivity.this, coinInfo));
        recyclerView.setAdapter(adapter);
    }

    private void initViewModel() {
        viewModel = (MainViewModel) getLastCustomNonConfigurationInstance();
        if (viewModel == null) {
            viewModel = new MainViewModel(AppDelegate.from(this).getCoinDataSource());
            viewModel.updateData();
        }

        viewModel.getListItems()
                .observe(this, coinInfoList -> adapter.setData(coinInfoList));
        bindVisibility(R.id.main_recycler_view, viewModel.getContentVisible());
        bindVisibility(R.id.loading_layout, viewModel.getLoadingVisible());
        bindVisibility(R.id.error_layout, viewModel.getErrorVisible());
    }

    private void updateData() {
        viewModel.updateData();
    }

    private void bindVisibility(@IdRes int viewId, LiveData<Boolean> data) {
        bindVisibility(findViewById(viewId), data);
    }

    private void bindVisibility(View view, LiveData<Boolean> data) {
        data.observe(
                this,
                visible -> {
                    if (visible == null) {
                        visible = false;
                    }

                    view.setVisibility(visible ? View.VISIBLE : View.GONE);
                }
        );
    }
}
