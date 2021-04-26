package com.droidninja.imageeditengine;

import com.droidninja.imageeditengine.model.ResponseTemplates;


import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

import retrofit2.http.GET;
import retrofit2.http.Path;

public interface FileUploadService {


    @GET("post-template/{postID}/{templateId}") // specify the sub url for our base url
    public Call<ResponseTemplates> postTemplates(@Path(value = "postID", encoded = true) String postId, @Path(value = "templateId", encoded = true) String templateID);
}
