package com.example.google_maps_marcadores_coninterfaz_e_informacion_personalizada.volley_request;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import androidx.annotation.Nullable;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.google_maps_marcadores_coninterfaz_e_informacion_personalizada.messages.Messages;
import com.google.android.gms.maps.model.LatLng;

public class Request_volley {

    private Context context;
    private String response;

    private String url;
    private DeserializeJSON deserializeJSON;

    public Request_volley(Context context){
        this.context=context;
        this.response= Messages.STATUS_EMPTY;
        this.url="";

    }


    //getters y setters

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    //**********************

    public void getVolleyResponse()
    {
        if(this.url!="") {
            RequestQueue queue = Volley.newRequestQueue(getContext());
            StringRequest stringRequest = new StringRequest(Request.Method.GET, this.url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            setResponse(response);
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    setResponse(Messages.ERROR);
                }
            });
            queue.add(stringRequest);
        }
    }





    public void create_url_location_around(LatLng latLng,String radius,Integer pos){
        this.url="https://maps.googleapis.com/maps/api/place/nearbysearch/json?" +
                "&location="+latLng.latitude+","+latLng.longitude+"" +
                "&radius="+radius+"" +
                "&type="+Messages.types[pos]+"" +
                "&key="+Messages.SECRET_KEY_GOOGLE+"" +
                "\n";
        Log.i("estemensajeesprueba",Messages.types[pos]);
    }


}

