package com.example.nutripal.Listeners;

import com.example.nutripal.Models.RandomRecipeAPIResponse;
//Listener for the API response
public interface ComplexSearchResponseListener {
    void didFetch(RandomRecipeAPIResponse response, String message);
    void didError(String message);

}
