package com.desislava.market.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.desislava.market.R;
import com.desislava.market.activities.ShoppingCartActivity;
import com.desislava.market.beans.Cart;
import com.desislava.market.beans.Product;
import com.desislava.market.fragments.PriceCartFragment.OnListFragmentInteractionListener;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a Cart items and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 */
public class CartPriceRecyclerViewAdapter extends RecyclerView.Adapter<CartPriceRecyclerViewAdapter.ViewHolder> {

    private  ArrayList<Cart> shopList;
    private final OnListFragmentInteractionListener mListener;

    public CartPriceRecyclerViewAdapter(ArrayList<Cart> shopList, OnListFragmentInteractionListener listener) { //TODO shopList is static no initialization
       this.shopList = shopList;
       this.mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cart_price_fragment, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        Cart current = shopList.get(position);
        holder.pr_name.setText(current.getProduct().getName());
        float productPrice = Float.parseFloat(current.getPrice());
        holder.pr_price.setText(String.valueOf(productPrice * (Float.parseFloat(current.getQuantity()))));

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(shopList.get(position));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return shopList.size();
    }

    public void removeItem(int position){
        ShoppingCartActivity.shoppingList.remove(position);
        notifyItemRemoved(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final View mView;
        private TextView pr_name;
        private TextView pr_price;
        private Product product;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            this.pr_name = view.findViewById(R.id.pr_name);
            this.pr_price = view.findViewById(R.id.pr_price);

        }

    }

}
