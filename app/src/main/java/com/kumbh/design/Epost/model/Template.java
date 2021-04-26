package com.kumbh.design.Epost.model;

import java.util.List;

public class Template{
	private int error;
	private List<ListTemplateItem> listTemplate;

	public int getError(){
		return error;
	}

	public List<ListTemplateItem> getListTemplate(){
		return listTemplate;
	}
}