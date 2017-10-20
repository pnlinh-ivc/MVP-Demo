package com.pnlinh.mvpdemo.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.pnlinh.mvpdemo.R;
import com.pnlinh.mvpdemo.model.WordData;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class AdapterRecyclerView extends RecyclerView.Adapter<AdapterRecyclerView.MyViewHolder> {

    private final String regexTranscribe = "/(.*?)/";
    private Pattern mPattern = Pattern.compile(regexTranscribe);
    private Matcher mMatcher;
    public abstract void onWordDataClick(WordData wordData);
    public abstract void onListenClick(String word);

    private ArrayList<WordData> listWord;
    Context context;
    private String keyword = "";

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public void clearAll() {
        listWord.clear();
        notifyDataSetChanged();
    }

    public AdapterRecyclerView(Context context, ArrayList<WordData> listWord) {
        this.listWord = listWord;
        this.context = context;
    }

    public String CH(String input) {
        return input.substring(0, 1).toUpperCase() + input.substring(1);
    }

    @Override
    public AdapterRecyclerView.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler_home_search, parent, false);
        return new MyViewHolder(v);
    }

    public String getSummaryText(String meaning) {

        String result = "";
        String Type = "";
        String subMean = "";

        int index1 = meaning.indexOf("@");
        if (index1 == -1)
            return meaning;
        int index2 = meaning.indexOf("*", index1 + 1);
        int index3 = meaning.indexOf("\\n", index1 + 1);
        int index4 = meaning.indexOf("#", index1 + 1);

        if (index4 == -1)
            return meaning;
        else {
            if (index2 != -1 && index2 < index4) {
                Type += meaning.substring(index2 + 1, index4 - 2);
            } else {
                if (index3 != -1) {
                    Type += meaning.substring(index1 + 1, index3);
                }
            }
        }

        int index5 = meaning.indexOf("\\n", index4 + 1);
        if (index5 == -1)
            subMean += meaning.substring(index4 + 1, meaning.length());
        else
            subMean += meaning.substring(index4 + 1, index5);


        result = CH(Type) + ": " + CH(subMean);
        return result;

    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final WordData wordData = listWord.get(position);
        holder.meaning.setText(getSummaryText(wordData.getMeaning()));


        SpannableString spannableString = new SpannableString(wordData.getTitle());
        Pattern pattern = Pattern.compile(keyword.toLowerCase());
        Matcher matcher = pattern.matcher(wordData.getTitle().toLowerCase());
        while (matcher.find()) {
            StyleSpan boldSpannable = new StyleSpan(Typeface.BOLD);
            spannableString.setSpan(new UnderlineSpan(), matcher.start(), matcher.end(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            spannableString.setSpan(boldSpannable, matcher.start(), matcher.end(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);

        }
        holder.title.setText(spannableString);

//        String arr[] = wordData.getMeaning().replace("\\n", "\n").split("\n");
//        for (String line : arr) {
//            if (!line.contains("*") && !line.contains("#")) {
//
//            }
//        }

        mMatcher = mPattern.matcher(wordData.getMeaning());
        String transcribe = "";
        while (mMatcher.find()) {
            String content = mMatcher.group();
            transcribe += content;
        }
        if (!TextUtils.isEmpty(transcribe)) {
            holder.transcribe.setText(transcribe);
            holder.transcribe.setVisibility(View.VISIBLE);
        } else
            holder.transcribe.setVisibility(View.GONE);
        holder.ivListen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onListenClick(wordData.getTitle());
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onWordDataClick(wordData);
            }
        });


    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return listWord.size();
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title, meaning, transcribe;
        ImageView ivListen;

        public MyViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.textViewItemTitle);
            transcribe = itemView.findViewById(R.id.tv_transcribe);
            meaning = itemView.findViewById(R.id.textViewItemContent);
            ivListen = itemView.findViewById(R.id.imListen);
        }
    }
}
