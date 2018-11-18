package com.impakter.impakter.social.facebook;


import com.impakter.impakter.object.UserObj;

public class FbUser {
    String id, name, email, avatar;
    String first_name, last_name;

    public FbUser(String id, String name, String email, String avatar, String first_name, String last_name) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.avatar = avatar;
        this.first_name = first_name;
        this.last_name = last_name;
    }

    public UserObj toUser() {
        UserObj user = new UserObj();
        user.setAvatar(avatar);
        user.setUsername(name);
        user.setEmail(email);
        user.setId(Integer.parseInt(id));
        user.setFirstName(first_name);
        user.setLastName(last_name);
        return user;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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
