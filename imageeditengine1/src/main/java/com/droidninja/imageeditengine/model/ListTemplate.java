package com.droidninja.imageeditengine.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ListTemplate {

	@SerializedName("template_id")
	@Expose
	private String templateId;
	@SerializedName("post_id")
	@Expose
	private String postId;
	@SerializedName("template_tagline")
	@Expose
	private String templateTagline;
	@SerializedName("template_title")
	@Expose
	private String templateTitle;
	@SerializedName("template_demo_image")
	@Expose
	private String templateDemoImage;
	@SerializedName("template_image")
	@Expose
	private String templateImage;
	@SerializedName("template_theme_color")
	@Expose
	private String templateThemeColor;
	@SerializedName("template_font_family")
	@Expose
	private String templateFontFamily;
	@SerializedName("template_logo_top_padding")
	@Expose
	private String templateLogoTopPadding;
	@SerializedName("template_logo_left_padding")
	@Expose
	private String templateLogoLeftPadding;
	@SerializedName("template_tagline_top_padding")
	@Expose
	private String templateTaglineTopPadding;
	@SerializedName("template_tagline_left_padding")
	@Expose
	private String templateTaglineLeftPadding;
	@SerializedName("template_email_top_padding")
	@Expose
	private String templateEmailTopPadding;
	@SerializedName("template_email_left_padding")
	@Expose
	private String templateEmailLeftPadding;
	@SerializedName("template_mobile_top_padding")
	@Expose
	private String templateMobileTopPadding;
	@SerializedName("template_mobile_left_padding")
	@Expose
	private String templateMobileLeftPadding;
	@SerializedName("template_address_top_padding")
	@Expose
	private String templateAddressTopPadding;
	@SerializedName("template_address_left_padding")
	@Expose
	private String templateAddressLeftPadding;
	@SerializedName("template_social_top_padding")
	@Expose
	private String templateSocialTopPadding;
	@SerializedName("template_social_left_padding")
	@Expose
	private String templateSocialLeftPadding;
	@SerializedName("template_status")
	@Expose
	private String templateStatus;
	@SerializedName("sort_order")
	@Expose
	private String sortOrder;
	@SerializedName("created_at")
	@Expose
	private String createdAt;
	@SerializedName("template_demo_image_path")
	@Expose
	private String templateDemoImagePath;
	@SerializedName("template_image_path")
	@Expose
	private String templateImagePath;
	@SerializedName("template_font_family_path")
	@Expose
	private String templateFontFamilyPath;

	public String getTemplateId() {
		return templateId;
	}

	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}

	public String getPostId() {
		return postId;
	}

	public void setPostId(String postId) {
		this.postId = postId;
	}

	public String getTemplateTitle() {
		return templateTitle;
	}

	public void setTemplateTitle(String templateTitle) {
		this.templateTitle = templateTitle;
	}

	public String getTemplateDemoImage() {
		return templateDemoImage;
	}

	public void setTemplateDemoImage(String templateDemoImage) {
		this.templateDemoImage = templateDemoImage;
	}

	public String getTemplateImage() {
		return templateImage;
	}

	public void setTemplateImage(String templateImage) {
		this.templateImage = templateImage;
	}

	public String getTemplateThemeColor() {
		return templateThemeColor;
	}

	public void setTemplateThemeColor(String templateThemeColor) {
		this.templateThemeColor = templateThemeColor;
	}

	public String getTemplateFontFamily() {
		return templateFontFamily;
	}

	public void setTemplateFontFamily(String templateFontFamily) {
		this.templateFontFamily = templateFontFamily;
	}

	public String getTemplateLogoTopPadding() {
		return templateLogoTopPadding;
	}

	public void setTemplateLogoTopPadding(String templateLogoTopPadding) {
		this.templateLogoTopPadding = templateLogoTopPadding;
	}

	public String getTemplateLogoLeftPadding() {
		return templateLogoLeftPadding;
	}

	public void setTemplateLogoLeftPadding(String templateLogoLeftPadding) {
		this.templateLogoLeftPadding = templateLogoLeftPadding;
	}

	public String getTemplateTaglineTopPadding() {
		return templateTaglineTopPadding;
	}

	public void setTemplateTaglineTopPadding(String templateTaglineTopPadding) {
		this.templateTaglineTopPadding = templateTaglineTopPadding;
	}

	public String getTemplateTaglineLeftPadding() {
		return templateTaglineLeftPadding;
	}

	public void setTemplateTaglineLeftPadding(String templateTaglineLeftPadding) {
		this.templateTaglineLeftPadding = templateTaglineLeftPadding;
	}

	public String getTemplateEmailTopPadding() {
		return templateEmailTopPadding;
	}

	public String getTemplateTagline() {
		return templateTagline;
	}

	public void setTemplateTagline(String templateTagline) {
		this.templateTagline = templateTagline;
	}

	public void setTemplateEmailTopPadding(String templateEmailTopPadding) {
		this.templateEmailTopPadding = templateEmailTopPadding;
	}

	public String getTemplateEmailLeftPadding() {
		return templateEmailLeftPadding;
	}

	public void setTemplateEmailLeftPadding(String templateEmailLeftPadding) {
		this.templateEmailLeftPadding = templateEmailLeftPadding;
	}

	public String getTemplateMobileTopPadding() {
		return templateMobileTopPadding;
	}

	public void setTemplateMobileTopPadding(String templateMobileTopPadding) {
		this.templateMobileTopPadding = templateMobileTopPadding;
	}

	public String getTemplateMobileLeftPadding() {
		return templateMobileLeftPadding;
	}

	public void setTemplateMobileLeftPadding(String templateMobileLeftPadding) {
		this.templateMobileLeftPadding = templateMobileLeftPadding;
	}

	public String getTemplateAddressTopPadding() {
		return templateAddressTopPadding;
	}

	public void setTemplateAddressTopPadding(String templateAddressTopPadding) {
		this.templateAddressTopPadding = templateAddressTopPadding;
	}

	public String getTemplateAddressLeftPadding() {
		return templateAddressLeftPadding;
	}

	public void setTemplateAddressLeftPadding(String templateAddressLeftPadding) {
		this.templateAddressLeftPadding = templateAddressLeftPadding;
	}

	public String getTemplateSocialTopPadding() {
		return templateSocialTopPadding;
	}

	public void setTemplateSocialTopPadding(String templateSocialTopPadding) {
		this.templateSocialTopPadding = templateSocialTopPadding;
	}

	public String getTemplateSocialLeftPadding() {
		return templateSocialLeftPadding;
	}

	public void setTemplateSocialLeftPadding(String templateSocialLeftPadding) {
		this.templateSocialLeftPadding = templateSocialLeftPadding;
	}

	public String getTemplateStatus() {
		return templateStatus;
	}

	public void setTemplateStatus(String templateStatus) {
		this.templateStatus = templateStatus;
	}

	public String getSortOrder() {
		return sortOrder;
	}

	public void setSortOrder(String sortOrder) {
		this.sortOrder = sortOrder;
	}

	public String getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}

	public String getTemplateDemoImagePath() {
		return templateDemoImagePath;
	}

	public void setTemplateDemoImagePath(String templateDemoImagePath) {
		this.templateDemoImagePath = templateDemoImagePath;
	}

	public String getTemplateImagePath() {
		return templateImagePath;
	}

	public void setTemplateImagePath(String templateImagePath) {
		this.templateImagePath = templateImagePath;
	}

	public String getTemplateFontFamilyPath() {
		return templateFontFamilyPath;
	}

	public void setTemplateFontFamilyPath(String templateFontFamilyPath) {
		this.templateFontFamilyPath = templateFontFamilyPath;
	}

}
