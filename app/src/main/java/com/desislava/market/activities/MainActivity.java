package com.desislava.market.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentTransaction;
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
import android.view.Window;
import android.widget.FrameLayout;

import com.desislava.market.R;
import com.desislava.market.beans.Product;
import com.desislava.market.fragments.MenuListProductFragment;
import com.desislava.market.fragments.ProductInfoFragment;
import com.desislava.market.server.communication.JSONResponse;
import com.desislava.market.utils.Constants;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, MenuListProductFragment.OnListFragmentInteractionListener, ProductInfoFragment.OnFragmentInteractionListener {

    private FrameLayout frameLayout;
    private CoordinatorLayout.LayoutParams params;
    public static int categoryId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Window w=getWindow();
        w.setStatusBarColor(Color.parseColor("#689B00"));
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Market");
        toolbar.setBackgroundColor(Color.parseColor("#689B00"));
        setSupportActionBar(toolbar);

        String store = getIntent().getStringExtra(Constants.STORE);
        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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

        MenuListProductFragment menuFragment = MenuListProductFragment.newInstance(1);
        getSupportFragmentManager().beginTransaction().replace(R.id.menuListInfoProduct, menuFragment, "menulist").commit();
        initStore(store);

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

        int id = item.getItemId();
        switch (id) {
            case R.id.allProducts: //TODO no category  might not need it !!
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
                break;
        }

        updateFragment(categoryId);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onListFragmentInteraction(Product product, int categoryId) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        ProductInfoFragment productInfoFragment = ProductInfoFragment.newInstance(product,categoryId); //else productid from this instance

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

    public void updateFragment(int categoryId){
        FragmentManager fragmentManager = getSupportFragmentManager();
        MenuListProductFragment fragment = MenuListProductFragment.newInstance(categoryId);

        if (fragmentManager != null) {
            fragmentManager.beginTransaction().replace(R.id.menuListInfoProduct, fragment).commit(); //updateFragment
        }
    }

  }
