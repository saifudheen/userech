package com.gofreshuser.model;

public class Sildermodel {
    public String banner;
    public String bannerlink;

    public String getBanner() {
        return banner;
    }

    public void setBanner(String banner) {
        this.banner = banner;
    }

    public String getBannerlink() {
        return bannerlink;
    }

    public void setBannerlink(String bannerlink) {

        this.bannerlink = bannerlink;
    }

    public Sildermodel(String banner, String bannerlink) {
        this.bannerlink=bannerlink;
        this.banner = banner;
    }

}
