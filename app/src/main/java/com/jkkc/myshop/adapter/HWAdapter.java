package com.jkkc.myshop.adapter;

import android.content.Context;

import com.jkkc.myshop.bean.Wares;

import java.util.List;

/**
 * Created by Guan on 2017/5/26.
 */

public class HWAdapter extends SimpleAdapter<Wares> {


    public HWAdapter(Context context, int layoutResId, List<Wares> datas) {
        super(context, layoutResId, datas);
    }

    @Override
    protected void convert(BaseViewHolder viewHoder, Wares item) {

    }
}
