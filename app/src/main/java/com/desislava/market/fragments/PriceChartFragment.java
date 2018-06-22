package com.desislava.market.fragments;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.desislava.market.R;
import com.desislava.market.database.helper.DBHelper;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PriceChartFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PriceChartFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PriceChartFragment extends Fragment {

    private static final String PRODUCT_NAME = "prName";
    private static final String DATABASE_PATH = "";

    private String prName;
    private LineChart chart;

    private OnFragmentInteractionListener mListener;

    public PriceChartFragment() {
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param name Product name.
     * @return A new instance of fragment PriceChartFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PriceChartFragment newInstance(String name) {
        PriceChartFragment fragment = new PriceChartFragment();
        Bundle args = new Bundle();
        args.putString(PRODUCT_NAME, name);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.prName = getArguments().getString(PRODUCT_NAME);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_price_chart, container, false);
        chart = view.findViewById(R.id.chart);
        DBHelper dbHelper = new DBHelper(getActivity().getApplicationContext());
        Log.i("DATA BASE OPEN", "NOW SELECT EXECUTED *********************");
        Map values = dbHelper.getAllPrices(prName);

        List<Entry> wrap=new ArrayList<>();

        values.forEach((key,tab)->wrap.add(new Entry(((Date)key).getTime(),(Float)tab)));
        LineDataSet dataSet = new LineDataSet(wrap, "bananaaaaaaa");
        dataSet.setColor(0xFFF449);
        dataSet.setValueTextColor(0x70db70);
        LineData lineData = new LineData(dataSet);
        chart.setData(lineData);
        chart.invalidate();

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
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
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
