package com.pnlinh.mvpdemo.base;

/**
 * Created by pnLinh on 19/10/2017.
 */

public class BasePresenterImpl<T extends BaseView> implements BasePresenter<T> {
    protected T mView;

    @Override
    public void setView(T view) {
        mView = view;
    }
}
