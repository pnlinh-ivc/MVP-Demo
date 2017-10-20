package com.pnlinh.mvpdemo.detail;

import com.pnlinh.mvpdemo.base.BasePresenter;
import com.pnlinh.mvpdemo.base.BaseView;

/**
 * Created by pnlinh on 10/20/2017.
 */

public interface DetailContract {
    interface ViewMethod extends BaseView {
        void showData(String tring);
        void setLike(boolean isLike);
    }

    interface PresenterMethod extends BasePresenter<ViewMethod> {
        void checkIsLike(int id);

        void toggleLike(int id);
    }
}
