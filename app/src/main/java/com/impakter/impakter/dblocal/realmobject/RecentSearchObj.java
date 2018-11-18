package com.impakter.impakter.dblocal.realmobject;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class RecentSearchObj extends RealmObject {
    @PrimaryKey
    private String keyWord;

    public String getKeyWord() {
        return keyWord;
    }

    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord;
    }
}
