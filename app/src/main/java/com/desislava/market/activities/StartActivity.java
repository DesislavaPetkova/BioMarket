package com.desislava.market.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.desislava.market.R;

import static com.desislava.market.utils.Constants.STORE;

public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
/*
        ImageButton img = findViewById(R.id.kauflandImgBnt);*/
    }


    public void onClick(View view) {
        String store = "lidl";
//TODO might add all stores products show
        if (view.getId() == R.id.kauflandImgBnt) {
            store = "kaufland";
            Toast.makeText(this, "Kaufland store", Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(this, "Lidl store", Toast.LENGTH_SHORT).show();
        }
        Intent intent = new Intent(StartActivity.this, MainActivity.class);
        intent.putExtra(STORE, store);
        startActivity(intent);
    }


}


