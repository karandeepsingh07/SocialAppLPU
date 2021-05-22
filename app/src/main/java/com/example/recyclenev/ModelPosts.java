package com.example.recyclenev;

public class ModelPosts {
    String pId,pTitle, pDescription, pImage, pTime,pEmil;

    public ModelPosts(){
        // empty constructor
    }

    public ModelPosts(String pId, String pTitle, String pDescription, String pImage, String pTime, String pEmil) {
        this.pId = pId;
        this.pTitle = pTitle;
        this.pDescription = pDescription;
        this.pImage = pImage;
        this.pTime = pTime;
        this.pEmil = pEmil;
    }

    public String getpId() {
        return pId;
    }

    public void setpId(String pId) {
        this.pId = pId;
    }

    public String getpTitle() {
        return pTitle;
    }

    public void setpTitle(String pTitle) {
        this.pTitle = pTitle;
    }

    public String getpDescription() {
        return pDescription;
    }

    public void setpDescription(String pDescription) {
        this.pDescription = pDescription;
    }

    public String getpImage() {
        return pImage;
    }

    public void setpImage(String pImage) {
        this.pImage = pImage;
    }

    public String getpTime() {
        return pTime;
    }

    public void setpTime(String pTime) {
        this.pTime = pTime;
    }

    public String getpEmil() {
        return pEmil;
    }

    public void setpEmil(String pEmil) {
        this.pEmil = pEmil;
    }
}
