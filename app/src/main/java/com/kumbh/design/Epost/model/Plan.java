package com.kumbh.design.Epost.model;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class Plan{

	@SerializedName("plans_list")
	private List<PlansListItem> plansList;

	@SerializedName("error")
	private int error;

	public List<PlansListItem> getPlansList(){
		return plansList;
	}

	public int getError(){
		return error;
	}
}