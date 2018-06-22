package com.desislava.market.activities;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
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
    private CoordinatorLayout.LayoutParams params;
    private DBHelper dbHelper;
    public static int categoryId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initToolbar();
        String store = getIntent().getStringExtra(Constants.STORE);
        initStore(store);
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

       /* params = (CoordinatorLayout.LayoutParams) frameLayout.getLayoutParams();
        params.setMargins(0, 300, 0, 0);*/
        //frameLayout.setLayoutParams(params);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View hView =  navigationView.getHeaderView(0);
        ImageView navImage= hView.findViewById(R.id.imageView);
        navImage.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.kaufland)); //TODO might set the chosen  store or category as a image!!!

        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
          /*  params.setMargins(0, 300, 0, 0);
            frameLayout.setLayoutParams(params);*/
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
           /* params.setMargins(0, 200, 0, 0);
            frameLayout.setLayoutParams(params);*/
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

/*    @Override
    public void onFragmentInteraction(Product product) {
        Log.i(getLocalClassName() + "onFragmentInteraction", "ENTER");
    }*/

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

        dbHelper = new DBHelper(getApplicationContext());
        if(dbHelper.isUpdateIsNeeded()){
        Date now=new Date();
        int size = storeList.get(0).getAllCategory().size();
        List<Category> allCategories = storeList.get(0).getAllCategory();
        List<Product> allProducts;
        if (size > 0) {
            for (Category cat : allCategories) {
                allProducts = cat.getAllProducts();
                for (Product pr : allProducts) {
                    dbHelper.insertValue(pr.getName(), pr.getPrice(), new SimpleDateFormat("yyyy-MM-dd", Locale.ITALY).format(now));
                }

            }
        }
        }else{
            Log.e("NO NEeeeDDDDD","to UpDATEEEEEEEEEEEEE &&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");
        }
        return true;
    }


    private void initToolbar() {
        setContentView(R.layout.activity_main);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        Window w = getWindow();
        w.setStatusBarColor(Color.parseColor(Constants.COLOR));
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(Constants.MARKET);
        toolbar.setBackgroundColor(Color.parseColor(Constants.COLOR));
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

    }
}
