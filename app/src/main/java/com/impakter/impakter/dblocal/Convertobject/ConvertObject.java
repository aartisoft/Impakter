package com.impakter.impakter.dblocal.Convertobject;

import com.impakter.impakter.dblocal.realmobject.CartItemRealmObj;
import com.impakter.impakter.dblocal.realmobject.RecentSearchObj;
import com.impakter.impakter.object.CartItemObj;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 11/10/2017.
 */

public class ConvertObject {
    // TODO: 17/09/2018 Toan
    public static ArrayList<CartItemObj> convertCartItemRealObjToCartItemObj(List<CartItemRealmObj> cartItemRealmObjs) {
        ArrayList<CartItemObj> cartItemObjs = new ArrayList<>();
        for (CartItemRealmObj item : cartItemRealmObjs) {
            CartItemObj cartItemObj = new CartItemObj();
            cartItemObj.setId(item.getId());
            cartItemObj.setName(item.getName());
            cartItemObj.setImage(item.getImage());
            cartItemObj.setBrand(item.getBrand());
            cartItemObj.setBrandId(item.getBrandId());
            cartItemObj.setCode(item.getCode());
            cartItemObj.setOption(item.getOption());
            cartItemObj.setQuantity(item.getQuantity());
            cartItemObj.setPrice(item.getPrice());
            cartItemObj.setTotalPrice(item.getTotalPrice());
            cartItemObjs.add(cartItemObj);
        }
        return cartItemObjs;
    }

    public static ArrayList<String> convertRealmStringToString(List<RecentSearchObj> recentSearchObjs) {
        ArrayList<String> listKeyWord = new ArrayList<>();
        for (RecentSearchObj item : recentSearchObjs) {
            listKeyWord.add(item.getKeyWord());
        }
        return listKeyWord;
    }
}
