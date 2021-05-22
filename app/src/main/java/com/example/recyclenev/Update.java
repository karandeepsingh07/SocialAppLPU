package com.example.recyclenev;

public class Update {


    String tier;
    String Domain;
    String contact;
    String ceo;
    String CoCeo, CulSec;
    String about;
    String Title;
    String uid;
    String imageUrl;

    public Update() {

    }

    public Update(String tier, String domain, String contact, String ceo, String coCeo, String culSec, String about, String title, String uid, String imageUrl) {
        this.tier = tier;
        Domain = domain;
        this.contact = contact;
        this.ceo = ceo;
        CoCeo = coCeo;
        CulSec = culSec;
        this.about = about;
        Title = title;
        this.uid = uid;
        this.imageUrl = imageUrl;
    }

    public String getTier() {
        return tier;
    }

    public void setTier(String tier) {
        this.tier = tier;
    }

    public String getDomain() {
        return Domain;
    }

    public void setDomain(String domain) {
        Domain = domain;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getCeo() {
        return ceo;
    }

    public void setCeo(String ceo) {
        this.ceo = ceo;
    }

    public String getCoCeo() {
        return CoCeo;
    }

    public void setCoCeo(String coCeo) {
        CoCeo = coCeo;
    }

    public String getCulSec() {
        return CulSec;
    }

    public void setCulSec(String culSec) {
        CulSec = culSec;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
