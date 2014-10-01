package fr.creads.midipile.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import fr.creads.midipile.objects.Badge;
import fr.creads.midipile.objects.Deal;
import fr.creads.midipile.objects.Deals;
import fr.creads.midipile.objects.User;
import retrofit.Callback;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Author : Hugo Gresse
 * Date : 01/09/14
 */
public interface MidipileAPI {

    @GET(Constants.URL_DEALS)
    public void getLastDeals(Callback<Deals> callback);

    @GET(Constants.URL_DEALS + "?active=false")
    public void getWinnersDeals(Callback<Deals> callback);

    @GET(Constants.URL_BADGES)
    public void getBadges(Callback<ArrayList<Badge>> callback);

    @GET(Constants.URL_USER_ME)
    public void getLoggedUser(
            @Header("x-wsse") String xwsse,
            Callback<User> callback);

    @GET(Constants.URL_USER_ME_CHANCE)
    public void getChances(
            @Header("x-wsse") String xwsse,
            Callback<Map<String, String>> callback);

    @GET(Constants.URL_USER_ME_DEALS)
    public void getWhishlist(
            @Header("x-wsse") String xwsse,
            @Query("from") int from,
            @Query("length") int length,
            Callback<List<Deal>> callback);




    @FormUrlEncoded
    @POST(Constants.URL_LOGIN)
    public void postLogin(@Field("email") String email,
                          @Field("password") String password,
                          @Field("id_device") String id_device,
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
            @Field("pays") String pays,
            @Field("password") String password,
            Callback<User> callback);


    @FormUrlEncoded
    @POST(Constants.URL_FORGETPASSWORD)
    public void postForgetPassword(@Field("email") String email,
                          Callback<Map<String, String>> callback);

    @POST(Constants.URL_DEAL_PLAY)
    public void postPlayDeal(
            @Header("x-wsse") String xwsse,
            @Path("id") int id,
            Callback<Map<String, String>> callback);

    @FormUrlEncoded
    @POST(Constants.URL_USER_ME_BADGE)
    public void postBadge(
            @Header("x-wsse") String xwsse,
            @Field("badges") String badge,
            Callback<User> callback);



}
