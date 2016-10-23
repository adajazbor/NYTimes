package com.ada.nytimes.network;

import android.util.Log;

import com.ada.nytimes.network.dto.articleSearch.ArticleSearchParam;
import com.ada.nytimes.network.dto.articleSearch.ArticlesResponse;
import com.ada.nytimes.utils.Constants;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by ada on 10/22/16.
 */
public class NYTimesServiceImpl {

    Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
            .create();

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(Constants.NY_TIMES_SEARCH_ARTICLES_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build();


    public void getArticlesList(ArticleSearchParam articleSearchDTO, Callback<ArticlesResponse> callback) {

        NYTimesService service = retrofit.create(NYTimesService.class);
        Call<ArticlesResponse> call = service.getArticles(articleSearchDTO.getAsMap(), Constants.NY_TIMES_API_KEY);
        Log.d("URL", String.format("query = %s", call.request().url().query()));
        call.enqueue(callback);
    }
}
