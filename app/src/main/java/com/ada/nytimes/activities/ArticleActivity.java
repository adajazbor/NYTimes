package com.ada.nytimes.activities;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
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
    private ShareActionProvider miShareAction;
    private Toolbar toolbar;

    ActivityArticleBinding binding;
    WebView wvArticle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_article);
        setupToolbar();

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

    private void setupToolbar() {
        //TODO can I use binding for include
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        MenuItem item = menu.findItem(R.id.menu_item_share);

        miShareAction = (ShareActionProvider) MenuItemCompat.getActionProvider(item);
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, wvArticle.getUrl());

        miShareAction.setShareIntent(shareIntent);
        return super.onCreateOptionsMenu(menu);
    }
}
