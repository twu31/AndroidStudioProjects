package com.example.tonywu.knowyourgov;

import java.io.Serializable;
import java.util.Arrays;


public class Official implements Serializable {




    private String name;
    private String office;
    private String party;


    private String address;
    private String phone;
    private String url;
    private String email;

    private String photoUrl;
    private String googleplus;
    private String facebook;
    private String twitter;
    private String youtube;

    public Official() {
        this.name = "No data Provided";
        this.office = "No data Provided";
        this.party = "No data provided";
        this.address = "No data provided";
        this.phone = "No data provided";
        this.url = "No data provided";
        this.email = "No data provided";
        this.photoUrl = "No data provided";
        this.googleplus = "No data provided";
        this.facebook = "No data provided";
        this.twitter = "No data provided";
        this.youtube = "No data provided";
    }

    public Official(String name, String office, String party, String address, String phone, String url, String email, String photoUrl, String googleplus, String facebook, String twitter, String youtube) {
        this.name = name;
        this.office = office;
        this.party = party;
        this.address = address;
        this.phone = phone;
        this.url = url;
        this.email = email;
        this.photoUrl = photoUrl;
        this.googleplus = googleplus;
        this.facebook = facebook;
        this.twitter = twitter;
        this.youtube = youtube;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOffice() {
        return office;
    }

    public void setOffice(String office) {
        this.office = office;
    }

    public String getParty() {
        return party;
    }

    public void setParty(String party) {
        this.party = party;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getGoogleplus() {
        return googleplus;
    }

    public void setGoogleplus(String googleplus) {
        this.googleplus = googleplus;
    }

    public String getFacebook() {
        return facebook;
    }

    public void setFacebook(String facebook) {
        this.facebook = facebook;
    }

    public String getTwitter() {
        return twitter;
    }

    public void setTwitter(String twitter) {
        this.twitter = twitter;
    }

    public String getYoutube() {
        return youtube;
    }

    public void setYoutube(String youtube) {
        this.youtube = youtube;
    }


}