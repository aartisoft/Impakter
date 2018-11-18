package com.impakter.impakter.dblocal.realmobject;

import io.realm.RealmList;
import io.realm.RealmObject;

public class ItemCartOl extends RealmObject {
    private static final String TAG = ItemCartOl.class.getName();

    private ItemOl item;
    private RealmList<RelishOl> arrRelish;
    private double totalprice;
    private String insTructions;
    private int quantities = 1;
    private int oi_course;

    public ItemCartOl(ItemOl item, RealmList<RelishOl> arrRelish, double totalprice, String insTructions, int quantities, int oi_course) {
        this.item = item;
        this.arrRelish = arrRelish;
        this.totalprice = totalprice;
        this.insTructions = insTructions;
        this.quantities = quantities;
        this.oi_course = oi_course;
    }

    public ItemCartOl() {
    }

    public int getOi_course() {
        return oi_course;
    }

    public void setOi_course(int oi_course) {
        this.oi_course = oi_course;
    }

    public String getInsTructions() {
        return insTructions;
    }

    public void setInsTructions(String insTructions) {
        this.insTructions = insTructions;
    }

    public int getQuantities() {
        return quantities;
    }

    public void setQuantities(int quantities) {
        this.quantities = quantities;
    }

    public double getTotalprice() {
        return totalprice;
    }

    public void setTotalprice(double totalprice) {
        this.totalprice = totalprice;
    }

    public static String getTAG() {
        return TAG;
    }

    public ItemOl getItem() {
        return item;
    }

    public void setItem(ItemOl item) {
        this.item = item;
    }

    public RealmList<RelishOl> getArrRelish() {
        return arrRelish;
    }

    public void setArrRelish(RealmList<RelishOl> arrRelish) {
        this.arrRelish = arrRelish;
    }
}
