package com.desislava.market.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.desislava.market.R;
import com.desislava.market.beans.Cart;
import com.desislava.market.beans.Product;
import com.desislava.market.fragments.PriceCartFragment.OnListFragmentInteractionListener;
import com.desislava.market.dummy.DummyContent.DummyItem;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class CartPriceRecyclerViewAdapter extends RecyclerView.Adapter<CartPriceRecyclerViewAdapter.ViewHolder> {

   private  ArrayList<Cart> shopList;
    private final OnListFragmentInteractionListener mListener;

    public CartPriceRecyclerViewAdapter(ArrayList<Cart> shopList, OnListFragmentInteractionListener listener) {
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
      /*  holder.mItem = mValues.get(position);
        holder.mIdView.setText(mValues.get(position).id);
        holder.mContentView.setText(mValues.get(position).content);*/

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

    public class ViewHolder extends RecyclerView.ViewHolder {
        private  final View mView;
       /* private  final TextView mIdView;
        private  final TextView mContentView;*/
        private Product product;

        public ViewHolder(View view) {
            super(view);
            mView = view;
           /* mIdView = (TextView) view.findViewById(R.id.id);
            mContentView = (TextView) view.findViewById(R.id.content);*/
        }

       /* @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }*/
    }
}
