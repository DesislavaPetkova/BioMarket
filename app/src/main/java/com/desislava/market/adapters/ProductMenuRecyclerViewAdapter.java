package com.desislava.market.adapters;

import android.annotation.SuppressLint;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.desislava.market.R;
import com.desislava.market.activities.ShoppingCartActivity;
import com.desislava.market.beans.Cart;
import com.desislava.market.beans.Product;
import com.desislava.market.beans.Store;
import com.desislava.market.fragments.MenuListProductFragment.OnListFragmentInteractionListener;
import com.desislava.market.server.communication.ImageASynchTask;
import com.desislava.market.server.communication.ParseServerResponse;
import com.desislava.market.utils.Util;

import java.util.List;


public class ProductMenuRecyclerViewAdapter extends RecyclerView.Adapter<ProductMenuRecyclerViewAdapter.ViewHolder> {

    private final List<Store> storeContent;
    private final OnListFragmentInteractionListener mListener;
    private int categoryId;
    private ImageASynchTask aSynchTask=null;

    @SuppressLint("LongLogTag")
    public ProductMenuRecyclerViewAdapter(int categoryId, OnListFragmentInteractionListener listener) {
        Log.d(""+getClass(), "Constructor - ENTER");
        this.categoryId = categoryId - 1;
        storeContent = ParseServerResponse.storeList;
        mListener = listener;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_main_menu, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Log.i(getClass() + " onBindViewHolder", "" + position);
        holder.product = storeContent.get(0).getAllCategory().get(categoryId).getAllProducts().get(position);
        if (aSynchTask == null /*&& holder.mItem.getImage()==null*/) {  //TODO think of a way when activity is back to start (choosing store) to be able to download images again !!!!
            ImageASynchTask aSynchTask = new ImageASynchTask(holder);
            aSynchTask.execute(holder.product.getImageURL());
            Log.i(getClass() + " onBindViewHolder", "ImageASynchTask is null.. Downloading..");
        }
        holder.price.setText((holder.product.getPrice() + " lv"));
        holder.pr_name.setText(holder.product.getName());
        holder.imageView.setImageBitmap(holder.product.getImage());

        holder.cart_button.setOnClickListener((View v) -> {
            Cart singleProduct = new Cart(holder.product, "1", Util.getCategoryById(categoryId + 1));
            ShoppingCartActivity.shoppingList.add(singleProduct);
            Toast.makeText(v.getContext(), "Added to cart.Default quantity: 1", Toast.LENGTH_LONG).show();
        });

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.product, categoryId);
                }
            }
        });
    }



    @Override
    public int getItemCount() {
        if (storeContent.size() > 0) {
            return storeContent.get(0).getAllCategory().get(categoryId).getAllProducts().size();
        }
        return storeContent.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final View mView;
        public final TextView price;
        public final ImageView imageView;
        public final Button cart_button;
        public final TextView pr_name;
        public Product product;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            this.price = view.findViewById(R.id.price_view);
            this.imageView = view.findViewById(R.id.product_image);
            this.cart_button = view.findViewById(R.id.bnt_add_cart);
            this.pr_name=view.findViewById(R.id.productName);
        }

        /*@Override
        public String toString() {
            return product.toString();
        }*/
    }

}
