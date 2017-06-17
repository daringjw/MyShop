package com.jkkc.myshop.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.jkkc.myshop.Constants;
import com.jkkc.myshop.R;
import com.jkkc.myshop.adapter.BaseAdapter;
import com.jkkc.myshop.adapter.CategoryAdapter;
import com.jkkc.myshop.adapter.WaresAdapter;
import com.jkkc.myshop.adapter.decoration.DividerGridItemDecoration;
import com.jkkc.myshop.adapter.decoration.DividerItemDecoration;
import com.jkkc.myshop.bean.Banner;
import com.jkkc.myshop.bean.Category;
import com.jkkc.myshop.bean.Page;
import com.jkkc.myshop.bean.Wares;
import com.jkkc.myshop.http.BaseCallback;
import com.jkkc.myshop.http.OkHttpHelper;
import com.jkkc.myshop.http.SpotsCallBack;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.util.List;

/**
 * Created by Guan on 2017/5/12.
 */

public class CategoryFragment extends Fragment {


    private RecyclerView mRecyclerView;

    private OkHttpHelper mOkHttpHelper = OkHttpHelper.getInstance();

    private CategoryAdapter mCategoryAdapter;

    private SliderLayout sliderShow;

    private WaresAdapter mWaresAdapter;

    private int currPage = 1;
    private int mTotalPage = 1;
    private int pageSize = 10;
    private long category_id = 0;

    public static final int STATE_NORMAL = 0;
    public static final int STATE_REFRESH = 1;
    public static final int STATE_MORE = 2;

    private int state = STATE_NORMAL;
    private RecyclerView mRecyclerview_wares;
    private MaterialRefreshLayout mRefresh_layout;


    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_category, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview_category);

        sliderShow = (SliderLayout) view.findViewById(R.id.slider);

        mRecyclerview_wares = (RecyclerView) view.findViewById(R.id.recyclerview_wares);

        mRefresh_layout = (MaterialRefreshLayout) view.findViewById(R.id.refresh_layout);

        requestCategoryData();
        requestBannerData();

        initRefreshLayout();

        return view;

    }

    private void initRefreshLayout() {

        mRefresh_layout.setLoadMore(true);
        mRefresh_layout.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {

                refreshData();

            }

            @Override
            public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {

                if (currPage <= mTotalPage) {
                    loadMoreData();

                } else {

                    Toast.makeText(getContext(), "没有更多数据了", Toast.LENGTH_SHORT).show();
                    mRefresh_layout.finishRefreshLoadMore();
                }


            }


        });

    }

    private void refreshData() {

        currPage = currPage + 1;
        state = STATE_REFRESH;
        requestWares(category_id);


    }

    private void loadMoreData() {

        currPage = ++currPage;
        state = STATE_MORE;
        requestWares(category_id);


    }

    private void requestCategoryData() {

        mOkHttpHelper.get(Constants.API.CATEGORY_LIST, new SpotsCallBack<List<Category>>(getContext()) {


            @Override
            public void onSuccess(Response response, List<Category> categories) {

                showCategoryData(categories);

                if (categories != null && categories.size() > 0) {

                    category_id = categories.get(0).getId();
                    requestWares(category_id);

                }

            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }
        });

    }

    private void showCategoryData(List<Category> categories) {

        mCategoryAdapter = new CategoryAdapter(getActivity(), categories);

        mCategoryAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                Category category = mCategoryAdapter.getItem(position);
                category_id = category.getId();
                currPage = 1;
                state = STATE_NORMAL;
                requestWares(category_id);

            }
        });

        mRecyclerView.setAdapter(mCategoryAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(),
                DividerItemDecoration.VERTICAL_LIST));


    }


    private void showSliderViews(List<Banner> banners) {


        if (banners != null) {

            for (Banner banner : banners) {

                DefaultSliderView sliderView = new DefaultSliderView(getActivity());
                sliderView.image(banner.getImgUrl());
                sliderView.description(banner.getName());
                sliderView.setScaleType(BaseSliderView.ScaleType.Fit);
                sliderShow.addSlider(sliderView);

            }
        }

        sliderShow.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
//        sliderShow.setCustomIndicator(mIndicator);
        sliderShow.setCustomAnimation(new DescriptionAnimation());
        sliderShow.setPresetTransformer(SliderLayout.Transformer.RotateDown);
        sliderShow.setDuration(3000);

        sliderShow.addOnPageChangeListener(new ViewPagerEx.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


    }


    private void requestBannerData() {

        String url = Constants.API.BANNER + "?type=1";

        mOkHttpHelper.get(url, new SpotsCallBack<List<Banner>>(getContext()) {

            @Override
            public void onSuccess(Response response, List<Banner> banners) {

                showSliderViews(banners);

            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }


        });


    }

    private void requestWares(long categoryId) {

        String url = Constants.API.WARES_LIST + "?categoryId=" + categoryId + "&curPage="
                + currPage + "&pageSize=" + pageSize;

        mOkHttpHelper.get(url, new BaseCallback<Page<Wares>>() {
            @Override
            public void onBeforeRequest(Request request) {

            }

            @Override
            public void onFailure(Request request, Exception e) {

            }

            @Override
            public void onResponse(Response response) {

            }

            @Override
            public void onSuccess(Response response, Page<Wares> waresPage) {

                currPage = waresPage.getCurrentPage();
                mTotalPage = waresPage.getTotalPage();

                showWaresData(waresPage.getList());


            }


            @Override
            public void onError(Response response, int code, Exception e) {

            }

            @Override
            public void onTokenError(Response response, int code) {

            }
        });

    }


    private void showWaresData(List<Wares> wares) {

        switch (state) {

            case STATE_NORMAL:

                if (mWaresAdapter == null) {
                    mWaresAdapter = new WaresAdapter(getContext(), wares);
                    mRecyclerview_wares.setAdapter(mWaresAdapter);
                    mRecyclerview_wares.setLayoutManager(new GridLayoutManager(getContext(), 2));
                    mRecyclerview_wares.setItemAnimator(new DefaultItemAnimator());
                    mRecyclerview_wares.addItemDecoration(new DividerGridItemDecoration(getContext()));

                } else {
                    mWaresAdapter.clear();
                    mWaresAdapter.addData(wares);



                }


                break;

            case STATE_REFRESH:

                mWaresAdapter.clear();
                mWaresAdapter.addData(wares);

                mRecyclerview_wares.scrollToPosition(0);
                mRefresh_layout.finishRefresh();

                break;

            case STATE_MORE:

                mWaresAdapter.addData(mWaresAdapter.getDatas().size(), wares);
                mRecyclerView.scrollToPosition(mWaresAdapter.getDatas().size());

                mRefresh_layout.finishRefreshLoadMore();


                break;

        }


    }
}
