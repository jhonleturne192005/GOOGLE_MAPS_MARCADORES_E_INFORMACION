package com.example.google_maps_marcadores_coninterfaz_e_informacion_personalizada;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

import com.example.google_maps_marcadores_coninterfaz_e_informacion_personalizada.Models.Location_Around;
import com.example.google_maps_marcadores_coninterfaz_e_informacion_personalizada.info_window.Info_windows;
import com.example.google_maps_marcadores_coninterfaz_e_informacion_personalizada.messages.Messages;
import com.example.google_maps_marcadores_coninterfaz_e_informacion_personalizada.volley_request.DeserializeJSON;
import com.example.google_maps_marcadores_coninterfaz_e_informacion_personalizada.volley_request.Request_volley;
import com.example.google_maps_marcadores_coninterfaz_e_informacion_personalizada.volley_request.Request_volley_image;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback,
                                                                GoogleMap.OnMapClickListener {

    GoogleMap mapa;
    View view;

    ProgressBar progressBar;
    Marker principal;
    final Integer RADIO=300;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        reference_map();
        view= LayoutInflater.from(this).inflate(R.layout.info_window, null);
    }

    private void reference_map() {
        progressBar=findViewById(R.id.carga);
        progressBar.setVisibility(View.GONE);
        SupportMapFragment mapFragment = (SupportMapFragment)
                getSupportFragmentManager()
                        .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mapa = googleMap;
        mapa.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        mapa.getUiSettings().setZoomControlsEnabled(true);
        mapa.setOnMapClickListener(this);
        mapa.setInfoWindowAdapter(new Info_windows(MainActivity.this));

        //posicion por defecto
        LatLng pos_UTEQ = new LatLng(-1.012487, -79.46953209871774);
        CameraPosition camPos = new CameraPosition.Builder()
                .target(pos_UTEQ)
                .zoom(18)
                .build();
        CameraUpdate camUpd3 = CameraUpdateFactory.newCameraPosition(camPos);
        mapa.animateCamera(camUpd3);

    }


    @Override
    public void onMapClick(@NonNull LatLng latLng) {
        try {
            mapa.clear();
            Log.i(Messages.MENSAJE_LOG, "Ha presiona sobre el mapa");

            /*mapa.addMarker(new MarkerOptions()
                    .position(latLng)
                    .title("Marcador 2")
                    .snippet("Info Window para Marcador 2"));*/

            mapa.addCircle(new CircleOptions().center(latLng)
                    .radius(RADIO)
                    .strokeColor(Color.RED)
                    .fillColor(0x8000FFFF));

            Thread_response(latLng);

        }catch (Exception ex){
            Log.i("dsfsdfsdf",ex.getMessage());
        }

    }

    private void Thread_response(LatLng latLng)
    {
        //principal.remove();
        progressBar.setVisibility(View.VISIBLE);
        Thread thread_response=new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    Request_volley request_volley=new Request_volley(MainActivity.this);
                    for(int x=0;x<Messages.types.length;x++)
                    {
                        request_volley.create_url_location_around(latLng,RADIO.toString(),x);
                        request_volley.getVolleyResponse();
                        while (request_volley.getResponse() == Messages.STATUS_EMPTY) {
                            Log.i(Messages.MENSAJE_LOG, "Getting data from " + request_volley.getUrl());
                            Thread.sleep(500);
                        }
                        if (request_volley.getResponse() != Messages.ERROR)
                        {
                            Log.i(Messages.MENSAJE_LOG, "Datos correctamente cargados");
                            Log.i(Messages.MENSAJE_LOG, request_volley.getResponse());
                            DeserializeJSON deserializeJSON = new DeserializeJSON(request_volley.getResponse(),  MainActivity.this);
                            deserializeJSON.JSON_Alrededor();

                            for(Integer j=0;j<deserializeJSON.getLocation_aroundList().size();j++)
                            {
                                Request_volley request_volley_det=new Request_volley(MainActivity.this);
                                //Va Cambiando el link para buscar las referencias del lugar
                                request_volley_det.setUrl(
                                        "https://maps.googleapis.com/maps/api/place/details/json?" +
                                                "place_id="+deserializeJSON.getLocation_aroundList().get(j).getReference()+"" +
                                                "&key="+Messages.SECRET_KEY_GOOGLE+""
                                );
                                request_volley_det.getVolleyResponse();
                                while (request_volley_det.getResponse() == Messages.STATUS_EMPTY) {
                                    Log.i(Messages.MENSAJE_LOG, "Getting data from " + request_volley_det.getUrl());
                                    Thread.sleep(500);
                                }

                                deserializeJSON.deserialize_json_detalle(request_volley_det.getResponse(),j);
                            }

                            runOnUiThread(new Runnable() {  //actualiza el hilo principal
                                @Override
                                public void run() {
                                    for (Location_Around la : deserializeJSON.getLocation_aroundList()) {


                                      Marker ma= mapa.addMarker(new
                                                MarkerOptions().position(la.getLocation())
                                        );
                                      ma.setTag(init_values(la));
                                      ma.setIcon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher));
                                    }
                                }
                            });
                        }
                        if(x==Messages.types.length-1) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    progressBar.setVisibility(View.GONE);
                                }
                            });
                        }
                    }
                }catch (Exception ex){
                    Log.i(Messages.ERROR,"Ha ocurrido un error: "+ex.getMessage());
                }
            }
        });
        thread_response.start();
    }


    private View init_values(Location_Around lo) {

        view = LayoutInflater.from(this).inflate(R.layout.info_window, null);
        try {
            TextView name = view.findViewById(R.id.nombre_lugar);
            TextView ubicacion = view.findViewById(R.id.ubicacion);
            TextView horarios = view.findViewById(R.id.horarios);
            TextView phone_number = view.findViewById(R.id.telefono);
            TextView direccion = view.findViewById(R.id.direccion);

            ImageView img_view = view.findViewById(R.id.info_window_imagen);

            name.setText(lo.getName());
            ubicacion.setText("Coordenadas: \nLatitud: " + lo.getLocation().latitude + "\n" + "Longitud: " + lo.getLocation().longitude);

            Request_volley_image request_volley_image=new Request_volley_image(this,img_view);
            if(lo.getImage_logo()==null) {
                if(lo.getImagenes().size()>0)
                    request_volley_image.set_image(lo.getImagenes().get(0));
                else
                    request_volley_image.set_image(lo.getLogo_icon_aux());
            }else request_volley_image.set_image(lo.getImage_logo());

            if(lo.getHorarios()!=null) {
                String horarios_aux = "\nHorarios: \n";
                for (Integer x = 0; x < lo.getHorarios().size(); x++)
                    horarios_aux += lo.getHorarios().get(x) + "\n";
                horarios.setText(horarios_aux);
            }

            if(lo.getFormatted_phone_number()!=null)
                phone_number.setText("Telefono: "+lo.getFormatted_phone_number());
            direccion.setText("Dirección: "+lo.getFormatted_address());
            return view;
        }
        catch (Exception ex){
            Log.i(Messages.ERROR,ex.getMessage());
        }
        return view;
    }

    //primero llama a getInfoWindow y si la respuesta es nula llama a getInfoContents
    //https://developers.google.com/maps/documentation/android-sdk/infowindows?hl=es-419
    //https://medium.com/@nenad.strbic/how-to-create-your-own-custom-info-window-adapter-for-google-maps-e9d719e3e0f

}