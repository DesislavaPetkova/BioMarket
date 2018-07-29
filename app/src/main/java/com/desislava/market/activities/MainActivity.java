package com.desislava.market.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
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
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.desislava.market.R;
import com.desislava.market.beans.Category;
import com.desislava.market.beans.Product;
import com.desislava.market.database.helper.DBHelper;
import com.desislava.market.fragments.MenuListProductFragment;
import com.desislava.market.fragments.PriceChartFragment;
import com.desislava.market.fragments.ProductInfoFragment;
import com.desislava.market.server.communication.JSONResponse;
import com.desislava.market.utils.Constants;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.desislava.market.server.communication.ParseServerResponse.storeList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, MenuListProductFragment.OnListFragmentInteractionListener, ProductInfoFragment.ProductInfoListener,JSONResponse.UpdateAndInsert ,PriceChartFragment.OnFragmentInteractionListener{

    private FrameLayout frameLayout;
    public static DBHelper dbHelper;
    public static int categoryId = 0;
    public static String DB_VER_STORE="db-store-";
    public static int DB_LAST_VERSION=1;
    public static SharedPreferences sharedPref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initToolbar();
        String store = getIntent().getStringExtra(Constants.STORE);
        initStore(store);
        DB_VER_STORE+=store;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        frameLayout = findViewById(R.id.menuListInfoProduct);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener((View view )-> {
                Intent intent = new Intent(MainActivity
                        .this, ShoppingCartActivity.class);
                startActivity(intent);
        });

        MenuListProductFragment menuFragment = MenuListProductFragment.newInstance(1);
        getSupportFragmentManager().beginTransaction().replace(R.id.menuListInfoProduct, menuFragment, "menulist").commit();
        sharedPref=this.getPreferences(Context.MODE_PRIVATE);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View hView =  navigationView.getHeaderView(0);
        ImageView navImage= hView.findViewById(R.id.imageView);
        navImage.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.marko)); //TODO might set the chosen  store or category as a image!!!

        navigationView.setNavigationItemSelectedListener(this);
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
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
            case R.id.orders:
                categoryId=6;
                Intent in=new Intent(this, SummaryActivity.class);
                startActivity(in);
                return true;
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
        }

    }


    private void initStore(String store) {
        Log.d("MainActivity initStore ","Enter");
        try {
            JSONResponse json = new JSONResponse(this, store);
            json.execute();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void updateFragment(int categoryId){
        FragmentManager fragmentManager = getSupportFragmentManager();
        MenuListProductFragment fragment = MenuListProductFragment.newInstance(categoryId);

        if (fragmentManager != null) {
            fragmentManager.beginTransaction().replace(R.id.menuListInfoProduct, fragment).commit(); //updateFragment
        }
    }

    @Override
    public void updateAdapter() {
        updateFragment(1);
    }

    @Override
    public boolean insertUpdateDB() {
        DB_LAST_VERSION = sharedPref.getInt(DB_VER_STORE, 1);
        Log.i("insertUpdateDB", "*** DB_LAST_VERSION ***" + DB_LAST_VERSION);

        dbHelper = new DBHelper(getBaseContext(), DB_LAST_VERSION);
        if (dbHelper.isUpdateIsNeeded()) {
            Log.i("insertUpdateDB", "*** DB is updated ***");
            Date now = new Date();
            int size = storeList.get(0).getAllCategory().size();
            List<Category> allCategories = storeList.get(0).getAllCategory();
            List<Product> allProducts;
            if (size > 0) {
                for (Category cat : allCategories) {
                    allProducts = cat.getAllProducts();
                    for (Product pr : allProducts) {
                        dbHelper.insertValue(pr.getName().toLowerCase(), pr.getPrice(), new SimpleDateFormat("dd-MM", Locale.ITALY).format(now));
                    }

                }
            }
        } else {
            Log.e("insertUpdateDB", "DB is *** NOT *** updated");
        }
        return true;
    }


    private void initToolbar() {
        setContentView(R.layout.activity_main);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        Window w = getWindow();
        w.setStatusBarColor(Constants.GREEN_COLOR);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(Constants.MARKET);
        toolbar.setBackgroundColor(Constants.GREEN_COLOR);
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

    }

    @Override
    public void bntPriceClicked(String name) {

        FragmentManager fragmentManager = getSupportFragmentManager();
        PriceChartFragment chart = PriceChartFragment.newInstance(name);
        if (fragmentManager != null) {
            fragmentManager.beginTransaction().replace(R.id.menuListInfoProduct, chart).addToBackStack(null).commit();
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
        Log.i("Price chart","Fragment clicked");

    }
}
