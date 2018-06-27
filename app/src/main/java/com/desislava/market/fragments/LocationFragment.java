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
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.desislava.market.R;
import com.desislava.market.beans.UserInfo;
import com.desislava.market.server.communication.GooglePlaceAsynchTask;
import com.desislava.market.server.communication.ParseServerResponse;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LocationFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link LocationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LocationFragment extends Fragment implements OnMapReadyCallback {

    private static final String CHECKED = "checked";
    private static final String USER_INFO = "userInfo";
    private static final String API_KEY = "AIzaSyAUSiW2rGjmWNcZBT-a7YLd8KJ5KtY4L8Q";
    private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?";
    private ArrayList<Place> places;
    private int radius = 1000;

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

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param isChecked Parameter 1.
     * @param user      Parameter 2.
     * @return A new instance of fragment LocationFragment.
     */
    // TODO: Rename and change types and number of parameters
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
            Log.i("onMapReady","User address");
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
        CameraPosition camera = CameraPosition.builder().target(loc).zoom(14).build();
        googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(camera));
        googleMap.addMarker(new MarkerOptions().position(loc).title("Address"));


    }

    private void storeSearch(double latitude, double longitude) {
        Log.i("storeSearch","Enter");
        StringBuilder request = new StringBuilder(PLACES_API_BASE);
        request.append("location=" + latitude + "," + longitude).append("&rankby=distance&").append("name=" + store).append("&key=" + API_KEY);
        Log.i("GOOGLE KEY GENERATED: ", request.toString());
        GooglePlaceAsynchTask search=new GooglePlaceAsynchTask();
        search.execute(request.toString());




        places = new ArrayList<>();
        HttpURLConnection http = null;
        StringBuilder jsonResults = new StringBuilder();

        try {
            URL url = new URL(request.toString());
            http = (HttpURLConnection) url.openConnection();
            InputStreamReader in = new InputStreamReader(http.getInputStream());
            int read;
            char[] buff = new char[1024];
            while ((read = in.read(buff)) != -1) {
                jsonResults.append(buff, 0, read);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (http != null) {
                http.disconnect();
            }
        }
        Log.i("FInally", "" + jsonResults.toString());

        // parameters.


    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
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
