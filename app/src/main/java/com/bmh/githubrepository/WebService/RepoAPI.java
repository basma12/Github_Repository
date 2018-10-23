package com.bmh.githubrepository.WebService;

import com.bmh.githubrepository.Model.AccessToken;
import com.bmh.githubrepository.Model.Repo;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface RepoAPI {

    @GET("/users/square/repos?")
    Call<List<Repo>> loadRepo(@Query("access_token") String access_token, @Query("per_page") int per_page, @Query("page") int page);

    @GET("/users/square/repos?")
    Call<List<Repo>> loadRepo(@Query("access_token") String access_token);

    @Headers("Accept: application/json")
    @POST("login/oauth/access_token")
    @FormUrlEncoded
    Call<AccessToken> getAccessToken(@Field("client_id") String clientId,
                                     @Field("client_secret") String clientSecret,
                                     @Field("code") String code);

}
