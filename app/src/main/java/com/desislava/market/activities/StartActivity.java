package com.desislava.market.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.desislava.market.R;
import com.desislava.market.utils.Constants;

import static com.desislava.market.utils.Constants.KAUFLAND;
import static com.desislava.market.utils.Constants.LIDL;
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
        //TODO might ask before empty the cart if ok/not
        if (view.getId() == R.id.kauflandImgBnt) {
            temp=KAUFLAND;
            checkStore(temp);
            Toast.makeText(this, "Kaufland store", Toast.LENGTH_SHORT).show(); //Todo rename stores

        } else {
            temp=LIDL;
            checkStore(temp);
            Toast.makeText(this, "Lidl store", Toast.LENGTH_SHORT).show(); //Todo rename stores
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


