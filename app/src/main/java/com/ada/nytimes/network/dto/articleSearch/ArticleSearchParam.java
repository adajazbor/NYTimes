package com.ada.nytimes.network.dto.articleSearch;

import android.text.TextUtils;

import com.annimon.stream.Stream;

import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ada on 10/21/16.
 */
@Parcel
public class ArticleSearchParam {

    public enum SortValues {
        newest,
        oldest
    }

    String q;
    String beginDate;//YYYYMMDD
    List<String> newsDesk = new ArrayList<>();
    SortValues sort = SortValues.newest;
    Integer page = 0;

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public String getQ() {
        return q;
    }

    public void setQ(String q) {
        this.q = q;
    }

    public String getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(String beginDate) {
        this.beginDate = beginDate;
    }

    public List<String> getNewsDesk() {
        return newsDesk;
    }

    public void setNewsDesk(List<String> newsDesk) {
        this.newsDesk = newsDesk;
    }

    public SortValues getSort() {
        return sort;
    }

    public void setSort(SortValues sort) {
        this.sort = sort;
    }

    public Map<String, String> getAsMap() {
        Map<String, String> result = new HashMap<>();
        if (!TextUtils.isEmpty(getQ())) {
            result.put("q", getQ());
        }
        if (!TextUtils.isEmpty(getBeginDate())) {
            result.put("begin_date", getBeginDate());
        }
        if (getNewsDesk() != null && getNewsDesk().size() > 0) {
            result.put("fl", getNewDeskList());
        }
        result.put("page", (getPage() == null ? "0" : getPage().toString()));
        result.put("sort", (getSort() == null ? SortValues.newest.name() : getSort().name()));
        return result;
    }

    public String getNewDeskList() {
        StringBuilder sb = new StringBuilder("news_desk:(");
        Stream.of(getNewsDesk()).forEach(param -> sb.append("\"").append(param).append("\","));
        sb.deleteCharAt(sb.length() -1);
        sb.append(")");
        return sb.toString();
    }

    public void increasePage() {
        this.page++;
    }

    public void resetPage() {
        setPage(0);
    }
}
