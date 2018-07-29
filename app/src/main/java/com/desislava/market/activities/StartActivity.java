package com.desislava.market.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.desislava.market.R;
import com.desislava.market.utils.Constants;

import static com.desislava.market.utils.Constants.DOGGY;
import static com.desislava.market.utils.Constants.MARKO;
import static com.desislava.market.utils.Constants.PEACHY;
import static com.desislava.market.utils.Constants.STORE;

public class StartActivity extends AppCompatActivity {
    static String store ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        Window w=getWindow();
        w.setStatusBarColor(Constants.GREEN_COLOR);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(Constants.GREEN_COLOR);
        toolbar.setTitle(Constants.CHOOSE_MARKET);
        setSupportActionBar(toolbar);

    }


    public void onClick(View view) {
        String temp;
        if (view.getId() == R.id.peachyImgBnt) {
            temp=PEACHY;
            checkStore(temp);
            Toast.makeText(this, "PEACHY store", Toast.LENGTH_SHORT).show();

        }else if (view.getId() == R.id.markoImgBnt) {
            temp=MARKO;
            checkStore(temp);
            Toast.makeText(this, "MARKO store", Toast.LENGTH_SHORT).show();

        }
        else {
            temp=DOGGY;
            checkStore(temp);
            Toast.makeText(this, "DOGGY store", Toast.LENGTH_SHORT).show();
        }
        store = temp;
        Intent intent = new Intent(StartActivity.this, MainActivity.class);
        intent.putExtra(STORE, store);
        startActivity(intent);
    }

    private void checkStore(String temp) {
        if(!ShoppingCartActivity.shoppingList.isEmpty() && !store.equals(temp)){
            ShoppingCartActivity.shoppingList.clear();
        }

    }

}


