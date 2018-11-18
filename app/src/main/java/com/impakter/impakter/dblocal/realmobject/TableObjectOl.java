package com.impakter.impakter.dblocal.realmobject;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Administrator on 11/10/2017.
 */

public class TableObjectOl extends RealmObject {
    @PrimaryKey
    private String id;
    private String roomId;
    private String name;
    private String num_of_seat;
    private String current_seat;
    private String waiter_id;
    private String waiter_name;

    public TableObjectOl() {
    }

    public TableObjectOl(String id, String roomId, String name, String num_of_seat, String current_seat, String waiter_id, String waiter_name) {
        this.id = id;
        this.roomId = roomId;
        this.name = name;
        this.num_of_seat = num_of_seat;
        this.current_seat = current_seat;
        this.waiter_id = waiter_id;
        this.waiter_name = waiter_name;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getWaiter_name() {
        return waiter_name;
    }

    public void setWaiter_name(String waiter_name) {
        this.waiter_name = waiter_name;
    }

    public String getWaiter_id() {
        return waiter_id;
    }

    public void setWaiter_id(String waiter_id) {
        this.waiter_id = waiter_id;
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

    public String getNum_of_seat() {
        return num_of_seat;
    }

    public void setNum_of_seat(String num_of_seat) {
        this.num_of_seat = num_of_seat;
    }

    public String getCurrent_seat() {
        return current_seat;
    }

    public void setCurrent_seat(String current_seat) {
        this.current_seat = current_seat;
    }
}
