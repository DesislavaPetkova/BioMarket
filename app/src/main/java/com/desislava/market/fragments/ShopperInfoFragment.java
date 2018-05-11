package com.desislava.market.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.desislava.market.R;
import com.desislava.market.beans.UserInfo;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ShopperInfoFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class ShopperInfoFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private Button next;

    public ShopperInfoFragment() {
        // Required empty public constructor
    }

    public static ShopperInfoFragment newInstance(String param1, String param2) {
        ShopperInfoFragment fragment = new ShopperInfoFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            //TODO get argument from bundle
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_shopper_info, container, false);
        next = view.findViewById(R.id.bnt_next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView email=view.findViewById(R.id.txtMail);
                TextView full=view.findViewById(R.id.shopperName);
                TextView username=view.findViewById(R.id.txtUsername);
                TextView pass=view.findViewById(R.id.txtPassword);
                TextView phone=view.findViewById(R.id.txtPhone);
                //TODO check if null !!!!!!!

                if (mListener != null) {
                    mListener.onFragmentInteraction(email.getText(),full.getText(),username.getText(),pass.getText(),phone.getText());  //TODO can pass the entire bean ot fields to the activity
                }
            }
        });
        return view;
    }

   /* // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }*/

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

        void onFragmentInteraction(CharSequence text, CharSequence text1, CharSequence text2, CharSequence text3, CharSequence text4);
    }
}
