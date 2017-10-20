package com.pnlinh.mvpdemo.base;

/**
 * Created by pnLinh on 19/10/2017.
 */

public interface BasePresenter<T extends BaseView> {
    void setView(T view);
}
