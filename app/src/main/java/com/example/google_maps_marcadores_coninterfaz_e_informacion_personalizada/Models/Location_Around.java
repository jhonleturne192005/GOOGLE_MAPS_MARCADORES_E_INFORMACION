package com.example.google_maps_marcadores_coninterfaz_e_informacion_personalizada.Models;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

public class Location_Around {

    private LatLng location;
    private String name;
    private String place_id;
    private String reference;
    private List<String> types;
    private String vicinity;

    private String image_logo;


    private List<String> imagenes;
    private List<String> horarios;

    private String formatted_address;
    private String formatted_phone_number;

    private String logo_icon_aux;


    public String getLogo_icon_aux() {
        return logo_icon_aux;
    }

    public void setLogo_icon_aux(String logo_icon_aux) {
        this.logo_icon_aux = logo_icon_aux;
    }

    public String getFormatted_address() {
        return formatted_address;
    }

    public void setFormatted_address(String formatted_address) {
        this.formatted_address = formatted_address;
    }

    public String getFormatted_phone_number() {
        return formatted_phone_number;
    }

    public void setFormatted_phone_number(String formatted_phone_number) {
        this.formatted_phone_number = formatted_phone_number;
    }

    public List<String> getImagenes() {
        return imagenes;
    }

    public void setImagenes(List<String> imagenes) {
        this.imagenes = imagenes;
    }

    public List<String> getHorarios() {
        return horarios;
    }

    public void setHorarios(List<String> horarios) {
        this.horarios = horarios;
    }

    public String getImage_logo() {
        return image_logo;
    }

    public void setImage_logo(String image_logo) {
        this.image_logo = image_logo;
    }


    public LatLng getLocation() {
        return location;
    }

    public void setLocation(LatLng location) {
        this.location = location;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPlace_id() {
        return place_id;
    }

    public void setPlace_id(String place_id) {
        this.place_id = place_id;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public List<String> getTypes() {
        return types;
    }

    public void setTypes(List<String> types) {
        this.types = types;
    }

    public String getVicinity() {
        return vicinity;
    }

    public void setVicinity(String vicinity) {
        this.vicinity = vicinity;
    }
}
