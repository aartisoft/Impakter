package com.impakter.impakter.dblocal.realmobject;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by Administrator on 11/10/2017.
 */

public class CookMethodOl extends RealmObject {
    private String id, name;
    private boolean isSelected;
    private RealmList<CookMethodOl> arrSubMethods;

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
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

    public RealmList<CookMethodOl> getArrSubMethods() {
        return arrSubMethods;
    }

  /*  public void setArrSubMethods(ArrayList<CookMethod> arrSubMethods) {
        ArrayList<CookMethod> arr = new ArrayList<CookMethod>();
        for (CookMethod cookMethod : arrSubMethods) {
            try {
                arr.add((CookMethod) cookMethod.clone());
            } catch (CloneNotSupportedException e) {
                // TODO Auto-generated catch block
                arr.add(cookMethod);
            }
        }
        this.arrSubMethods = arr;
    }*/

}
