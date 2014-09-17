package fr.creads.midipile.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import fr.creads.midipile.objects.Badge;
import fr.creads.midipile.objects.Deals;
import fr.creads.midipile.objects.User;
import retrofit.Callback;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.POST;
import retrofit.http.PUT;

/**
 * Author : Hugo Gresse
 * Date : 01/09/14
 */
public interface MidipileAPI {

    @GET(Constants.URL_LASTDEALS)
    public void getLastDeals(Callback<Deals> callback);

    @GET(Constants.URL_BADGES)
    public void getBadges(Callback<ArrayList<Badge>> callback);


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
                          @Field("referer") String referer,
                          @Field("id_device") String id_device,
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
            @Field("referer") String referer,
            @Field("id_device") String id_device,
            Callback<User> callback);


    @FormUrlEncoded
    @PUT(Constants.URL_USER_ME)
    public void putUser(
            @Header("x-wsse") String xwsse,
            @Field("firstname") String firstname,
            @Field("lastname") String lastname,
            @Field("email") String email,
            @Field("mobile") String mobile,
            @Field("rue") String rue,
            @Field("rue_bis") String rue_bis,
            @Field("code_postal") String code_postal,
            @Field("ville") String ville,
            @Field("password") String password,
            Callback<User> callback);


    @FormUrlEncoded
    @POST(Constants.URL_FORGETPASSWORD)
    public void postForgetPassword(@Field("email") String email,
                          Callback<Map<String, String>> callback);



}
