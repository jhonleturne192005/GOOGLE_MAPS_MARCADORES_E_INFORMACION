package com.example.google_maps_marcadores_coninterfaz_e_informacion_personalizada.volley_request;

import android.content.Context;
import android.util.Log;

import com.example.google_maps_marcadores_coninterfaz_e_informacion_personalizada.MainActivity;
import com.example.google_maps_marcadores_coninterfaz_e_informacion_personalizada.Models.Location_Around;
import com.example.google_maps_marcadores_coninterfaz_e_informacion_personalizada.messages.Messages;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DeserializeJSON {

    List<Location_Around> location_aroundList;
    JSONObject jsonObject;
    Context context;

    /* GETTER Y SETTERS  */

    public List<Location_Around> getLocation_aroundList() {
        return location_aroundList;
    }

    public void setLocation_aroundList(List<Location_Around> location_aroundList) {
        this.location_aroundList = location_aroundList;
    }

    public JSONObject getJsonObject() {
        return jsonObject;
    }

    public void setJsonObject(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    /* FIN GETTER Y SETTER */



    public DeserializeJSON(String jsoncad,Context context)
    {
        try {
            this.jsonObject = new JSONObject(jsoncad);
            this.context=context;
        }catch (Exception ex)
        {
            Log.i(Messages.ERROR,ex.getMessage());
        }
    }


    public void JSON_Alrededor() throws JSONException {
        location_aroundList=new ArrayList<>();
        Location_Around location_around;
        JSONArray jsonArray=jsonObject.getJSONArray("results");
        for(Integer x=0;x<jsonArray.length();x++)
        {
            location_around=new Location_Around();
            JSONObject jsonaux=jsonArray.getJSONObject(x);
            JSONObject geometry=jsonaux.getJSONObject("geometry");
            JSONObject location=geometry.getJSONObject("location");
            location_around.setLocation(
                    new LatLng(
                            location.getDouble("lat"),
                            location.getDouble("lng")
                    )
            );
            location_around.setName(jsonaux.getString("name"));
            location_around.setPlace_id(jsonaux.getString("place_id"));
            location_around.setReference(jsonaux.getString("reference"));
            JSONArray types=jsonaux.getJSONArray("types");
            List<String> lstTypes_temp=new ArrayList<>();
            for(Integer i=0;i<types.length();i++)
                lstTypes_temp.add(types.getString(i));
            location_around.setVicinity(jsonaux.getString("vicinity"));


            if(jsonaux.has("photos"))
            {
                JSONArray json_photo=jsonaux.getJSONArray("photos");
                if(json_photo.length()>0)
                    location_around.setImage_logo(
                            biuld_url_image(json_photo.getJSONObject(0).getString("photo_reference"))
                    );
            }
            location_around.setLogo_icon_aux(jsonaux.getString("icon"));
            location_aroundList.add(location_around);
        }
    }


    public void deserialize_json_detalle(String json,Integer pos) throws JSONException
    {
        JSONObject json_detalle=new JSONObject(json);
        JSONObject json_result=json_detalle.getJSONObject("result");
        if(json_result.has("current_opening_hours"))
            get_horarios(json_result.getJSONObject("current_opening_hours").getJSONArray("weekday_text"),pos);

        List<String> images_aux = new ArrayList<>();
        if(json_result.has("photos")) {
            JSONArray fotos = json_result.getJSONArray("photos");

            for (Integer i = 0; i < fotos.length(); i++) {

                String reference = biuld_url_image(fotos.getJSONObject(i).getString("photo_reference"));
                images_aux.add(reference);
            }
        }

        location_aroundList.get(pos).setImagenes(images_aux);
        if(json_result.has("formatted_phone_number"))
            location_aroundList.get(pos).setFormatted_phone_number(json_result.getString("formatted_phone_number"));
        location_aroundList.get(pos).setFormatted_address(json_result.getString("formatted_address"));
    }

    public void get_horarios(JSONArray horarios,Integer pos) throws JSONException {
        List<String> lst_horarios=new ArrayList<>();
        for(Integer x=0;x<horarios.length();x++)
            lst_horarios.add(horarios.getString(x));
        location_aroundList.get(pos).setHorarios(lst_horarios);
    }

    public String biuld_url_image(String reference)
    {
        String url="https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&" +
                "photo_reference="+reference+""+
                "&key="+Messages.SECRET_KEY_GOOGLE+"";
        return url;
    }

}
