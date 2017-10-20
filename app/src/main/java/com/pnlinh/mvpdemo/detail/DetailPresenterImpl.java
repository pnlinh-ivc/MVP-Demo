package com.pnlinh.mvpdemo.detail;

import com.pnlinh.mvpdemo.MyApplication;
import com.pnlinh.mvpdemo.base.BasePresenterImpl;

/**
 * Created by pnlinh on 10/20/2017.
 */

public class DetailPresenterImpl extends BasePresenterImpl<DetailContract.ViewMethod> implements DetailContract.PresenterMethod {

    @Override
    public void checkIsLike(int id) {
        boolean isLike = isWordLiked(id);
        mView.setLike(isLike);
    }

    @Override
    public void toggleLike(int id) {
        boolean isLike = isWordLiked(id);
        if (isLike) {
            dislikeWord(id);
            mView.setLike(false);
        } else {
            likeWord(id);
            mView.setLike(true);
        }
    }

    private void likeWord(int id) {
        MyApplication.getInstance().getSharedPrefsHelper().put(String.valueOf(id), true);
    }

    private boolean isWordLiked(int id) {
        return MyApplication.getInstance().getSharedPrefsHelper().contains(id);
    }

    private void dislikeWord(int id) {
        MyApplication.getInstance().getSharedPrefsHelper().remove(id);
    }
}
