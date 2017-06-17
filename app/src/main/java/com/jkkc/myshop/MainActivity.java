package com.jkkc.myshop;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;

import com.jkkc.myshop.bean.Tab;
import com.jkkc.myshop.fragment.CartFragment;
import com.jkkc.myshop.fragment.CategoryFragment;
import com.jkkc.myshop.fragment.HomeFragment;
import com.jkkc.myshop.fragment.HotFragment;
import com.jkkc.myshop.fragment.MineFragment;
import com.jkkc.myshop.widget.FragmentTabHost;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class MainActivity extends AppCompatActivity {


    private FragmentTabHost mTabhost;

    private LayoutInflater mInflater;

    private List<Tab> mTabs = new ArrayList<>(5);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initTab();


    }

    private void initTab() {

        Tab tabHome = new Tab(R.string.home, R.drawable.selector_icon_home, HomeFragment.class);
        Tab tabHot = new Tab(R.string.hot, R.drawable.selector_icon_hot, HotFragment.class);
        Tab tabCategory = new Tab(R.string.catagory, R.drawable.selector_icon_category, CategoryFragment.class);
        Tab tabCart = new Tab(R.string.cart, R.drawable.selector_icon_cart, CartFragment.class);
        Tab tabMine = new Tab(R.string.mine, R.drawable.selector_icon_mine, MineFragment.class);

        mTabs.add(tabHome);
        mTabs.add(tabHot);
        mTabs.add(tabCategory);
        mTabs.add(tabCart);
        mTabs.add(tabMine);

        mInflater = LayoutInflater.from(this);
        mTabhost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        mTabhost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);

        for (Tab tab : mTabs
                ) {
            TabHost.TabSpec tabSpec = mTabhost.newTabSpec(getString(tab.getTitle()));


            tabSpec.setIndicator(buildIndicator(tab));

            mTabhost.addTab(tabSpec, tab.getFragment(), null);

        }

        mTabhost.getTabWidget().setShowDividers(LinearLayout.SHOW_DIVIDER_NONE);
        mTabhost.setCurrentTab(0);


    }


    private View buildIndicator(Tab tab) {

        View view = mInflater.inflate(R.layout.tab_indicator, null);
        ImageView img = (ImageView) view.findViewById(R.id.icon_tab);
        TextView text = (TextView) view.findViewById(R.id.txt_indicator);

        img.setBackgroundResource(tab.getIcon());
        text.setText(tab.getTitle());

        return view;

    }


}
