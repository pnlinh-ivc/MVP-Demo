package com.pnlinh.mvpdemo.home;

import android.content.Context;
import android.content.res.Resources;
import android.os.Handler;
import android.os.Looper;
import android.speech.tts.TextToSpeech;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pnlinh.mvpdemo.R;
import com.pnlinh.mvpdemo.base.BasePresenterImpl;
import com.pnlinh.mvpdemo.base.BaseView;
import com.pnlinh.mvpdemo.model.WordData;

import org.apache.commons.io.IOUtils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by pnLinh on 19/10/2017.
 */

public class HomePresenterImpl extends BasePresenterImpl<HomeContract.ViewMethod> implements HomeContract.PresenterMethod {

    private static final String TAG = HomePresenterImpl.class.getSimpleName();
    private Handler mHandler = new Handler(Looper.getMainLooper());
    private TextToSpeech mTextToSpeech;

    public HomePresenterImpl() {
        super();
    }

    @Override
    public void getData(String query) {

        new Thread(new GetDataRunnable(query)).start();
    }

    @Override
    public void speak(String word) {
        try {
            Log.e(TAG, String.format("speak: "));
            if (mTextToSpeech == null)
                mTextToSpeech = new TextToSpeech(((Context) mView), null);
            mTextToSpeech.setLanguage(Locale.US);
            mTextToSpeech.speak(word, TextToSpeech.QUEUE_ADD, null);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private class GetDataRunnable implements Runnable {
        String query;

        public GetDataRunnable(String query) {
            this.query = query;
        }
//onther code
        @Override
        public void run() {
            try {
                Resources res = ((Context) mView).getResources();
                InputStream inputStream = res.openRawResource(R.raw.data);
                String data = IOUtils.toString(inputStream);

                Gson gson = new Gson();
                List<WordData> listData = gson.fromJson(data, new TypeToken<List<WordData>>() {
                }.getType());
                final List<WordData> result = new ArrayList<>();

                for (WordData wordData : listData) {
                    if (wordData.getTitle().toLowerCase().contains(query.toLowerCase()))
                        result.add(wordData);
                }
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mView.showList(result);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
