package com.desislava.market.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.desislava.market.R;
import com.desislava.market.activities.ShoppingCartActivity;
import com.desislava.market.beans.Cart;
import com.desislava.market.beans.Product;
import com.desislava.market.utils.Util;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProductInfoFragment.ProductInfoListener} interface
 * to handle interaction events.
 * Use the {@link ProductInfoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProductInfoFragment extends Fragment {
    private static final String PRODUCT = "product";
    private static final String CATEGORY = "category";

    private Product infoProduct;

    private ProductInfoListener mListener;

    private ImageView single_img;

    private TextView productName;

    private TextView prInfo;

    private ElegantNumberButton quantity;

    private Spinner product_weight;

    private Button add_cart;

    private Button bnt_price;

    private int categoryId = 0;

    private ImageView coverImage;


    public ProductInfoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ProductInfoFragment.
     */
    public static ProductInfoFragment newInstance(Product product, int category) {
        ProductInfoFragment fragment = new ProductInfoFragment();
        Bundle args = new Bundle();
        args.putSerializable(PRODUCT, product);
        args.putInt(CATEGORY, category);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            infoProduct = (Product) getArguments().getSerializable(PRODUCT);
            categoryId = getArguments().getInt(CATEGORY) + 1;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_product_info, container, false);
        //init fields
        coverImage = getActivity().findViewById(R.id.coverImage);

        //ingle_img = view.findViewById(R.id.single_product_info_img);
        productName = view.findViewById(R.id.lbl_product);
        prInfo = view.findViewById(R.id.txt_info);
        quantity = view.findViewById(R.id.incdec);
        product_weight = view.findViewById(R.id.productWeight);
        bnt_price = view.findViewById(R.id.bnt_price);
        bnt_price.setOnClickListener((View v) -> {
            // PriceChartFragment price = PriceChartFragment.newInstance("", "");//todo
            if (mListener != null) {
                mListener.bntPriceClicked(infoProduct.getName());
            }
        });
        add_cart = view.findViewById(R.id.add_cart);
        add_cart.setOnClickListener((View v) -> {
            Cart newItem = new Cart(infoProduct, quantity.getNumber(), Util.getCategoryById(categoryId));
            ShoppingCartActivity.shoppingList.add(newItem);
            Toast.makeText(v.getContext(), "Added to cart. " + categoryId, Toast.LENGTH_SHORT).show();
            Log.i("Cart product object:", newItem.toString());

        });

        //set fields with values
        coverImage.setImageBitmap(infoProduct.getImage());
        productName.setText(infoProduct.getName());
        quantity.setNumber("1");
        quantity.setRange(1, 1000);
        prInfo.setText(infoProduct.getInfo());
        bnt_price.setText((infoProduct.getPrice() + " lv"));

        return view;
    }

/*    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }*/

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ProductInfoListener) {
            mListener = (ProductInfoListener) context;
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
    public interface ProductInfoListener {
        void bntPriceClicked(String prName);
    }
}
