package com.kumbh.design.Epost.model;

import com.google.gson.annotations.SerializedName;

public class PremiumResponse{

	@SerializedName("response")
	private Response response;

	public Response getResponse(){
		return response;
	}
}