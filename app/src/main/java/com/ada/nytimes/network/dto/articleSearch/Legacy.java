package com.ada.nytimes.network.dto.articleSearch;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by ada on 10/22/16.
 */
public class Legacy {

    @SerializedName("wide")
    @Expose
    public String wide;

    @SerializedName("wideheight")
    @Expose
    public String wideheight;

    @SerializedName("widewidth")
    @Expose
    public String widewidth;

    public String getWide() {
        return wide;
    }

    public void setWide(String wide) {
        this.wide = wide;
    }

    public String getWideheight() {
        return wideheight;
    }

    public void setWideheight(String wideheight) {
        this.wideheight = wideheight;
    }

    public String getWidewidth() {
        return widewidth;
    }

    public void setWidewidth(String widewidth) {
        this.widewidth = widewidth;
    }
}
