package com.lnsoft.dataobject;

import java.io.Serializable;

/**
 * Created By Chr on 2019/1/28/0028.
 */
public class ItemInfo implements Serializable{

    private Integer id;

    private String title;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "ItemInfo{" +
                "id=" + id +
                ", title='" + title + '\'' +
                '}';
    }
}
