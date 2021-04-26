package com.kumbh.design.Epost.model;

public class ListTemplateItem{
	private String postId;

	public ListTemplateItem(String postId, String templateTitle, String templateId, String templateDemoImagePath, String templateImagePath) {
		this.postId = postId;
		this.templateTitle = templateTitle;
		this.templateId = templateId;
		this.templateDemoImagePath = templateDemoImagePath;
		this.templateImagePath = templateImagePath;
	}

	private String templateTitle;
	private String templateId;
	private String templateDemoImagePath;
	private String templateImagePath;

	public String getPostId(){
		return postId;
	}

	public String getTemplateTitle(){
		return templateTitle;
	}

	public String getTemplateId(){
		return templateId;
	}

	public String getTemplateDemoImagePath(){
		return templateDemoImagePath;
	}

	public String getTemplateImagePath(){
		return templateImagePath;
	}
}
