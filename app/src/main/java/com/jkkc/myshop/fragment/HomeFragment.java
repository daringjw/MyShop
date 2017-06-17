package com.jkkc.myshop.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.google.gson.Gson;
import com.jkkc.myshop.Constants;
import com.jkkc.myshop.R;
import com.jkkc.myshop.adapter.HomeCatgoryAdapter;
import com.jkkc.myshop.adapter.decoration.DividerItemDecoration;
import com.jkkc.myshop.bean.Banner;
import com.jkkc.myshop.bean.Campaign;
import com.jkkc.myshop.bean.HomeCampaign;
import com.jkkc.myshop.http.BaseCallback;
import com.jkkc.myshop.http.OkHttpHelper;
import com.jkkc.myshop.http.SpotsCallBack;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.util.List;

/**
 * Created by Guan on 2017/5/12.
 */

public class HomeFragment extends Fragment {


    private SliderLayout sliderShow;
    private PagerIndicator mIndicator;
    private RecyclerView mRecyclerView;
    private HomeCatgoryAdapter mAdapter;

    private static final String TAG = "HomeFragment";

    private Gson mGson = new Gson();

    private List<Banner> mBanner;

    private OkHttpHelper httpHelper = OkHttpHelper.getInstance();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);
        sliderShow = (SliderLayout) view.findViewById(R.id.slider);

        mIndicator = (PagerIndicator) view.findViewById(R.id.custom_indicator);

        requestImages();

        initRecylcerView(view);

        return view;

    }

    private void requestImages() {

        String url = "http://112.124.22.238:8081/course_api/banner/query?type=1";
//
//        OkHttpClient client = new OkHttpClient();
//        RequestBody body = new FormEncodingBuilder()
//                .add("type", "1")
//                .build();
//
//
//        Request request = new Request.Builder()
//                .url(url)
//                .post(body)
//                .build();
//
//
//        client.newCall(request).enqueue(new Callback() {
//            @Override
//            public void onFailure(Request request, IOException e) {
//
//            }
//
//            @Override
//            public void onResponse(Response response) throws IOException {
//
//                if (response.isSuccessful()) {
//
//                    String json = response.body().string();
//
//                    Type type = new TypeToken<List<Banner>>() {
//                    }.getType();
//                    mBanner = mGson.fromJson(json, type);
//
//
//                    initSlider();
//
//
//                }
//            }
//        });

        httpHelper.get(url, new SpotsCallBack<List<Banner>>(getContext()) {

            @Override
            public void onSuccess(Response response, List<Banner> banners) {

                mBanner = banners;
                initSlider();

            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }


        });


    }


    private void initRecylcerView(View view) {

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);

//        List<HomeCategory> datas = new ArrayList<>(15);
//
//        HomeCategory category = new HomeCategory("热门活动", R.drawable.img_big_1, R.drawable.img_1_small1, R.drawable.img_1_small2);
//        datas.add(category);
//
//        category = new HomeCategory("有利可图", R.drawable.img_big_4, R.drawable.img_4_small1, R.drawable.img_4_small2);
//        datas.add(category);
//        category = new HomeCategory("品牌街", R.drawable.img_big_2, R.drawable.img_2_small1, R.drawable.img_2_small2);
//        datas.add(category);
//
//        category = new HomeCategory("金融街 包赚翻", R.drawable.img_big_1, R.drawable.img_3_small1, R.drawable.imag_3_small2);
//        datas.add(category);
//
//        category = new HomeCategory("超值购", R.drawable.img_big_0, R.drawable.img_0_small1, R.drawable.img_0_small2);
//        datas.add(category);
//
//
//        mAdapter = new HomeCatgoryAdapter(datas);
//        mRecyclerView.setAdapter(mAdapter);
//        mRecyclerView.addItemDecoration(new DividerItemDecortion());
//        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        httpHelper.get(Constants.API.CAMPAIGN_HOME, new BaseCallback<List<HomeCampaign>>() {

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
            public void onSuccess(Response response, List<HomeCampaign> homeCampaigns) {


                initData(homeCampaigns);


            }

            @Override
            public void onError(Response response, int code, Exception e) {


            }

            @Override
            public void onTokenError(Response response, int code) {


            }
        });


    }

    private void initData(List<HomeCampaign> homeCampaigns) {

        mAdapter = new HomeCatgoryAdapter(homeCampaigns, getActivity());
        mAdapter.setOnCompaignClickListener(new HomeCatgoryAdapter.onCampaignClickListener() {
            @Override
            public void onClick(View view, Campaign campaign) {

                Toast.makeText(getActivity(), "titile=" + campaign.getTitle(), Toast.LENGTH_SHORT)
                        .show();
            }
        });

        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(),
                DividerItemDecoration.VERTICAL_LIST));

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


    }

    private void initSlider() {

        if (mBanner != null) {

            for (final Banner banner : mBanner) {

                TextSliderView textSliderView = new TextSliderView(getActivity());
                textSliderView.image(banner.getImgUrl());
                textSliderView.description(banner.getName());
                textSliderView.setScaleType(BaseSliderView.ScaleType.Fit);
                sliderShow.addSlider(textSliderView);

            }
        }

        sliderShow.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
//        sliderShow.setCustomIndicator(mIndicator);
        sliderShow.setCustomAnimation(new DescriptionAnimation());
        sliderShow.setPresetTransformer(SliderLayout.Transformer.RotateUp);
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


    @Override
    public void onStop() {
        sliderShow.stopAutoCycle();
        super.onStop();
    }


}
