package com.example.google_maps_marcadores_coninterfaz_e_informacion_personalizada.volley_request;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.LruCache;
import android.widget.ImageView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.example.google_maps_marcadores_coninterfaz_e_informacion_personalizada.R;

public class Request_volley_image {

    private Context context;
    private ImageView img_view;
    private String url;

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public ImageView getImg_view() {
        return img_view;
    }

    public void setImg_view(ImageView img_view) {
        this.img_view = img_view;
    }



    public Request_volley_image(Context context, ImageView img_view)
    {
        this.context=context;
        this.img_view=img_view;
    }

    public void set_image(String url)
    {
        RequestQueue colaPeticiones = Volley.newRequestQueue(getContext());
        ImageLoader imageLoader = new ImageLoader(colaPeticiones, new ImageLoader.ImageCache() {
            private final LruCache<String, Bitmap> cache = new LruCache<String,
                  Bitmap>(10);  //el numero indica el max de elemtos que queremos guardar en cache
            public void putBitmap(String url, Bitmap bitmap) {
                cache.put(url, bitmap);
            }
            public Bitmap getBitmap(String url) {
                return cache.get(url);
            }
        });

        imageLoader.get(url,
                ImageLoader.getImageListener(getImg_view(), R.drawable.baseline_error_24,
                        R.drawable.baseline_error_24));
    }



}
