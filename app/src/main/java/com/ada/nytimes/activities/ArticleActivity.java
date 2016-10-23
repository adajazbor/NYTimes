package com.ada.nytimes.activities;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.ada.nytimes.R;
import com.ada.nytimes.databinding.ActivityArticleBinding;
import com.ada.nytimes.network.dto.articleSearch.Doc;
import com.ada.nytimes.utils.Constants;

import org.parceler.Parcels;

public class ArticleActivity extends AppCompatActivity {

    private Doc mItem;

    ActivityArticleBinding binding;
    WebView wvArticle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_article);

        mItem = Parcels.unwrap(getIntent().getParcelableExtra(Constants.PARAM_ITEM));

        binding.setArticle(mItem);
        wvArticle = binding.wvArticle;
        wvArticle.getSettings().setLoadsImagesAutomatically(true);
        wvArticle.getSettings().setJavaScriptEnabled(true);
        wvArticle.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        wvArticle.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        wvArticle.loadUrl(mItem.getWebUrl());
    }
}
