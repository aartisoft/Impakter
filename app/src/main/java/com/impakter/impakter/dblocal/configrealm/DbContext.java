package com.impakter.impakter.dblocal.configrealm;

import android.content.Context;

import com.impakter.impakter.dblocal.realmobject.CartItemRealmObj;
import com.impakter.impakter.dblocal.realmobject.RecentSearchObj;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by Minh Toan on 15/09/2018
 */

public class DbContext {
    private Realm realm;

    public DbContext(Context context) {
        Realm.init(context);
        realm = Realm.getDefaultInstance();

    }

    private static DbContext instance;

    public static DbContext getInstance() {
        return instance;
    }

    public static void init(Context context) {
        if (instance == null) {
            instance = new DbContext(context);
        }
    }

    public void addItemToCart(final CartItemRealmObj object) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(object);

            }
        });
    }

    public CartItemRealmObj getCartByKey(String primaryKey) {
        CartItemRealmObj cartItemRealmObj = realm.where(CartItemRealmObj.class).equalTo("primaryKeyItem", primaryKey).findFirst();
        return cartItemRealmObj;
    }

    public List<CartItemRealmObj> getCartItems() {
        RealmResults<CartItemRealmObj> dataRealmResults =
                realm.where(CartItemRealmObj.class).findAll();
        return dataRealmResults;
    }

    //===============
    boolean isDeleted;

    public boolean deleteCartItem(String primaryKeyItem) {
        final RealmResults<CartItemRealmObj> results = realm.where(CartItemRealmObj.class)
                .equalTo("primaryKeyItem", primaryKeyItem).findAll();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                isDeleted = results.deleteAllFromRealm();
            }
        });
        return isDeleted;
    }

    public boolean deleteAllCartItem() {
        final RealmResults<CartItemRealmObj> results = realm.where(CartItemRealmObj.class).findAll();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                isDeleted = results.deleteAllFromRealm();
            }
        });
        return isDeleted;
    }

    public boolean isExist(final String primaryKey) {
        RealmResults<CartItemRealmObj> dataRealmResults =
                realm.where(CartItemRealmObj.class).equalTo("primaryKeyItem", primaryKey).findAll();
        return dataRealmResults.size() != 0;
    }

    public void addToRecentSearch(final RecentSearchObj keyword) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(keyword);
            }
        });
    }

    public List<RecentSearchObj> getRecentSearch() {
        RealmResults<RecentSearchObj> dataRealmResults =
                realm.where(RecentSearchObj.class).findAll();
        return dataRealmResults;
    }

    public boolean deleteRecentSearcdItem(String primaryKey) {
        final RealmResults<RecentSearchObj> results = realm.where(RecentSearchObj.class)
                .equalTo("keyWord", primaryKey).findAll();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                isDeleted = results.deleteAllFromRealm();
            }
        });
        return isDeleted;
    }
}
