package com.kumbh.design.Epost.model;

public class ListPostItem{
	private String date;
	private String postId;
	private String catName;
	private String postName;
	private String postImagePath;
	private String cateId;

	public ListPostItem(String date, String postId, String catName, String postName, String postImagePath, String cateId) {
		this.date = date;
		this.postId = postId;
		this.catName = catName;
		this.postName = postName;
		this.postImagePath = postImagePath;
		this.cateId = cateId;
	}

	public String getDate(){
		return date;
	}

	public String getPostId(){
		return postId;
	}

	public String getCatName(){
		return catName;
	}

	public String getPostName(){
		return postName;
	}

	public String getPostImagePath(){
		return postImagePath;
	}

	public String getCateId(){
		return cateId;
	}
}
