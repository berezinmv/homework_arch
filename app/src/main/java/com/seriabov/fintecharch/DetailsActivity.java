package com.seriabov.fintecharch;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.seriabov.fintecharch.domain.entity.CoinInfo;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

public class DetailsActivity extends AppCompatActivity {

    private static final String EXTRA_INFO = "extra_info";

    public static void start(Activity activity, CoinInfo coinInfo) {
        Intent intent = new Intent(activity, DetailsActivity.class);
        intent.putExtra(EXTRA_INFO, coinInfo);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        CoinInfo info = (CoinInfo) getIntent().getSerializableExtra(EXTRA_INFO);
        getSupportActionBar().setTitle(info.getSymbol());
        ImageView logo = findViewById(R.id.coin_logo);
        String logoUrl = getString(R.string.coin_logo_url, info.getSymbol().toLowerCase());
        Glide.with(this)
                .load(logoUrl)
                .into(logo);

        TextView title = findViewById(R.id.coin_title);
        title.setText(info.getName());

        TextView price = findViewById(R.id.price_value);
        price.setText(formatPrice(info.getPriceUsd()));

        setPercentageChangeText(R.id.change_value_7d, info.getPercentChange7d());
        setPercentageChangeText(R.id.change_value_24h, info.getPercentChange24h());
        setPercentageChangeText(R.id.change_value_1h, info.getPercentChange1h());

        TextView marketCap = findViewById(R.id.market_cap_value);
        marketCap.setText(getString(R.string.price_format, info.getMarketCapUsd()));

        TextView lastUpdate = findViewById(R.id.last_update_value);
        lastUpdate.setText(DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM, Locale.getDefault())
                .format(new Date(info.getLastUpdated() * 1000)));
    }

    private String formatPrice(Object... args) {
        return getString(R.string.price_format, args);
    }

    private String formatPercent(double percent) {
        return getString(R.string.percent_format, percent);
    }

    private int getPercentColor(double percent) {
        int resId = percent > 0 ? R.color.green700 : R.color.red700;

        return ContextCompat.getColor(this, resId);
    }

    private void setPercentageChangeText(@IdRes int textViewId,
                                         double percent) {
        setPercentageChangeText(findViewById(textViewId), percent);
    }

    private void setPercentageChangeText(TextView textView,
                                         double percent) {
        textView.setText(formatPercent(percent));
        textView.setTextColor(getPercentColor(percent));
    }
}
