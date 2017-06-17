package com.jkkc.myshop.adapter;

import android.content.Context;

import com.jkkc.myshop.R;
import com.jkkc.myshop.bean.Category;

import java.util.List;

/**
 * Created by Guan on 2017/5/26.
 */

public class CategoryAdapter extends SimpleAdapter<Category> {


    public CategoryAdapter(Context context, List<Category> datas) {
        super(context, R.layout.template_single_text, datas);
    }

    @Override
    protected void convert(BaseViewHolder viewHoder, Category item) {


        viewHoder.getTextView(R.id.textView).setText(item.getName());

    }
}
