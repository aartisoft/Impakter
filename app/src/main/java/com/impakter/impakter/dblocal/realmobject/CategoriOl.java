package com.impakter.impakter.dblocal.realmobject;


import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by Administrator on 11/10/2017.
 */

public class CategoriOl extends RealmObject {
    private String id, name;
    private boolean isPanini = false;
    private RealmList<RelishOl> arrRelishs;
    private RealmList<CookMethodOl> arrCookMethods;
    private boolean isSelected = false;

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

    public boolean isPanini() {
        return isPanini;
    }

    public void setPanini(boolean isPanini) {
        this.isPanini = isPanini;
    }

    public RealmList<RelishOl> getArrRelishs() {
        return arrRelishs;
    }

    public void setArrRelishs(RealmList<RelishOl> arrRelishs) {
        this.arrRelishs = arrRelishs;
    }

    public RealmList<CookMethodOl> getArrCookMethods() {
        return arrCookMethods;
    }

    public void setArrCookMethods(RealmList<CookMethodOl> arrCookMethods) {
        this.arrCookMethods = arrCookMethods;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }
}
