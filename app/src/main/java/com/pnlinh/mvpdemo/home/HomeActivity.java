package com.pnlinh.mvpdemo.home;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;

import com.pnlinh.mvpdemo.R;
import com.pnlinh.mvpdemo.adapter.AdapterRecyclerView;
import com.pnlinh.mvpdemo.animation.ViewAnimator;
import com.pnlinh.mvpdemo.base.BaseActivity;
import com.pnlinh.mvpdemo.detail.DetailActivity;
import com.pnlinh.mvpdemo.model.WordData;
import com.pnlinh.mvpdemo.widget.RecyclerItemDecoration;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends BaseActivity<HomeContract.PresenterMethod> implements HomeContract.ViewMethod {
    private static final String TAG = HomeActivity.class.getSimpleName();

    public static void start(Context context) {
        Intent starter = new Intent(context, HomeActivity.class);
        context.startActivity(starter);
    }


    ArrayList<WordData> listWord;
    RecyclerView recyclerView;
    AdapterRecyclerView adapter;
    LinearLayoutManager mLayoutManager;
    Drawable icClear;
    private ImageView ivClear;
    private AppCompatEditText edtSearch;

    @Override
    public HomeContract.PresenterMethod getPresenter() {
        return new HomePresenterImpl();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initView();
        initRecyclerView();
        mPresenter.getData("");
    }

    @Override
    public void showList(List<WordData> list) {
        listWord.clear();
        listWord.addAll(list);
        adapter.notifyDataSetChanged();

    }

    private void initView() {
        icClear = getWrappedDrawable(this, R.drawable.ic_clear_black_24dp);
        ivClear = (ImageView) findViewById(R.id.iv_clear);
        DrawableCompat.setTint(icClear, Color.parseColor("#787878"));
        ivClear.setImageDrawable(icClear);
        ivClear.setAlpha(0f);
        edtSearch = (AppCompatEditText) findViewById(R.id.edt_search);

        ivClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtSearch.setText("");
            }
        });
        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0 && ivClear.getAlpha() == 0) {
                    ViewAnimator.animate(ivClear).alpha(0f, 1f).duration(300).start();
                }
                if (s.length() == 0)
                    ViewAnimator.animate(ivClear).alpha(1f, 0f).duration(300).start();
                String search = s.toString();
                adapter.setKeyword(search);
                mPresenter.getData(search);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }

    private void initRecyclerView() {
        recyclerView = (RecyclerView) findViewById(R.id.rcl_words);
        listWord = new ArrayList<>();
        adapter = new AdapterRecyclerView(this, listWord) {
            @Override
            public void onWordDataClick(WordData wordData) {
                DetailActivity.start(HomeActivity.this, wordData);
            }

            @Override
            public void onListenClick(String word) {
                mPresenter.speak(word);
            }
        };
        mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration((new RecyclerItemDecoration(this, LinearLayoutManager.VERTICAL)));
        recyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideKeyboard(v);
                return false;
            }
        });
    }

    private static Drawable getWrappedDrawable(Context context, @DrawableRes int resId) throws Resources.NotFoundException {
        return DrawableCompat.wrap(ResourcesCompat.getDrawable(context.getResources(),
                resId, null));
    }

    private void hideKeyboard(View v) {
        InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }
}
