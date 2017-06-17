package com.jkkc.myshop.adapter;

import android.content.Context;

import java.util.List;

/**
 * Created by Guan on 2017/5/26.
 */

public abstract class SimpleAdapter<T> extends BaseAdapter<T, BaseViewHolder> {



    public SimpleAdapter(Context context, int layoutResId) {
        super(context, layoutResId);
    }

    public SimpleAdapter(Context context, int layoutResId, List<T> datas) {
        super(context, layoutResId, datas);
    }




}
