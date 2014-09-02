package fr.creads.midipile.api;

import fr.creads.midipile.objects.Deals;
import retrofit.Callback;
import retrofit.http.GET;

/**
 * Author : Hugo Gresse
 * Date : 01/09/14
 */
public interface MidipileAPI {

    @GET(Constants.URL_LASTDEALS)
    public void getLastDeals(Callback<Deals> callback);

}
