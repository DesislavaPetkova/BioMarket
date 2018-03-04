package com.desislava.market;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.anton46.stepsview.StepsView;
import com.desislava.market.fragments.AddressUserFragment;
import com.desislava.market.fragments.ShopperInfoFragment;

public class UserInfoActivity extends AppCompatActivity implements ShopperInfoFragment.OnFragmentInteractionListener,
                                                                    AddressUserFragment.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        Button bntContinue = findViewById(R.id.bntContinue);
        setSupportActionBar(toolbar);

        final StepsView stepsView = (StepsView) findViewById(R.id.stepsView);
        String[] steps = new String[]{"1", "2", "3"};
        stepsView.setLabels(steps)
                .setBarColorIndicator(getResources().getColor(R.color.material_blue_grey_800))
                .setProgressColorIndicator(getResources().getColor(R.color.orange))
                .setLabelColorIndicator(getResources().getColor(R.color.orange))
                .setCompletedPosition(0)
                .drawView();


        ShopperInfoFragment shopperInfoFragment =new ShopperInfoFragment();
        getSupportFragmentManager().
                beginTransaction().add(R.id.fragment_info,shopperInfoFragment,"shopper Info").commit();

        bntContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddressUserFragment addressUserFragment=new AddressUserFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_info,addressUserFragment,"address info").commit();
                stepsView.setCompletedPosition(1).drawView();
                addressUserFragment.onButtonPressed("Button");
            }
        });
    }


    @Override
    public void onFragmentInteraction(String view) {

        System.out.print("FUCK OUOUOUOUUU "+ view);

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
