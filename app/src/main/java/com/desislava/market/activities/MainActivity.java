package com.desislava.market.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.desislava.market.R;
import com.desislava.market.beans.Product;
import com.desislava.market.fragments.MenuListProductFragment;
import com.desislava.market.fragments.ProductInfoFragment;
import com.desislava.market.server.communication.JSONResponse;
import com.desislava.market.utils.Constants;

import java.util.logging.Logger;

import static android.view.View.INVISIBLE;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, MenuListProductFragment.OnListFragmentInteractionListener, ProductInfoFragment.OnFragmentInteractionListener {

    FrameLayout frameLayout;
    CoordinatorLayout.LayoutParams params;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(getLocalClassName() + " onCreate", "ENTER ");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String store = getIntent().getStringExtra(Constants.STORE);
        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initStore(store);
        frameLayout = (FrameLayout) findViewById(R.id.menuListInfoProduct);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity
                        .this, ShoppingCartActivity.class);
                startActivity(intent);
            }
        });

        MenuListProductFragment menuFragment = new MenuListProductFragment();
        getSupportFragmentManager().
                beginTransaction().add(R.id.menuListInfoProduct, menuFragment, "menu list").commit();
        params = (CoordinatorLayout.LayoutParams) frameLayout.getLayoutParams();
        params.setMargins(0, 300, 0, 0);
        frameLayout.setLayoutParams(params);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            params.setMargins(0, 300, 0, 0);
            frameLayout.setLayoutParams(params);
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        FragmentManager fragmentManager = getSupportFragmentManager();
        int categoryId = 0;
        int id = item.getItemId();
        switch (id) {
            case R.id.allProducts:
                categoryId = 5;
                break;
            case R.id.drinks:
                categoryId = 4;
                break;
            case R.id.freshMeat:
                categoryId = 3;
                break;
            case R.id.vegetables:
                categoryId = 2;
                break;
            case R.id.fruits:
                categoryId = 1;
        }

        MenuListProductFragment fragment = MenuListProductFragment.newInstance(categoryId);

        if (fragmentManager != null) {
            fragmentManager.beginTransaction().replace(R.id.updateFragment, fragment).commit();
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onListFragmentInteraction(Product product) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        ProductInfoFragment productInfoFragment = ProductInfoFragment.newInstance(product);

        if (fragmentManager != null) {
            fragmentManager.beginTransaction().replace(R.id.menuListInfoProduct, productInfoFragment).addToBackStack(null).commit();
            params.setMargins(0, 50, 0, 0);
            frameLayout.setLayoutParams(params);
        }

    }

    private void initStore(String store) {
        try {
            JSONResponse json = new JSONResponse(this, store);
            json.execute();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFragmentInteraction(Product product) {
        Log.i(getLocalClassName() + "onFragmentInteraction", "ENTER");
    }
}
