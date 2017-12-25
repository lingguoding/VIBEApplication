package com.hao.common.base;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.hao.common.R;
import com.hao.common.adapter.BaseRecyclerViewAdapter;
import com.hao.common.adapter.OnRVItemClickListener;
import com.hao.common.exception.ErrorMessageFactory;
import com.hao.common.nucleus.presenter.LoadPresenter;
import com.hao.common.nucleus.view.loadview.ILoadPageListDataView;
import com.hao.common.utils.ToastUtil;
import com.hao.common.utils.ViewUtils;
import com.hao.common.widget.LoadingLayout;
import com.hao.common.widget.xrecyclerview.XRecyclerView;

import java.util.List;

/**
 * @Package com.hao.common.base
 * @作 用:
 * @创 建 人: 林国定 邮箱：linggoudingg@gmail.com
 * @日 期: 2017/3/18 0018
 */

public abstract class BaseLoadFragment<T> extends BaseFragment<LoadPresenter> implements ILoadPageListDataView<T>,
        SwipeRefreshLayout.OnRefreshListener, LoadingLayout.OnReloadListener, OnRVItemClickListener, XRecyclerView.LoadingListener {
    protected LoadingLayout mLoadingLayout;
    protected RecyclerView mRecyclerView;
    protected SwipeRefreshLayout mRefreshLayout;
    protected BaseRecyclerViewAdapter<T> mAdapter;

    @Override
    public int getRootLayoutResID() {
        return R.layout.layout_base_load;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        mLoadingLayout = getViewById(R.id.loading_layout);
        mRecyclerView = getViewById(R.id.recycler_view);
        mRefreshLayout = getViewById(R.id.swipe_refresh_layout);
        mRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary), getResources().getColor(R.color.colorAccent));

        ViewUtils.initVerticalLinearRecyclerView(getContext(), mRecyclerView);
        createAdapter();
        //mRecyclerView.addItemDecoration(BaseDivider.newBitmapDivider());
        mRecyclerView.setAdapter(mAdapter);
        showLoadingView();
    }

    protected abstract void createAdapter();


    @Override
    protected void setListener() {
        mRefreshLayout.setOnRefreshListener(this);
        mLoadingLayout.setOnReloadListener(this);
        mAdapter.setOnRVItemClickListener(this);
        if (mRecyclerView instanceof XRecyclerView) {
            ((XRecyclerView) mRecyclerView).setLoadingListener(this);
        }
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {

    }

    @Override
    public void onRefreshDataToUI(List<T> ms) {
        if (mAdapter != null) {
            mAdapter.setData(ms);
            if (mRecyclerView instanceof XRecyclerView) {
                ((XRecyclerView) mRecyclerView).resetLoadMore();
            }
        }
    }

    @Override
    public void onLoadMoreDataToUI(List<T> ms) {
        if (mAdapter != null)
            mAdapter.addMoreData(ms);
    }


    @Override
    public void onRefreshComplete() {
        if (mRefreshLayout != null)
            mRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onLoadComplete() {
        if (mRecyclerView instanceof XRecyclerView) {
            XRecyclerView xRecyclerView = (XRecyclerView) mRecyclerView;
            xRecyclerView.loadMoreComplete();
            if (mRefreshLayout != null)
                mRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void onNoDate() {
        if (mRefreshLayout != null)
            mRefreshLayout.setRefreshing(false);

        if(mAdapter.getData()!=null&&mAdapter.getData().size()!=0){
            mAdapter.clear();
        }
        ToastUtil.show("暂无数据");
    }

    @Override
    public void onNoMoreLoad() {
        if (mRecyclerView instanceof XRecyclerView) {
            XRecyclerView xRecyclerView = (XRecyclerView) mRecyclerView;
            xRecyclerView.noMoreLoading();
            if (mRefreshLayout != null)
                mRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public int getTotalItem() {
        return mAdapter.getItemCount();
    }

    @Override
    public void showLoadingView() {
        if (mLoadingLayout != null)
            mLoadingLayout.setStatus(LoadingLayout.Loading);
    }

    @Override
    public void showContentView() {
        if (mLoadingLayout != null)
            mLoadingLayout.setStatus(LoadingLayout.Success);
    }

    @Override
    public void showEmptyView() {
        if (mLoadingLayout != null)
            mLoadingLayout.setStatus(LoadingLayout.Empty);
    }

    @Override
    public void showFailView() {
        if (mLoadingLayout != null)
            mLoadingLayout.setStatus(LoadingLayout.Error);
    }

    @Override
    public void showError(String message) {
        ToastUtil.show(message);
    }

    @Override
    public Context getContext() {
        return getBaseActivity();
    }


    @Override
    public void onReload(View v) {

    }

    @Override
    public void onRVItemClick(ViewGroup parent, View itemView, int position) {

    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void onLoadMore() {

    }
}
