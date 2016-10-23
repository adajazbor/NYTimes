package com.ada.nytimes.network;

import com.ada.nytimes.network.dto.articleSearch.ArticlesResponse;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

/**
 * Created by ada on 10/22/16.
 */
public interface NYTimesService {
    @GET("articlesearch.json")
    Call<ArticlesResponse> getArticles(@QueryMap Map<String, String> options, @Query("api") String apiKey);
}
