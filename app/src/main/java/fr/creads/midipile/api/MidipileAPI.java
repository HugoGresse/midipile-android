package fr.creads.midipile.api;

import fr.creads.midipile.objects.Deals;
import fr.creads.midipile.objects.User;
import retrofit.Callback;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.POST;

/**
 * Author : Hugo Gresse
 * Date : 01/09/14
 */
public interface MidipileAPI {

    @GET(Constants.URL_LASTDEALS)
    public void getLastDeals(Callback<Deals> callback);

    @FormUrlEncoded
    @POST(Constants.URL_LOGIN)
    public void postLogin(@Field("email") String email,
                          @Field("password") String password,
                          Callback<User> callback);



}
