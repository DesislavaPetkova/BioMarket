package com.desislava.market.fragments;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.desislava.market.R;
import com.desislava.market.activities.MainActivity;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;


public class PriceChartFragment extends Fragment {

    private static final String PRODUCT_NAME = "prName";
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
        Map values = MainActivity.dbHelper.getAllPrices(prName.toLowerCase());
        List<Entry> wrap = new ArrayList<>();

        values.forEach((key, tab) -> wrap.add(new Entry(((Long) key).floatValue(), (Float) tab)));
        Log.i("onCreateView", "wrap list size: " + wrap.size());

        LineDataSet dataSet = new LineDataSet(wrap, "Price chart");
        initChartView(dataSet, wrap.size());

        return view;
    }


    private void initChartView(@NonNull LineDataSet set, int size) {
        set.setColor(Color.RED);
        set.setDrawCircles(true);
        set.setDrawValues(false);
        set.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        chart.getLegend().setEnabled(false);
        LineData lineData = new LineData(set);
        chart.setData(lineData);

        YAxis rightAxis = chart.getAxisRight();
        rightAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        rightAxis.setAxisMinimum(0f);
        rightAxis.setLabelCount(4);
        rightAxis.setAxisMaximum(30f);

        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setAxisMinimum(0f);
        leftAxis.setLabelCount(4);
        leftAxis.setAxisMaximum(30f);

        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.TOP);
        xAxis.setDrawGridLines(false);
        xAxis.setLabelCount(size);
        xAxis.setGranularity(1f);

        xAxis.setValueFormatter((float value, AxisBase axis) -> {
            Date d = new Date(Float.valueOf(value).longValue());
            return new SimpleDateFormat("dd-MM", Locale.ITALY).format(d);
        });

        chart.invalidate();

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
