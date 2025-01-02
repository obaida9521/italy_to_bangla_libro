package com.developerobaida.italytobanglalibro.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiController {
   private static final String URL = "https://eyasinit.xyz/api/";
   private static ApiController controller;
   private static Retrofit retrofit;
   ApiController(){
      retrofit = new Retrofit.Builder().baseUrl(URL).addConverterFactory(GsonConverterFactory.create()).build();
   }
   public static synchronized ApiController getInstance(){
      if (controller ==null) controller = new ApiController();
      return controller;
   }
   public ApiInterface getApi(){
      return retrofit.create(ApiInterface.class);
   }
}
