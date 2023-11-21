package com.example.nutripal;

import android.content.Context;


import com.example.nutripal.Listeners.ComplexSearchResponseListener;
import com.example.nutripal.Models.RandomRecipeAPIResponse;
import com.example.nutripal.Models.Recipe;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public class RequestManager {
    Context context;
    Retrofit retrofit = new Retrofit.Builder().baseUrl("https://api.spoonacular.com/").addConverterFactory(GsonConverterFactory.create()).build();
    public RequestManager(Context context) {
        this.context = context;
    }

//    public void getComplexSearch(ComplexSearchResponseListener listener){
//        CallComplexSearch callComplexSearch = retrofit.create(CallComplexSearch.class);
//        Call<RandomRecipeAPIResponse> call = callComplexSearch.autocompleteRecipes(context.getString(R.string.api_key), "10");
//        call.enqueue(new Callback<RandomRecipeAPIResponse>() {
//            @Override
//            public void onResponse(Call<RandomRecipeAPIResponse> call, Response<RandomRecipeAPIResponse> response) {
//                if (!response.isSuccessful()){
//                    listener.didError(response.message());
//                    return;
//                }
//                listener.didFetch(response.body(), response.message());
//            }
//
//            @Override
//            public void onFailure(Call<RandomRecipeAPIResponse> call, Throwable t) {
//                listener.didError(t.getMessage());
//            }
//        });
//    }
    public interface CallComplexSearch{
        @GET("recipes/complexSearch")
        Call<List<Recipe>> autocompleteRecipes(@Query("query") String query, @Query("apiKey") String apiKey);
    }



}
