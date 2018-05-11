package com.desislava.market.activities;

import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.anton46.stepsview.StepsView;
import com.desislava.market.R;
import com.desislava.market.beans.UserInfo;
import com.desislava.market.fragments.AddressUserFragment;
import com.desislava.market.fragments.ShopperInfoFragment;

public class UserInfoActivity extends AppCompatActivity implements ShopperInfoFragment.OnFragmentInteractionListener,
                                                                    AddressUserFragment.OnFragmentInteractionListener {


    StepsView stepsView;

    UserInfo info = new UserInfo();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        Window w=getWindow();
        w.setStatusBarColor(Color.parseColor("#689B00"));
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(Color.parseColor("#689B00"));
        toolbar.setTitle("User info");
        setSupportActionBar(toolbar);
        //Button bntContinue = findViewById(R.id.bntContinue);
        stepsView = (StepsView) findViewById(R.id.stepsView);
        String[] steps = new String[]{"1", "2", "3"};
        stepsView.setLabels(steps)
                .setBarColorIndicator(getResources().getColor(R.color.material_blue_grey_800))
                .setProgressColorIndicator(getResources().getColor(R.color.orange))
                .setLabelColorIndicator(getResources().getColor(R.color.orange))
                .setCompletedPosition(0)
                .drawView();


        ShopperInfoFragment shopperInfoFragment = ShopperInfoFragment.newInstance("","");
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_info,shopperInfoFragment,"shopper Info").commit();

       /* bntContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddressUserFragment addressUserFragment = AddressUserFragment.newInstance("","");
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_info,addressUserFragment,"address info").commit();
                stepsView.setCompletedPosition(1).drawView();
                addressUserFragment.onButtonPressed("Button");
            }
        });*/
    }




    @Override
    public void onFragmentInteraction(CharSequence email, CharSequence name, CharSequence user, CharSequence pass, CharSequence phone) { //TODOmight create a bean for this
        info.setEmail(""+ email);
        info.setName(""+ name);
        info.setUsername(""+user);
        info.setPassword(""+pass);
        info.setPassword(""+phone);

        AddressUserFragment addressUserFragment = AddressUserFragment.newInstance("","");
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_info,addressUserFragment,"address info").commit();
        stepsView.setCompletedPosition(1).drawView();

    }

    @Override
    public void onFragmentInteraction(CharSequence user, CharSequence address, Object dist, Object city) {

        info.setAddress(""+address);
        info.setDistrict(""+dist);
        info.setCity(""+city);
        Log.e("FINALY .........",info.toString());

    }
}
