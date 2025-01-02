package com.developerobaida.italytobanglalibro.api;

import com.developerobaida.italytobanglalibro.models.ModelCategory;
import com.developerobaida.italytobanglalibro.models.ResponseWrapper2;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiInterface {

    @FormUrlEncoded
    @POST("get_bd_to_italian.php")
    Call<ResponseWrapper2> getQuestions(@Field("category") String category);

    @GET("get_categories.php")
    Call<List<ModelCategory>> showAllCategories();

}