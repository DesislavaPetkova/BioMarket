package com.desislava.market.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.desislava.market.R;
import com.desislava.market.beans.Cart;
import com.desislava.market.beans.Product;
import com.desislava.market.dummy.DummyContent;
import com.desislava.market.fragments.CartFragment;
import com.desislava.market.fragments.PriceCartFragment;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class ShoppingCartActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
                                                                       CartFragment.OnListFragmentInteractionListener,
                                                                        PriceCartFragment.OnListFragmentInteractionListener {

    public static ArrayList<Cart> shoppingList=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_cart);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarCart);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.shopping_layout);
       /* ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();*/

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view_cart);
        navigationView.setNavigationItemSelectedListener(this);
        Button placeOrder=(Button)findViewById(R.id.place_order);
        placeOrder.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent placeOrder=new Intent(ShoppingCartActivity.this, UserInfoActivity.class);
                        startActivity(placeOrder);
                    }
                }
        );
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.shopping_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected( MenuItem item){ //TODO use same switch from main activity same result expected
    int id = item.getItemId();

    return true;
    }

    @Override
    public void onListFragmentInteraction(Product product) {

        Log.i("ShoppingCartActivity","onListFragmentInteraction==== clicked product");

    }

    @Override
    public void onListFragmentInteraction(Cart cart) {

        Log.i("ShoppingCartActivity","onListFragmentInteraction====  clicked PRICE product");
    }
}
