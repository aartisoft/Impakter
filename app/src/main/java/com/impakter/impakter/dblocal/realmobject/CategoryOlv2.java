package com.impakter.impakter.dblocal.realmobject;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Administrator on 11/10/2017.
 */

public class CategoryOlv2 extends RealmObject {
    @PrimaryKey
    private String id;
    private String name;
    private String thumb;

    public CategoryOlv2() {
    }

    public CategoryOlv2(String id, String name, String thumb) {
        this.id = id;
        this.name = name;
        this.thumb = thumb;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
