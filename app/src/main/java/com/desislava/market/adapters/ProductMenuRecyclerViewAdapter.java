package com.desislava.market.adapters;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.desislava.market.R;
import com.desislava.market.beans.Product;
import com.desislava.market.beans.Store;
import com.desislava.market.fragments.MenuListProductFragment.OnListFragmentInteractionListener;
import com.desislava.market.dummy.DummyContent.DummyItem;
import com.desislava.market.server.communication.ImageASynchTask;
import com.desislava.market.server.communication.ParseServerResponse;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class ProductMenuRecyclerViewAdapter extends RecyclerView.Adapter<ProductMenuRecyclerViewAdapter.ViewHolder> {

    private final List<Store> storeContent;
    private final OnListFragmentInteractionListener mListener;
    private int categoryId;
    private ImageASynchTask aSynchTask=null;

    public ProductMenuRecyclerViewAdapter(int categoryId, OnListFragmentInteractionListener listener) {
        Log.d("ProductMenuRecyclerAdap", "ENTER");
        this.categoryId = categoryId - 1;
        storeContent = ParseServerResponse.storeList;
        mListener = listener;
        Log.d("ProductMenuRecyclerAdap", "LEAVE");
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_main_menu, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Log.i("onBindViewHolder", "" + position);
        holder.product = storeContent.get(0).getAllCategory().get(categoryId).getAllProducts().get(position);

        if(aSynchTask==null /*&& holder.mItem.getImage()==null*/) {  //TODO think of a way when activity is back to start (choosing store) to be able to download images again !!!!
            ImageASynchTask aSynchTask = new ImageASynchTask(holder);
            Log.i("before call aSynchImage", "");
            aSynchTask.execute(holder.product.getImageURL());
        }
        holder.price.setText(holder.product.getPrice());
        holder.imageView.setImageBitmap(holder.product.getImage());
        Log.e("onBindViewHolder","IS WORKINGGGGGGGGGGGGGGGGGGGGG***************************");
        /* holder.mContentView.setText(storeContent.get(position).content);*/

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.product);
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
        private final TextView price;
        private final ImageView imageView;

        private Product product;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            this.price = (TextView) view.findViewById(R.id.price_view);
            this.imageView = (ImageView) view.findViewById(R.id.product_image);
           /* mContentView = (TextView) view.findViewById(R.id.content);*/
        }

        @Override
        public String toString() {
            return product.toString();
        }
    }

}
