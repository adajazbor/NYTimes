package com.ada.nytimes.activities;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.ada.nytimes.R;
import com.ada.nytimes.adapters.AdapterUtils;
import com.ada.nytimes.adapters.ArticleAdapter;
import com.ada.nytimes.databinding.ActivityMainBinding;
import com.ada.nytimes.fragments.SearchAdvancedFragment;
import com.ada.nytimes.network.NYTimesServiceImpl;
import com.ada.nytimes.network.dto.articleSearch.ArticleSearchParam;
import com.ada.nytimes.network.dto.articleSearch.ArticlesResponse;
import com.ada.nytimes.network.dto.articleSearch.Doc;
import com.ada.nytimes.utils.Constants;
import com.ada.nytimes.utils.EndlessRecycleViewScrollListener;
import com.ada.nytimes.utils.Utils;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private List<Doc> articles = new ArrayList<>();
    private ArticleAdapter aToDoAdapter;
    private EndlessRecycleViewScrollListener scrollListener;

    private RecyclerView rvItems;
    private Toolbar toolbar;
    private StaggeredGridLayoutManager lLayoutManager;
    private SwipeRefreshLayout swipeContainer;
    private ActivityMainBinding binding;

    private ArticleSearchParam mSearchParams;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        mSearchParams = new ArticleSearchParam();
        populateArrayItems();
        setupToolbar();

        rvItems = binding.rvItems;
        lLayoutManager = new StaggeredGridLayoutManager(AdapterUtils.getColNm(this), StaggeredGridLayoutManager.VERTICAL);
        rvItems.setLayoutManager(lLayoutManager);
        rvItems.setAdapter(aToDoAdapter);
        scrollListener = new EndlessRecycleViewScrollListener(lLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                mSearchParams.increasePage();
                readItems(true);
            }
/*
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
                mSearchParams.increasePage();
                loadNextDataFromApi(page);
            }
            */
        };
        // Adds the scroll listener to RecyclerView
        rvItems.addOnScrollListener(scrollListener);

        swipeContainer = binding.swipeContainer;
        swipeContainer.setOnRefreshListener(() -> {
            readItems(false);
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

    }

    private void setupToolbar() {
        //TODO can I use binding for include
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    public void populateArrayItems() {
        aToDoAdapter = new ArticleAdapter(
                this,
                articles,
                new ArticleAdapter.ItemArrayAdapterDelegate() {
                    @Override
                    public boolean onLongClick(int position) {
                        return true;
                    }

                    @Override
                    public void onClick(int position) {
                        Intent i = new Intent(MainActivity.this, ArticleActivity.class);
                        i.putExtra(Constants.PARAM_ITEM, Parcels.wrap(articles.get(position)));
                        startActivity(i);
                    }
                });
        readItems(false);
    }

    //==== data opperations
    private boolean readItems(boolean nextPage) {
        new NYTimesServiceImpl().getArticlesList(mSearchParams, getArticleListResponseCallback(nextPage));
        return true;
    }

    private Callback<ArticlesResponse> getArticleListResponseCallback(boolean nextPage) {
        return new Callback<ArticlesResponse>() {
            @Override
            public void onResponse(Call<ArticlesResponse> call, Response<ArticlesResponse> response) {
                ArticlesResponse reponse = response.body();
                if (!nextPage) {
                    articles.clear();
                }
                if (response != null && reponse.getResponse() != null && reponse.getResponse().getDocs() != null) {
                    articles.addAll(reponse.getResponse().getDocs());
                    aToDoAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(MainActivity.this, R.string.no_results_info, Toast.LENGTH_LONG);
                }
                swipeContainer.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<ArticlesResponse> call, Throwable t) {
                //TODO change Toast to ???
                if (!Utils.isOnline()) {
                    Toast.makeText(MainActivity.this, R.string.network_connection_lost, Toast.LENGTH_LONG).show();
                }
                Log.d(this.getClass().getName(), "NEW Cannot load data");
                swipeContainer.setRefreshing(false);
            }
        };
    }

    //================ menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();

        inflater.inflate(R.menu.menu_main, menu);
        MenuItem searchItem = menu.findItem(R.id.miSearchItem);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mSearchParams.resetPage();
                mSearchParams.setQ(query);
                readItems(false);
                searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        MenuItemCompat.setOnActionExpandListener(searchItem, new MenuItemCompat.OnActionExpandListener() {

            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                /*
                mSearchParams.setQ(null);
                Log.d("MENU OPTIONS", "expand");
                readItems(false);
                */
                return false;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                Log.d("MENU OPTIONS", "collapse");
                mSearchParams.setQ(null);
                readItems(false);
                return true;
            }
        });

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                Log.d("MENU OPTIONS", "close");
                mSearchParams.setQ(null);
                readItems(false);
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        Log.d("MENU OPTIONS", "itemId = " + id + ", advanced = " + android.R.id.home);
        switch (id) {
            case android.R.id.home:
                mSearchParams.setQ(null);
                readItems(false);
                return super.onOptionsItemSelected(item);
            case R.id.miSearchItemAdvanced:
                showAdvancedSearchDialog();
                return false;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    private void showAdvancedSearchDialog() {
        SearchAdvancedFragment dialog = SearchAdvancedFragment.newInstance((params) -> {
            mSearchParams = Parcels.unwrap(params);
            MainActivity.this.onDataChanged()
            ;}, mSearchParams);
        dialog.show(getSupportFragmentManager(), Constants.FRAGMENT_SEARCH_ADVANCED);
    }

    private void onDataChanged() {
        readItems(false);
    }
}
