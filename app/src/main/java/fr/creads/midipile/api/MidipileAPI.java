package fr.creads.midipile.api;

import java.util.Map;

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


    @FormUrlEncoded
    @POST(Constants.URL_LOGINFB)
    public void postLoginFacebook(@Field("email") String email,
                          @Field("password") String password,
                          @Field("fid") String fid,
                          @Field("firstname") String firstname,
                          @Field("lastname") String lastname,
                          @Field("cgv") String cgv,
                          Callback<User> callback);

    @FormUrlEncoded
    @POST(Constants.URL_REGISTER)
    public void postRegister(
            @Field("firstname") String firstname,
            @Field("lastname") String lastname,
            @Field("email") String email,
            @Field("password") String password,
            @Field("cgv") String cgv,
            @Field("newsletter") String newsletter,
            Callback<User> callback);

    @FormUrlEncoded
    @POST(Constants.URL_FORGETPASSWORD)
    public void postForgetPassword(@Field("email") String email,
                          Callback<Map<String, String>> callback);



}
