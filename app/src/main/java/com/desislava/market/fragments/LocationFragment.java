package com.desislava.market.fragments;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
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

import com.desislava.market.R;
import com.desislava.market.beans.GooglePlace;
import com.desislava.market.beans.UserInfo;
import com.desislava.market.server.communication.GooglePlaceAsyncTask;
import com.desislava.market.server.communication.JSONResponse;
import com.desislava.market.server.communication.ParseServerResponse;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;


public class LocationFragment extends Fragment implements OnMapReadyCallback, GooglePlaceAsyncTask.GooglePlace {

    private static final String CHECKED = "checked";
    private static final String USER_INFO = "userInfo";
    private static final String API_KEY = "AIzaSyCM8c1DKFJygv1Jl1czSVf6PdowVXL6AMY";//AIzaSyAUSiW2rGjmWNcZBT-a7YLd8KJ5KtY4L8Q;  //AIzaSyCM8c1DKFJygv1Jl1czSVf6PdowVXL6AMY
    private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?";
    private LatLngBounds.Builder builder = new LatLngBounds.Builder();
    private static final String[] LOCATION_PERMS = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };

    MapView mMapView;
    private GoogleMap googleMap;
    View mview;

    private boolean isChecked;
    private UserInfo userInfo;
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
        return mview;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.locationInteraction(uri);
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
        Log.i("onMapReady","Enter");
        MapsInitializer.initialize(getContext());
        Criteria criteria = new Criteria();
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        this.googleMap = googleMap;
        double latitude = 0;
        double longitude = 0;
        if (isChecked) {
            Log.i("onMapReady","isChecked-TRUE");
            getCurrentLocation();
            LocationManager locationManager = (LocationManager)
                    getActivity().getSystemService(Context.LOCATION_SERVICE);

            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED /*&& ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED*/) {
                requestPermissions(new String[]{ACCESS_FINE_LOCATION /*ACCESS_COARSE_LOCATION*/}, 2);
            }
            Location location = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));
            if (location != null) {
                latitude = location.getLatitude();
                longitude = location.getLongitude();
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
                }
            } catch (IOException e) {
                Log.e("PROBLEM", " if the network is unavailable or any other I/O problem occurs");
            }
        }

        storeSearch(latitude, longitude);

        LatLng loc = new LatLng(latitude, longitude);
        // CameraPosition camera = CameraPosition.builder().target(loc).zoom(10).build();
        //googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(camera));
        googleMap.addMarker(new MarkerOptions().position(loc).title("Address"));
        updateCamera(loc);

    }

    private void storeSearch(double latitude, double longitude) {
        Log.i("storeSearch", "Enter");
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
        LatLng loc = new LatLng(place.getLat(), place.getLng());
        googleMap.addMarker(new MarkerOptions().position(loc).title(place.getVicinity()));
        updateCamera(loc);
    }


    private void updateCamera(LatLng loc) {

        builder.include(loc);
        LatLngBounds bounds = builder.build();
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 0);
        googleMap.moveCamera(cu);
        googleMap.animateCamera(cu);
    }


    public interface OnFragmentInteractionListener {
        void locationInteraction(Uri uri);
    }

 /*   @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==1 &&grantResults[0]==PERMISSION_GRANTED){
            //googleMap.setMyLocationEnabled(true);
            googleMap.getUiSettings().setMyLocationButtonEnabled(true);
        }

    }*/

    private void getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(getContext(), ACCESS_FINE_LOCATION) != PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), ACCESS_COARSE_LOCATION) != PERMISSION_GRANTED) {
            requestPermissions(new String[]{ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION}, 1);
        } else {
            googleMap.setMyLocationEnabled(true);
            googleMap.getUiSettings().setMyLocationButtonEnabled(true);
            googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        }

    }

}
