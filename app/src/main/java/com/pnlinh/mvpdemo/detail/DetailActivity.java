package com.pnlinh.mvpdemo.detail;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.pnlinh.mvpdemo.R;
import com.pnlinh.mvpdemo.base.BaseActivity;
import com.pnlinh.mvpdemo.home.HomeActivity;
import com.pnlinh.mvpdemo.model.WordData;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DetailActivity extends BaseActivity<DetailContract.PresenterMethod> implements DetailContract.ViewMethod, View.OnClickListener {
    private static final String TAG = DetailActivity.class.getSimpleName();
    private static final String BUNDLE_WORD_DATA = "word_data";

    private TextView tvWord, tvTranscribe;
    private LinearLayout llMeaning;
    private AppCompatImageView ivFavourite;

    private final String regexHashTag = "#(.*?)(\\n|$)";
    private final String regexStart = "\\*(.*?)(\\n|$)";
    private final String regexTranscribe = "/(.*?)/";
    private Pattern pattern = Pattern.compile(regexHashTag);
    private Matcher mMatcher;

    private WordData mWordData;

    @Override
    public DetailContract.PresenterMethod getPresenter() {
        return new DetailPresenterImpl();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        initToolbar();
        parseIntent();
        initView();
        bindMeaning();
        mPresenter.checkIsLike(Integer.valueOf(mWordData.getId()));
    }

    @Override
    public void showData(String tring) {

    }

    @Override
    public void setLike(boolean isLike) {
        ivFavourite.setSelected(isLike);
    }

    public static void start(Context context, WordData wordData) {
        Intent starter = new Intent(context, DetailActivity.class);
        starter.putExtra(BUNDLE_WORD_DATA, wordData);
        context.startActivity(starter);
    }

    private void parseIntent() {
        Intent intentPass = getIntent();
        if (intentPass != null) {
            mWordData = intentPass.getParcelableExtra(BUNDLE_WORD_DATA);
            if (mWordData == null)
                stopActivity();
        } else
            stopActivity();
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle("");
            toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isTaskRoot()) {
                        startActivity(new Intent(DetailActivity.this, HomeActivity.class));
                        finish();
                    } else finish();
                }
            });
        }
    }

    private void initView() {
        tvWord = (TextView) findViewById(R.id.tv_word);
        llMeaning = (LinearLayout) findViewById(R.id.ll_meaning);
        ivFavourite = (AppCompatImageView) findViewById(R.id.iv_favourite);
        tvTranscribe = (TextView) findViewById(R.id.tv_transcribe);
        ivFavourite.setOnClickListener(this);
        tvWord.setText(fromHtml(mWordData.getTitle()));

        if (ivFavourite != null) {
            VectorDrawableCompat vcFavourite = VectorDrawableCompat.create(getResources(), R.drawable.ic_favorite, getTheme());
            VectorDrawableCompat vcFavouriteBorder = VectorDrawableCompat.create(getResources(), R.drawable.ic_favorite_border, getTheme());
            StateListDrawable stateList = new StateListDrawable();
            stateList.addState(new int[]{android.R.attr.state_selected}, vcFavourite);
            stateList.addState(new int[]{}, vcFavouriteBorder);
            ivFavourite.setImageDrawable(stateList);
        }
    }

    private void bindMeaning() {
        String meaning = mWordData.getMeaning();
        meaning = meaning.replace("\\n", "\n");
        meaning.replaceAll("@" + mWordData.getTitle(), "");

        String arr[] = meaning.split("\n");
        SpannableStringBuilder ssb = new SpannableStringBuilder();
        for (int i = 0; i < arr.length; i++) {
            String line = arr[i];
            LayoutInflater inflater = LayoutInflater.from(this);
            if (!TextUtils.isEmpty(line)) {
                line = line.trim();
                if (line.contains("*")) {

                    TextView tvContent = (TextView) inflater.inflate(R.layout.content_start, llMeaning, false);
                    tvContent.setText(UpperFirstChar(line.substring(1, line.length()).trim()));
                    llMeaning.addView(tvContent);
                } else {
                    if (line.contains("#")) {
                        View view = inflater.inflate(R.layout.content_hashtag, llMeaning, false);
                        ((TextView) view.findViewById(R.id.tv_hash_tag)).setText(line.substring(1, line.length()).trim());
                        llMeaning.addView(view);
                    } else {
                        if (line.contains("/")) {
                            String transcribe = "";
                            pattern = Pattern.compile(regexTranscribe);
                            mMatcher = pattern.matcher(line);
                            boolean isAddNewLine = false;
                            while (mMatcher.find()) {
                                String content = mMatcher.group();
                                if (isAddNewLine)
                                    transcribe += "\n" + content;
                                else {
                                    transcribe += content;
                                    isAddNewLine = true;
                                }
                            }
                            if (!TextUtils.isEmpty(transcribe)) {
                                tvTranscribe.setText(transcribe);
                                tvTranscribe.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                }

            }

        }
    }

    private void stopActivity() {
        Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
        finish();
        return;
    }

    private Spanned fromHtml(String source) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return Html.fromHtml(source, Html.FROM_HTML_MODE_LEGACY);
        } else {
            return Html.fromHtml(source);
        }
    }

    private String UpperFirstChar(String input) {
        return input.substring(0, 1).toUpperCase() + input.substring(1);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_favourite:
                mPresenter.toggleLike(Integer.valueOf(mWordData.getId()));
                break;
        }
    }
}
