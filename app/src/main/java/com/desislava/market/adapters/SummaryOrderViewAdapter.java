package com.desislava.market.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.desislava.market.R;
import com.desislava.market.fragments.SummaryActivityFragment;
import com.desislava.market.activities.UserInfoActivity;
import com.desislava.market.beans.SummaryOrder;

import java.util.List;


public class SummaryOrderViewAdapter extends RecyclerView.Adapter<SummaryOrderViewAdapter.ViewHolder> {

    private final List<SummaryOrder> mValues;
    private final SummaryActivityFragment.SummaryFragmentListener mListener;

    public SummaryOrderViewAdapter(SummaryActivityFragment.SummaryFragmentListener listener) {
        mValues = UserInfoActivity.orders;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_summary, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.storeName.setText(holder.mItem.getStoreName().toUpperCase());
        holder.status.setText("Placed");
        holder.total.setText(holder.mItem.getTotal());
        holder.delivery.setText("3lv"); //TODO delivery price based on distance
        holder.address.setText(holder.mItem.getInfo().getFullAddress().toUpperCase());

        holder.mView.setOnClickListener((View v)-> {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.summaryFragment("");
                }
            });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final View mView;
        private final TextView storeName;
        private final TextView status;
        private final TextView total;
        private final TextView delivery;
        private final TextView address;
        private SummaryOrder mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            storeName = view.findViewById(R.id.txt_name);
            status = view.findViewById(R.id.txt_order);
            total = view.findViewById(R.id.txt_total_price);
            delivery = view.findViewById(R.id.txt_delivery);
            address = view.findViewById(R.id.txt_address);
        }

    }
}
