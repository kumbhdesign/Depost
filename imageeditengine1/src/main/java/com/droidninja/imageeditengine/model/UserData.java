package com.droidninja.imageeditengine.model;

public class UserData {
    private String facebookUrl;
    private String country;
    private String googleId;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public UserData(String facebookUrl, String companyEmail, String company_logo_path, String websiteUrl, String instagramUrl, String linkedinUrl, String mobileNumber, String address, String twitter_url) {
        this.facebookUrl = facebookUrl;
        this.companyEmail = companyEmail;
        this.company_logo_path = company_logo_path;
        this.websiteUrl = websiteUrl;
        this.instagramUrl = instagramUrl;
        this.linkedinUrl = linkedinUrl;
        this.mobileNumber = mobileNumber;
        this.address = address;
        this.twitter_url = twitter_url;

    }

    private String loginType;
    private String userName;
    private String mobile;
    private String companyEmail;
    private String createdAt;
    private String macId;
    private String token;
    private String profileImg;
    private String companyLogo;
    private String websiteUrl;
    private String instagramUrl;
    private String userId;
    private String fbId;
    private String linkedinUrl;
    private String mobileNumber;
    private String address;
    private String twitter_url;

    public String getCompany_logo_path() {
        return company_logo_path;
    }

    public void setCompany_logo_path(String company_logo_path) {
        this.company_logo_path = company_logo_path;
    }

    String company_logo_path;

    public String getFacebookUrl() {
        return facebookUrl;
    }

    public String getCountry() {
        return country;
    }

    public String getGoogleId() {
        return googleId;
    }

    public String getLoginType() {
        return loginType;
    }

    public String getUserName() {
        return userName;
    }

    public String getMobile() {
        return mobile;
    }

    public String getCompanyEmail() {
        return companyEmail;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getMacId() {
        return macId;
    }

    public String getToken() {
        return token;
    }

    public String getProfileImg() {
        return profileImg;
    }

    public String getCompanyLogo() {
        return companyLogo;
    }

    public String getWebsiteUrl() {
        return websiteUrl;
    }

    public String getInstagramUrl() {
        return instagramUrl;
    }

    public String getUserId() {
        return userId;
    }

    public String getFbId() {
        return fbId;
    }

    public String getLinkedinUrl() {
        return linkedinUrl;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public String getTwitter_url() {
        return twitter_url;
    }

    public void setTwitter_url(String twitter_url) {
        this.twitter_url = twitter_url;
    }
}
