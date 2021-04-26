package com.kumbh.design.Epost;

import com.kumbh.design.Epost.model.ResponseTemplate;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Url;

public interface FileUploadService {
    @Multipart
    @POST("profile-update/{id}")
    Call<ResponseBody> upload(@Path(value = "id", encoded = true)String appartId,
            @Part MultipartBody.Part company_logo,
            @Part("user_id") String userID,
            @Part("company_email") RequestBody  companyEmail,
            @Part("mobile_number") RequestBody  mobileNumber,
            @Part("website_url") RequestBody  websiteUrl,
            @Part("facebook_url") RequestBody  facebookUrl,
            @Part("instagram_url") RequestBody  instagramUrl,
            @Part("linkedin_url") RequestBody  linkedinUrl,
            @Part("company_logo_hidden") RequestBody logo_hidden,
            @Part("company_address") RequestBody company_address
    );

    @GET("post-template/{postID}/{templateId}") // specify the sub url for our base url
    public void postTemplates(@Path(value = "postID", encoded = true)String postId, @Path(value = "templateId", encoded = true)String templateID, Callback<ResponseTemplate> callback);
}
