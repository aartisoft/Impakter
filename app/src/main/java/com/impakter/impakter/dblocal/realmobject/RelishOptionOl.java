package com.impakter.impakter.dblocal.realmobject;

import io.realm.RealmObject;

public class RelishOptionOl extends RealmObject {

    private String id, name,method_price;
    private int positionSelect = 0;

    public RelishOptionOl(String id, String name, String method_price) {
        this.id = id;
        this.name = name;
        this.method_price = method_price;
    }

    public RelishOptionOl(String id, String name, String method_price, int positionSelect) {
        this.id = id;
        this.name = name;
        this.method_price = method_price;
        this.positionSelect = positionSelect;
    }

    public RelishOptionOl() {
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

    public String getMethod_price() {
        return method_price;
    }

    public void setMethod_price(String method_price) {
        this.method_price = method_price;
    }

    public int getPositionSelect() {
        return positionSelect;
    }

    public void setPositionSelect(int positionSelect) {
        this.positionSelect = positionSelect;
    }
}
