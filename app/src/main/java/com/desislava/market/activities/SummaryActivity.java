package com.desislava.market.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Window;

import com.desislava.market.R;
import com.desislava.market.fragments.SummaryActivityFragment;
import com.desislava.market.utils.Constants;

public class SummaryActivity extends AppCompatActivity implements  SummaryActivityFragment.SummaryFragmentListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);
        Window w = getWindow();
        w.setStatusBarColor(Constants.GREEN_COLOR);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(Constants.GREEN_COLOR);
        toolbar.setTitle(Constants.PLACED);
        setSupportActionBar(toolbar);

        FloatingActionButton newOrder = findViewById(R.id.fab);
        newOrder.setOnClickListener(view -> {
            Intent intent = new Intent(SummaryActivity
                    .this, StartActivity.class);
            startActivity(intent);

        });
        SummaryActivityFragment summary = SummaryActivityFragment.newInstance(1);
        getSupportFragmentManager().beginTransaction().add(R.id.summary_info, summary, "summary order").commit();
    }

    @Override
    public void summaryFragment(String item) {







    }
}
