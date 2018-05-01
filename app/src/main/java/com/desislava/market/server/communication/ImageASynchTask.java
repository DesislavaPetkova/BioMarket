package com.desislava.market.server.communication;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import com.desislava.market.adapters.ProductMenuRecyclerViewAdapter;
import java.io.InputStream;
import java.net.URL;


public class ImageASynchTask  extends AsyncTask<String, Integer, Bitmap> {
    private ProductMenuRecyclerViewAdapter.ViewHolder holder;

    public ImageASynchTask(ProductMenuRecyclerViewAdapter.ViewHolder holder) {
    this.holder=holder;
    }

    @Override
    protected Bitmap doInBackground(String... strings) {
       // Log.e("doinBackground...", "Downloading..." + strings[0]);
        Bitmap bm = null;
        InputStream is;

        try {
            is = new URL(strings[0]).openStream();
            if (is != null) {
                bm = BitmapFactory.decodeStream(is);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return bm;
    }


    @Override
    protected void onPostExecute(Bitmap bitmap) {
        if (holder.product != null) {
            holder.product.setImage(bitmap);
        }
        holder.imageView.setImageBitmap(bitmap);
    }
}
