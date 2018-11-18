package com.impakter.impakter.dblocal.realmobject;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Administrator on 10/19/2017.
 */

public class OderHistoryOl extends RealmObject {
    public static final String PRIMARY_KEY = "primary_key";
    public static final String NOTIFY_ADAPTER = "notify_adapter";

    private String status;
    private String orderPrice;
    private String name;
    private String phone;
    private String paymentMethod;
    @PrimaryKey
    private String time;
    private String address;
    private String id;
    private String type;
    private String tableId;
    private String seats;
    private RealmList<ItemCartOl> listItemCartOl;

    public OderHistoryOl() {
    }

    public OderHistoryOl(String status, String orderPrice, String name, String phone, String paymentMethod, String time, String address, String id, String type, String tableId, String seats, RealmList<ItemCartOl> listItemCartOl) {
        this.status = status;
        this.name = name;
        this.phone = phone;
        this.paymentMethod = paymentMethod;
        this.time = time;
        this.address = address;
        this.id = id;
        this.type = type;
        this.tableId = tableId;
        this.seats = seats;
        this.listItemCartOl = listItemCartOl;
        this.orderPrice = orderPrice;
    }

    public String getOrderPrice() {
        return orderPrice;
    }

    public void setOrderPrice(String orderPrice) {
        this.orderPrice = orderPrice;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTableId() {
        return tableId;
    }

    public void setTableId(String tableId) {
        this.tableId = tableId;
    }

    public String getSeats() {
        return seats;
    }

    public void setSeats(String seats) {
        this.seats = seats;
    }

    public RealmList<ItemCartOl> getListItemCartOl() {
        return listItemCartOl;
    }

    public void setListItemCartOl(RealmList<ItemCartOl> listItemCartOl) {
        this.listItemCartOl = listItemCartOl;
    }
}
