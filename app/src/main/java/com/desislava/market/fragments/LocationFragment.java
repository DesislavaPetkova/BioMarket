package com.desislava.market.fragments;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.desislava.market.R;
import com.desislava.market.beans.GooglePlace;
import com.desislava.market.beans.UserInfo;
import com.desislava.market.server.communication.DirectionsAsyncTask;
import com.desislava.market.server.communication.DirectionsDataParser;
import com.desislava.market.server.communication.GooglePlaceAsyncTask;
import com.desislava.market.server.communication.ParseServerResponse;
import com.desislava.market.utils.Constants;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.PolyUtil;

import java.io.IOException;
import java.util.List;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;


public class LocationFragment extends Fragment implements OnMapReadyCallback, GooglePlaceAsyncTask.GooglePlace, DirectionsAsyncTask.DirectionsReady {

    private static final String CHECKED = "checked";
    public static final float KILOMETER = 0.40f;
    public static final float INITIAL_PRICE_DELIVERY= 2f;
    private static final String USER_INFO = "userInfo";
    private static final String API_KEY = "AIzaSyC3jG3rvjsW7R2c2kP6l_AeJEoJOBiO7NE";
    private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?";
    private LatLngBounds.Builder builder = new LatLngBounds.Builder();
    private String startAddr;
    private String endAddr;


    MapView mMapView;
    private GoogleMap googleMap;
    View mview;

    private boolean isChecked;
    private UserInfo userInfo;
    private LatLng loc;
    private LatLng dest;
    private TextView distance;
    private TextView duration;
    private TextView delivery;
    private TextView start;
    private TextView end;
    float pr;
    String store;

    private OnFragmentInteractionListener mListener;

    public LocationFragment() {
        // Required empty public constructor
    }

    public static LocationFragment newInstance(boolean isChecked, UserInfo user) {
        LocationFragment fragment = new LocationFragment();
        Bundle args = new Bundle();
        args.putBoolean(CHECKED, isChecked);
        args.putSerializable(USER_INFO, user);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.isChecked = getArguments().getBoolean(CHECKED);
            this.userInfo = (UserInfo) getArguments().getSerializable(USER_INFO);
            this.store = ParseServerResponse.storeList.get(0).getName();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mview = inflater.inflate(R.layout.fragment_location, container, false);
        Button finish = mview.findViewById(R.id.bnt_finish);
        finish.setOnClickListener((View view) -> mListener.locationInteraction(startAddr, endAddr,pr));
        distance = mview.findViewById(R.id.txtDistance);
        duration = mview.findViewById(R.id.txtDuration);
        delivery = mview.findViewById(R.id.txtDelivery);
        start = mview.findViewById(R.id.txtStart);
        end = mview.findViewById(R.id.txtEnd);
        return mview;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.locationInteraction(startAddr, endAddr,pr);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mMapView = mview.findViewById(R.id.mapView);
        if (mMapView != null) {
            mMapView.onCreate(savedInstanceState);
            mMapView.onResume();
            mMapView.getMapAsync(this);

        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.i("onMapReady", "Enter");
        MapsInitializer.initialize(getContext());
        Criteria criteria = new Criteria();
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        this.googleMap = googleMap;
        double latitude = 0;
        double longitude = 0;
        if (isChecked) {
            Log.i("onMapReady", "isChecked-TRUE");
            getCurrentLocation();

            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED /*&& ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED*/) {
                Log.e("REQUESTING", "LOCATION &&&&&&&&&&&&&&^^^^^^^^^^^^^^^^^^^^^^^");
                requestPermissions(new String[]{ACCESS_FINE_LOCATION /*ACCESS_COARSE_LOCATION*/}, 2);
            } else {
                LocationManager locationManager = (LocationManager)
                        getActivity().getSystemService(Context.LOCATION_SERVICE);
                Location location = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));
                if (location != null) {
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();
                    storeSearch(latitude, longitude);
                }
                //TODO check take lat and lang i get address as string and set it in user info
            }

        } else {
            Log.i("onMapReady", "User address");
            String chosenLocation = "Bulgaria," + userInfo.getCity() + "," + userInfo.getDistrict() + "," + userInfo.getAddress();
            Geocoder geocoder = new Geocoder(getContext());
            try {
                List<Address> list = geocoder.getFromLocationName(chosenLocation, 3);
                if (list.size() > 0) {
                    latitude = list.get(0).getLatitude();
                    longitude = list.get(0).getLongitude();
                    storeSearch(latitude, longitude);
                }
            } catch (IOException e) {
                Log.e("PROBLEM", " if the network is unavailable or any other I/O problem occurs");
            }
        }

    }

    private void storeSearch(double latitude, double longitude) {
        Log.i("storeSearch", "Enter");
        loc = new LatLng(latitude, longitude);
        builder.include(loc);
        googleMap.addMarker(new MarkerOptions().position(loc).title("Address"));
        updateCamera();
        StringBuilder request = new StringBuilder(PLACES_API_BASE);
        request.append("location=" + latitude + "," + longitude).append("&language=en&rankby=distance&").append("name=" + store).append("&key=" + API_KEY);
        Log.i("GOOGLE KEY GENERATED: ", request.toString());
        GooglePlaceAsyncTask search = new GooglePlaceAsyncTask(this);
        search.execute(request.toString());
    }

    @Override
    public void placeReady() {
        Log.i("placeReady", "LIST IS AVAILABLE ");
        GooglePlace place = ParseServerResponse.places.get(0);
        dest = new LatLng(place.getLat(), place.getLng());
        builder.include(dest);
        googleMap.addMarker(new MarkerOptions().position(dest).title(place.getVicinity()));
        updateCamera();
        Log.i("placeReady", "Before - DirectionsAsyncTask execute");
        DirectionsAsyncTask async = new DirectionsAsyncTask(this);
        async.execute(getDirectionsUrl());
    }

    private String getDirectionsUrl() {
        StringBuilder googleDirectionsUrl = new StringBuilder("https://maps.googleapis.com/maps/api/directions/json?");
        googleDirectionsUrl.append("origin=" + loc.latitude + "," + loc.longitude);
        googleDirectionsUrl.append("&destination=" + dest.latitude + "," + dest.longitude);
        googleDirectionsUrl.append("&key=" + API_KEY);
        Log.i("getDirectionsUrl  ", googleDirectionsUrl.toString());
        return googleDirectionsUrl.toString();
    }

    private void updateCamera() {
        LatLngBounds bounds = builder.build();
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 5);
        googleMap.moveCamera(cu);
        googleMap.animateCamera(cu);
    }

    @Override
    public void directionReady(String str) {
        Log.i("directionReady", "Enter");

        DirectionsDataParser parse = new DirectionsDataParser();
        String[] directionsList = parse.parseDirections(str);

        for (String s : directionsList) {
            PolylineOptions options = new PolylineOptions();
            options.color(Color.RED);
            options.width(10);
            options.addAll(PolyUtil.decode(s));
            googleMap.addPolyline(options);
        }

        distance.setText(parse.getGoogleDirectionsMap().get(Constants.DISTANCE));
        duration.setText(parse.getGoogleDirectionsMap().get(Constants.DURATION));
        pr=Float.parseFloat(parse.getGoogleDirectionsMap().get(Constants.DISTANCE).replace(" km",""));
        delivery.setText(String.format("%.1f", (pr * KILOMETER+INITIAL_PRICE_DELIVERY))+"lv");

        startAddr = parse.getAddr()[0];
        endAddr = parse.getAddr()[1];
        start.setText(startAddr);
        end.setText(endAddr);

    }


    public interface OnFragmentInteractionListener {
        void locationInteraction(String startAddr, String endAddr,float price);
    }

    private void getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(getContext(), ACCESS_FINE_LOCATION) != PERMISSION_GRANTED /*&& ActivityCompat.checkSelfPermission(getContext(), ACCESS_COARSE_LOCATION) != PERMISSION_GRANTED*/) {
            requestPermissions(new String[]{ACCESS_FINE_LOCATION/*, ACCESS_COARSE_LOCATION*/}, 1);
        } else {
            googleMap.setMyLocationEnabled(true);
            googleMap.getUiSettings().setMyLocationButtonEnabled(true);
            googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        }

    }

}
