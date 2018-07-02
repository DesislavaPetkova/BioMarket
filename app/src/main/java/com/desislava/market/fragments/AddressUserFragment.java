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
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import com.desislava.market.R;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AddressUserFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AddressUserFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddressUserFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private Button next;

    private Switch current;

    public AddressUserFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddressUserFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddressUserFragment newInstance(String param1, String param2) {
        AddressUserFragment fragment = new AddressUserFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_address_user, container, false);
        next = view.findViewById(R.id.bnt_place_order);
        final EditText name = view.findViewById(R.id.shipUser);
        final EditText addressShip = view.findViewById(R.id.txtAddress);
        final Spinner district = view.findViewById(R.id.spinnerDistrict);
        final Spinner city = view.findViewById(R.id.spinnerCity);
        next.setOnClickListener((View v)-> {
                if (mListener != null) {
                    mListener.addressInteraction(name.getText(), addressShip.getText(), district.getSelectedItem(), city.getSelectedItem());// TODO handle on city change -> district change also relocate them
                }
        });

        current = view.findViewById(R.id.isCurrent);
        current.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {


            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.i("onCheckedChanged  ", "" + isChecked);
                disableField(!isChecked);
                if (mListener != null) {
                    mListener.isChecked(isChecked);
                }
            }

            private void disableField(boolean isEnabled) {
                name.setEnabled(isEnabled);
                name.setFocusable(isEnabled);
                addressShip.setEnabled(isEnabled);
                addressShip.setFocusable(isEnabled);
                district.setEnabled(isEnabled);
                district.setFocusable(isEnabled);
                city.setEnabled(isEnabled);
                city.setFocusable(isEnabled);

            }

        });
        return view;
    }



   /* // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(String view) {
        if (mListener != null) {
            mListener.onFragmentInteraction(view);
        }
    }
*/
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

        void addressInteraction(CharSequence text, CharSequence text1, Object selectedItem, Object selectedItem1);

        void isChecked(boolean checked);
    }
}
