package com.ada.nytimes.network.dto.articleSearch;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

/**
 * Created by ada on 10/22/16.
 */
@Parcel
public class Multimedia {
/*
    @SerializedName("width")
    @Expose
    public Integer width;
*/
    @SerializedName("url")
    @Expose
    public String url;
/*
    @SerializedName("height")
    @Expose
    public Integer height;

    @SerializedName("subtype")
    @Expose
    public String subtype;

    @SerializedName("legacy")
    @Expose
    public Object legacy;

*/
    @SerializedName("type")
    @Expose
    public String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
/*
    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }
*/
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
/*
    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public String getSubtype() {
        return subtype;
    }

    public void setSubtype(String subtype) {
        this.subtype = subtype;
    }

    public Object getLegacy() {
        return legacy;
    }

    public void setLegacy(Object legacy) {
        this.legacy = legacy;
    }
    */
}
