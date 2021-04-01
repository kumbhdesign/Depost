package com.kumbh.design.Epost.model;

public class ResponseItem{
	private UserData userData;
	private int error;
	private String message;

	public UserData getUserData(){
		return userData;
	}

	public int getError(){
		return error;
	}

	public String getMessage(){
		return message;
	}
}
