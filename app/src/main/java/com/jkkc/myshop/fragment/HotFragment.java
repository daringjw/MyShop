package com.jkkc.myshop.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.jkkc.myshop.Constants;
import com.jkkc.myshop.R;
import com.jkkc.myshop.adapter.HotWaresAdapter;
import com.jkkc.myshop.adapter.decoration.DividerItemDecoration;
import com.jkkc.myshop.bean.Page;
import com.jkkc.myshop.bean.Wares;
import com.jkkc.myshop.http.OkHttpHelper;
import com.jkkc.myshop.http.SpotsCallBack;
import com.squareup.okhttp.Response;

import java.util.List;

/**
 * Created by Guan on 2017/5/12.
 */

public class HotFragment extends Fragment {

    private OkHttpHelper mOkHttpHelper = OkHttpHelper.getInstance();
    private int currPage = 1;
    private int mTotalPage = 1;
    private int pageSize = 10;

    public static final int STATE_NORMAL = 0;
    public static final int STATE_REFRESH = 1;
    public static final int STATE_MORE = 2;

    private int state = STATE_NORMAL;

    private List<Wares> datas;
    private HotWaresAdapter mAdapter;


    private RecyclerView mRecyclerView;


    private MaterialRefreshLayout mRefreshLayout;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_hot, container, false);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);

        mRefreshLayout = (MaterialRefreshLayout) view.findViewById(R.id.refresh_view);

        initRefreshLayout();

        getData();

        return view;
    }


    private void initRefreshLayout() {

        mRefreshLayout.setLoadMore(true);
        mRefreshLayout.setMaterialRefreshListener(new MaterialRefreshListener() {
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
                    mRefreshLayout.finishRefreshLoadMore();
                }


            }


        });

    }

    private void refreshData() {

        currPage = currPage + 1;
        state = STATE_REFRESH;
        getData();


    }

    private void loadMoreData() {

        currPage = ++currPage;
        state = STATE_MORE;
        getData();


    }

    public void getData() {

        String url = Constants.API.WARES_HOT + "?curPage=" +
                currPage + "&pageSize=" + pageSize;

        mOkHttpHelper.get(url, new SpotsCallBack<Page<Wares>>(getContext()) {


            @Override
            public void onSuccess(Response response, Page<Wares> waresPage) {

                datas = waresPage.getList();
                currPage = waresPage.getCurrentPage();

                mTotalPage = waresPage.getTotalPage();

                showData();

            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }
        });


    }


    private void showData() {

        switch (state) {

            case STATE_NORMAL:

                mAdapter = new HotWaresAdapter(datas);


                mRecyclerView.setAdapter(mAdapter);
                mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                mRecyclerView.setItemAnimator(new DefaultItemAnimator());
                mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(),
                        DividerItemDecoration.VERTICAL_LIST));


                break;

            case STATE_REFRESH:

                mAdapter.clearData();
                mAdapter.addData(datas);
                mRecyclerView.scrollToPosition(0);

                mRefreshLayout.finishRefresh();
                break;

            case STATE_MORE:

                mAdapter.addData(mAdapter.getDatas().size(), datas);
                mRecyclerView.scrollToPosition(mAdapter.getDatas().size());

                mRefreshLayout.finishRefreshLoadMore();


                break;

        }


    }


}
