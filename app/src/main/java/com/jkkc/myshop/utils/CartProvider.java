package com.jkkc.myshop.utils;

import android.util.SparseArray;

import com.jkkc.myshop.bean.ShoppingCart;

import java.util.List;

/**
 * Created by Guan on 2017/5/27.
 */

public class CartProvider {

    private SparseArray<ShoppingCart> datas = null;

    public CartProvider() {

        datas = new SparseArray<>(10);

    }

    public void put(ShoppingCart cart) {

        ShoppingCart temp = datas.get(cart.getId().intValue());

        if (temp != null) {
            temp.setCount(temp.getCount() + 1);
        } else {
            temp = cart;
        }

        datas.put(cart.getId().intValue(), temp);


    }

    public void update(ShoppingCart cart) {

        datas.put(cart.getId().intValue(), cart);
    }

    public void delete(ShoppingCart cart) {

        datas.delete(cart.getId().intValue());

    }

    public List<ShoppingCart> getAll() {


        return null;
    }


}
