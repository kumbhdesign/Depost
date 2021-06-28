package com.kumbh.design.Epost.model;

import com.google.gson.annotations.SerializedName;

public class PlansListItem{

	@SerializedName("plan_title")
	private String planTitle;

	@SerializedName("plan_price")
	private String planPrice;

	@SerializedName("created_at")
	private String createdAt;

	public PlansListItem(String planTitle, String planPrice, String planId, String planDuration, String planDescription, String planStatus) {
		this.planTitle = planTitle;
		this.planPrice = planPrice;
		this.planId = planId;
		this.planDuration = planDuration;
		this.planDescription = planDescription;
		this.planStatus = planStatus;
	}

	@SerializedName("sort_order")
	private String sortOrder;

	@SerializedName("plan_id")
	private String planId;

	@SerializedName("plan_duration")
	private String planDuration;

	@SerializedName("plan_description")
	private String planDescription;

	@SerializedName("plan_status")
	private String planStatus;

	public String getPlanTitle(){
		return planTitle;
	}

	public String getPlanPrice(){
		return planPrice;
	}

	public String getCreatedAt(){
		return createdAt;
	}

	public String getSortOrder(){
		return sortOrder;
	}

	public String getPlanId(){
		return planId;
	}

	public String getPlanDuration(){
		return planDuration;
	}

	public String getPlanDescription(){
		return planDescription;
	}

	public String getPlanStatus(){
		return planStatus;
	}
}