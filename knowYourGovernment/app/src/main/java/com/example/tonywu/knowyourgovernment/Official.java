package com.example.tonywu.knowyourgovernment;

import java.io.Serializable;

public class Official implements Serializable {

    public static String noData = "no data available";
    public static String unknown = "unknown";

    //recyclerview
    private String name;
    private String office;
    private String party;

    //official
    private String address;
    private String phone;
    private String email;
    private String URL;

    private String photoUrl;
    private String googleplus;
    private String facebook;
    private String twitter;
    private String youtube;


    public Official(){
        this.name = noData;
        this.office = noData;
        this.party = noData;
        this.address = noData;
        this.phone = noData;
        this.email = noData;
        this.URL = noData;
        this.photoUrl = noData;
        this.googleplus = noData;
        this.facebook = noData;
        this.twitter = noData;
        this.youtube = noData;

    }
    public Official(String name, String office, String party, String address, String phone, String email, String URL, String photoUrl, String googleplus, String facebook, String twitter, String youtube){
        this.name = name;
        this.office = office;
        this.party = party;
        this.address = address;
        this.phone = phone;
        this.email = email;
        this.URL = URL;
        this.photoUrl = photoUrl;
        this.googleplus = googleplus;
        this.facebook = facebook;
        this.twitter = twitter;
        this.youtube = youtube;
    }

    public String getName(){
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getOffice(){
        return office;
    }
    public void setOffice(String office){
        this.office = office;
    }
    public String getParty(){
        return party;

    }
    public void setParty(String party){
        this.party = party;

    }
    public String getAddress(){
        return address;
    }
    public void setAddress(String address){
        this.address = address;
    }
    public String getPhone(){
        return phone;
    }
    public void setPhone(){
        this.phone = phone;
    }
    public String getEmail(){
        return email;
    }
    public void setEmail(String email){
        this.email = email;
    }
    public String getURL(){
        return URL;
    }
    public void setURL(String URL){
        this.URL = URL;
    }
    public String getPhotoUrl(){
        return photoUrl;
    }
    public void setPhotoUrl(String photoUrl){
        this.photoUrl = photoUrl;
    }
    public void setGoogleplus(String googleplus){
        this.googleplus = googleplus;
    }
    public void setFacebook(String facebook){
        this.facebook = facebook;
    }
    public void setTwitter(String twitter){
        this.twitter = twitter;
    }
    public void setYoutube(String youtube){
        this.youtube = youtube;
    }
    public String getGoogleplus(){
        return googleplus;
    }
    public String getFacebook(){
        return facebook;
    }
    public String getTwitter(){
        return twitter;
    }
    public String getYoutube(){
        return youtube;
    }





}
