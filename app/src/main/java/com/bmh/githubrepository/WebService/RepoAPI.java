package com.bmh.githubrepository.WebService;

import com.bmh.githubrepository.Model.Repo;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RepoAPI {

    @GET("/users/square/repos?")
    Call<List<Repo>> loadRepo(@Query("access_token") String access_token, @Query("per_page") int per_page, @Query("page") int page);

    @GET("/users/square/repos?")
    Call<List<Repo>> loadRepo(@Query("access_token") String access_token);

}
