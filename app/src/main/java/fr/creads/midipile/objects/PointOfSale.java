package fr.creads.midipile.objects;

import com.google.android.gms.maps.model.LatLng;

/**
 * Author : Hugo Gresse
 * Date : 05/09/14
 */
public class PointOfSale {

    private String intitule;
    private String adresse;
    private float longitude;
    private float latitude;

    public String getIntitule() {
        return intitule;
    }

    public String getAdresse() {
        return adresse;
    }

    public LatLng getLatLng() {
        return new LatLng(latitude, longitude);
    }

    @Override
    public String toString() {
        return "PointOfSale{" +
                "intitule='" + intitule + '\'' +
                ", adresse='" + adresse + '\'' +
                '}';
    }
}
