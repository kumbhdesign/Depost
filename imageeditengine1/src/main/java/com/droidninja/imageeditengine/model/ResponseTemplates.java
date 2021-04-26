package com.droidninja.imageeditengine.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class ResponseTemplates {

	@SerializedName("response")
	@Expose
	private ResponseData response;

	public ResponseData getResponse() {
		return response;
	}

	public void setResponse(ResponseData response) {
		this.response = response;
	}

}



