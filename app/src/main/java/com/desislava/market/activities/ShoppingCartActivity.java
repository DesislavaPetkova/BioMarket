package com.desislava.market.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.desislava.market.R;
import com.desislava.market.beans.Cart;
import com.desislava.market.beans.Product;
import com.desislava.market.cart.helper.ShoppingCartHelper;
import com.desislava.market.fragments.CartFragment;
import com.desislava.market.fragments.MenuListProductFragment;
import com.desislava.market.fragments.PriceCartFragment;
import com.desislava.market.utils.Constants;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class ShoppingCartActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
                                                                       CartFragment.OnListFragmentInteractionListener,
                                                                        PriceCartFragment.OnListFragmentInteractionListener ,
                                                                         ShoppingCartHelper.RecyclerItemTouchHelperListener {

    public static ArrayList<Cart> shoppingList=new ArrayList<>();
    private CartFragment cartFragment;
    private PriceCartFragment priceCartFragment;
    private  TextView total ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_cart);
        initToolbar();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view_cart);
        navigationView.setNavigationItemSelectedListener(this);
        Button placeOrder = (Button) findViewById(R.id.place_order);
        placeOrder.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent placeOrder = new Intent(ShoppingCartActivity.this, UserInfoActivity.class);
                        startActivity(placeOrder);
                    }
                }
        );

        //initFragmentManagers();
       total = findViewById(R.id.total);
       total.setText(("" + getTotalPrice()));
    }

    private void initToolbar() {
        Window w = getWindow();
        w.setStatusBarColor(Color.parseColor(Constants.COLOR));
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarCart);
        toolbar.setBackgroundColor(Color.parseColor(Constants.COLOR));
        toolbar.setTitle("Shopping cart"); //TODo constant
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.shopping_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
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
    public boolean onNavigationItemSelected(MenuItem item) { //TODO use same switch from main activity same result expected

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.shopping_layout);
        int id = item.getItemId();
        switch (id) {
            case R.id.allProducts: //TODO no category  might not need it !!
                MainActivity.categoryId = 5;
                break;
            case R.id.drinks:
                MainActivity.categoryId = 4;
                break;
            case R.id.freshMeat:
                MainActivity.categoryId = 3;
                break;
            case R.id.vegetables:
                MainActivity.categoryId = 2;
                break;
            case R.id.fruits:
                MainActivity.categoryId = 1;
                break;
        }

        drawer.closeDrawer(GravityCompat.START);
        finish();
        return true;
    }

    @Override
    public void onListFragmentInteraction(Cart cart) {
        Log.i("ShoppingCartActivity","onListFragmentInteraction====  clicked PRICE product"); //TOdo check if 1 it should be only 1 interface handling 2 actions
    }

    private float getTotalPrice() {
        float total = 0;
        for (Cart pr : shoppingList) {
            total += pr.getQuantityInt() * pr.getPriceAsInt();
        }
        return total;
    }


    private void initFragmentManagers() {
        List<Fragment> mng = getSupportFragmentManager().getFragments();
        //Log.i("Fragments size :  ", ""+mng.size());
        cartFragment = (CartFragment) mng.get(0);
        priceCartFragment = (PriceCartFragment) mng.get(1);
        if (cartFragment == null || priceCartFragment == null) {
            throw new ExceptionInInitializerError("Could not get fragment managers");
        }
    }



    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        Log.i("On Swipe - UPDATE","Item removed  update adapters total price updated");
        initFragmentManagers();
        cartFragment.adapterRemove(viewHolder);
        priceCartFragment.adapterPriceRemove();
        total.setText(("" + getTotalPrice()));

    }
}
