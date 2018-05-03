package com.desislava.market.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.desislava.market.adapters.ProductMenuRecyclerViewAdapter;
import com.desislava.market.R;
import com.desislava.market.beans.Product;

import static android.webkit.ConsoleMessage.MessageLevel.LOG;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class MenuListProductFragment extends Fragment {

    //private static final String ARG_COLUMN_COUNT = "column-count";
    private static final String PRODUCT_ID = "single_product_info-id";
    private int mColumnCount = 2;
    private int categoryId = 1;
    private OnListFragmentInteractionListener mListener;
    ProductMenuRecyclerViewAdapter adapter;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public MenuListProductFragment() {
    }

    public static MenuListProductFragment newInstance(int categoryId) {
        MenuListProductFragment fragment = new MenuListProductFragment();
        Bundle args = new Bundle();
        args.putInt(PRODUCT_ID, categoryId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            categoryId = getArguments().getInt(PRODUCT_ID);
            Log.i(getClass() + "onCreate", "categoryId: " + categoryId);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i("ON CREATE Fragment","FRAGMENT");
        View view = inflater.inflate(R.layout.fragment_product_menu_list, container, false);
        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            //if(adapter==null) {
                adapter = new ProductMenuRecyclerViewAdapter(categoryId, mListener);
               // Log.i("onCreateView ", "Set NEW ADAPTER since it is NULLLLLLLLLL *******");
            //}
            recyclerView.setAdapter(adapter);
        }
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
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
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(Product product,int categoryId); //,int category
    }

/*    public void dataChange() {
        if (adapter != null) {
            adapter.notifyDataSetChanged();
            Log.i("dataChange", "notifyDataSetChanged");
        }
    }*/
}
