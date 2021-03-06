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

    private ArticleSearchParam mSearchParams = new ArticleSearchParam();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);


        populateArrayItems();
        setupToolbar();

        rvItems = binding.rvItems;
        lLayoutManager = new StaggeredGridLayoutManager(AdapterUtils.getColNm(this), StaggeredGridLayoutManager.VERTICAL);
        rvItems.setLayoutManager(lLayoutManager);
        rvItems.setAdapter(aToDoAdapter);
        scrollListener = new EndlessRecycleViewScrollListener(lLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                Log.d("on Load More", "page = " + page);
                readItems(page);
            }
        };
        // Adds the scroll listener to RecyclerView
        rvItems.addOnScrollListener(scrollListener);

        swipeContainer = binding.swipeContainer;
        swipeContainer.setOnRefreshListener(() -> {
            readItems();
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
        readItems();
    }

    //==== data operations
    private boolean readItems(int page) {
        mSearchParams.setPage(page);
        if (miClear != null) {
           Log.d("CLEAR MENU", "try set to " +  !mSearchParams.isEmpty());
            miClear.setVisible(!mSearchParams.isEmpty());
        }
        new NYTimesServiceImpl().getArticlesList(mSearchParams, getArticleListResponseCallback(page > 0));
        return true;
    }

    private boolean readItems() {
        return readItems(0);
    }

    private Callback<ArticlesResponse> getArticleListResponseCallback(final boolean nextPage) {
        return new Callback<ArticlesResponse>() {
            @Override
            public void onResponse(Call<ArticlesResponse> call, Response<ArticlesResponse> response) {
                ArticlesResponse res = response.body();
                if (!nextPage) {
                    articles.clear();
                }
                if (res != null && res.getResponse() != null && res.getResponse().getDocs() != null) {
                    articles.addAll(res.getResponse().getDocs());
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
    SearchView searchView;
    MenuItem miClear;
    MenuItem miAdvanced;
    MenuItem miSearchItem;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();

        inflater.inflate(R.menu.menu_main, menu);

        miClear = menu.findItem(R.id.miClear);
        miAdvanced = menu.findItem(R.id.miSearchItemAdvanced);
        miSearchItem = menu.findItem(R.id.miSearchItem);

        miClear.setVisible(false);
        miAdvanced.setVisible(true);

        searchView = (SearchView) MenuItemCompat.getActionView(miSearchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mSearchParams.resetPage();
                mSearchParams.setQ(query);
                readItems();
                searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        MenuItemCompat.setOnActionExpandListener(miSearchItem, new MenuItemCompat.OnActionExpandListener() {

            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {

                mSearchParams.setQ(null);
                Log.d("MENU OPTIONS", "expand");
                readItems();

                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                Log.d("MENU OPTIONS", "collapse");
                mSearchParams.setQ(null);
                readItems();
                return true;
            }
        });

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                Log.d("MENU OPTIONS", "close");
                mSearchParams.setQ(null);
                readItems();
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
            case R.id.miSearchItemAdvanced:
                showAdvancedSearchDialog();
                return false;
            case android.R.id.home:
                return super.onOptionsItemSelected(item);
            case R.id.miClear:
                mSearchParams.reset();
                readItems();
                searchView.setQuery(null, false);
                searchView.onActionViewCollapsed();
                return false;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    private void showAdvancedSearchDialog() {
        SearchAdvancedFragment dialog = SearchAdvancedFragment.newInstance((params) -> {
            mSearchParams = Parcels.unwrap(params);
            searchView.setQuery(mSearchParams.getQ(), false);
            MainActivity.this.onDataChanged()
            ;}, mSearchParams);
        dialog.show(getSupportFragmentManager(), Constants.FRAGMENT_SEARCH_ADVANCED);
    }

    private void onDataChanged() {
        readItems();
    }
}
