package com.desislava.market.adapters;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.desislava.market.activities.ShoppingCartActivity;
import com.desislava.market.beans.Cart;
import com.desislava.market.beans.Product;
import com.desislava.market.cart.helper.ShoppingCartHelper;
import com.desislava.market.fragments.CartFragment.OnListFragmentInteractionListener;
import com.desislava.market.R;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a list with items and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 */
public class MyCartRecyclerViewAdapter extends RecyclerView.Adapter<MyCartRecyclerViewAdapter.ViewHolder> {

    private final List<Cart> shoppingList;
    private final OnListFragmentInteractionListener mListener;

    public MyCartRecyclerViewAdapter(ArrayList<Cart> shopList,OnListFragmentInteractionListener listener) {
        this.shoppingList = shopList;
        this.mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cart_fragment, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        Cart cart = shoppingList.get(position);//TODO DO I NEED IT ?
        holder.pr_cart = cart;
        holder.imageView.setImageBitmap(cart.getProduct().getImage());
        holder.cartCategory.setText(cart.getCategory());
        holder.productName.setText(cart.getProduct().getName());
        holder.quantity.setText(cart.getQuantity());

        //holder.cartCategory;

        holder.mView.setOnClickListener((View v) -> {
            if (null != mListener) {
                // Notify the active callbacks interface (the activity, if the
                // fragment is attached to one) that an item has been selected.
                mListener.onListFragmentInteraction(shoppingList.get(position));
            }

        });
    }

    @Override
    public int getItemCount() {
        return shoppingList.size();
    }

    public void removeItem(int position){
        ShoppingCartActivity.shoppingList.remove(position);
        notifyItemRemoved(position);
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        private final View mView;
        private ImageView imageView;
        private TextView cartCategory;
        private TextView productName;
        private TextView quantity;

        Cart pr_cart;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            this.imageView = view.findViewById(R.id.img_cart_product);
            this.cartCategory = view.findViewById(R.id.cart_category);
            this.productName = view.findViewById(R.id.cart_pr_name);
            this.quantity = view.findViewById(R.id.cart_txt_quantity);

        }

    }

}
