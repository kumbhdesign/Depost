package com.kumbh.design.Epost.model;

import com.google.gson.annotations.SerializedName;

public class Response{

	@SerializedName("plan")
	private Plan plan;

	public Plan getPlan(){
		return plan;
	}
}