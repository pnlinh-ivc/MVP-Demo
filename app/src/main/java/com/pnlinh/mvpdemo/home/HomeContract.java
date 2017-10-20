package com.pnlinh.mvpdemo.home;

import com.pnlinh.mvpdemo.base.BasePresenter;
import com.pnlinh.mvpdemo.base.BaseView;
import com.pnlinh.mvpdemo.model.WordData;

import java.util.List;

/**
 * Created by pnLinh on 19/10/2017.
 */

public interface HomeContract {
    interface ViewMethod extends BaseView {
        void showList(List<WordData> listData);
    }

    interface PresenterMethod extends BasePresenter<ViewMethod> {
        void getData(String query);

        void speak(String word);
    }
}
