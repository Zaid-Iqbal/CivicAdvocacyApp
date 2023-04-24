package com.example.civicadvocacyapp;

import java.io.Serializable;

public class Official implements Serializable {

    public String Name;
    public String Title;
    public String Address;
    public String Party;
    public String Phone;
    public String Website;
    public String Email;
    public String Pic;
    public String Facebook;
    public String Twitter;
    public String Youtube;

    public Official(String name, String title, String address, String party, String phone, String website, String email,
                    String pic, String facebook, String twitter, String youtube)
    {
        Name = name;
        Title = title;
        Address = address;
        Party = party;
        Phone = phone;
        Website = website;
        Email = email;
        Pic = pic;
        Facebook = facebook;
        Twitter = twitter;
        Youtube = youtube;
    }

}
