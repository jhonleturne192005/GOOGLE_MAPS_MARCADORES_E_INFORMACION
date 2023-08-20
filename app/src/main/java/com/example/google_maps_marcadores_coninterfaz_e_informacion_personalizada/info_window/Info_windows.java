package com.example.google_maps_marcadores_coninterfaz_e_informacion_personalizada.info_window;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

//import com.bumptech.glide.Glide;
import com.example.google_maps_marcadores_coninterfaz_e_informacion_personalizada.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

public class Info_windows implements GoogleMap.InfoWindowAdapter{

    View view;
    Context context;

    public Info_windows(Context context)
    {
        this.context=context;
        view= LayoutInflater.from(context).inflate(R.layout.info_window, null);
    }

    public Info_windows()
    {}

    @Nullable
    @Override
    public View getInfoContents(@NonNull Marker marker) {
        //View view1=(View) marker.getTag();
        return view;
    }

    @Nullable
    @Override
    public View getInfoWindow(@NonNull Marker marker) {
        View view1=(View) marker.getTag();
        return view1;
    }

}
