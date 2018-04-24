package com.desislava.market.server.communication;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import com.desislava.market.adapters.ProductMenuRecyclerViewAdapter;
import java.io.InputStream;
import java.net.URL;

/**
 * Created by Desislava on 20-Apr-18.
 */

public class ImageASynchTask  extends AsyncTask<String, Integer, Bitmap> {
    ProductMenuRecyclerViewAdapter.ViewHolder holder;

    public ImageASynchTask(ProductMenuRecyclerViewAdapter.ViewHolder holder) {
    this.holder=holder;
    }

    @Override
    protected Bitmap doInBackground(String... strings) {
        Log.e("doinBackground...","Downloaddd>>>>>" +strings[0]);
        //String myurl="https://media.alienwarearena.com/media/tux-r.jpg";
        Bitmap bm=null;
        InputStream is=null;

        try
        {
            is=new URL(strings[0]).openStream();
            if(is!=null) {
                bm = BitmapFactory.decodeStream(is);
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        return bm;
         }




    @Override
    protected void onPostExecute(Bitmap bitmap) {
        if(holder.mItem!=null) {
            holder.mItem.setImage(bitmap);
        }
        holder.imageView.setImageBitmap(bitmap);
    }
}
