package com.desislava.market.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;

import com.anton46.stepsview.StepsView;
import com.desislava.market.R;
import com.desislava.market.beans.SummaryOrder;
import com.desislava.market.beans.UserInfo;
import com.desislava.market.fragments.AddressUserFragment;
import com.desislava.market.fragments.LocationFragment;
import com.desislava.market.fragments.ShopperInfoFragment;
import com.desislava.market.utils.Constants;

import java.util.ArrayList;
import java.util.List;

import static com.desislava.market.activities.ShoppingCartActivity.shoppingList;

public class UserInfoActivity extends AppCompatActivity implements ShopperInfoFragment.OnFragmentInteractionListener,
        AddressUserFragment.OnFragmentInteractionListener, LocationFragment.OnFragmentInteractionListener {

    //private static final String STATE = "state";
    private StepsView stepsView;
    UserInfo info = new UserInfo();
    private boolean isLocationChecked;
    private int position = 0;
    private FloatingActionButton add;
   public static List<SummaryOrder> orders = new ArrayList<>();
    private SummaryOrder order = new SummaryOrder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initToolbar();
        stepsView = findViewById(R.id.stepsView);
        String[] steps = new String[]{"User info", "Address", "Place order"};
        add = findViewById(R.id.add_new);
        add.setOnClickListener((View view) -> {
            Intent intent = new Intent(UserInfoActivity
                    .this, StartActivity.class);
            shoppingList.clear();
            startActivity(intent);

        });
        add.hide();
        stepsView.setLabels(steps)
                .setBarColorIndicator(getResources().getColor(R.color.material_blue_grey_800))
                .setProgressColorIndicator(getResources().getColor(R.color.orange))
                .setLabelColorIndicator(getResources().getColor(R.color.orange))
                .setCompletedPosition(position)
                .drawView();

        if (savedInstanceState == null) {
            ShopperInfoFragment shopperInfoFragment = ShopperInfoFragment.newInstance();
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_info, shopperInfoFragment, "shopper Info").commit();
        }
    }

    @Override
    public void userInfoInteraction(CharSequence email, CharSequence name, CharSequence user, CharSequence pass, CharSequence phone) {
        //TODO(DB existing users)
        info.setEmail("" + email);
        info.setName("" + name);
        info.setUsername("" + user);
        info.setPassword("" + pass);
        info.setPassword("" + phone);
        position = 1;
        AddressUserFragment addressUserFragment = AddressUserFragment.newInstance("", "");
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_info, addressUserFragment, "address info").commit();
        stepsView.setCompletedPosition(position).drawView();
        add.hide();

    }

    @Override
    public void addressInteraction(CharSequence user, CharSequence address, Object dist, Object city) {
        if (!isLocationChecked) {
            info.setAddress("" + address);
            info.setDistrict("" + dist);
            info.setCity("" + city);
        }
        position = 2;
        LocationFragment location = LocationFragment. newInstance(isLocationChecked, info);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_info, location, "location").commit();
        stepsView.setCompletedPosition(position).drawView();
        add.show();
    }


    @Override
    public void locationInteraction() {
        order.setInfo(info);
        orders.add(order);
        Intent intent = new Intent(UserInfoActivity
                .this, SummaryActivity.class);
        startActivity(intent);
    }

    @Override
    public void isChecked(boolean checked) {
        this.isLocationChecked = checked;
    }


    private void initToolbar() {
        setContentView(R.layout.activity_user_info);
        Window w = getWindow();
        w.setStatusBarColor(Constants.GREEN_COLOR);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(Constants.GREEN_COLOR);
        toolbar.setTitle(Constants.USERINFO);
        setSupportActionBar(toolbar);

    }

}
