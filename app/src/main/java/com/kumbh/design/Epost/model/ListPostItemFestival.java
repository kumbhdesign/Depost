package com.kumbh.design.Epost.model;

public class ListPostItemFestival{
	private String backgroundImgPath;
	private String backgroundId;

	public ListPostItemFestival(String backgroundImgPath, String backgroundId) {
		this.backgroundImgPath = backgroundImgPath;
		this.backgroundId = backgroundId;
	}

	public String getBackgroundImgPath(){
		return backgroundImgPath;
	}

	public String getBackgroundId(){
		return backgroundId;
	}
}
