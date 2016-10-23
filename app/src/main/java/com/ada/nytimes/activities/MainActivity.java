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
import com.ada.nytimes.network.NYTimesServiceImpl;
import com.ada.nytimes.network.dto.articleSearch.ArticleSearchParam;
import com.ada.nytimes.network.dto.articleSearch.ArticlesResponse;
import com.ada.nytimes.network.dto.articleSearch.Doc;
import com.ada.nytimes.utils.Constants;
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

    //==== data opperations
    private boolean readItems() {
        new NYTimesServiceImpl().getArticlesList(mSearchParams, getArticleListResponseCallback());
        return true;
    }

    private Callback<ArticlesResponse> getArticleListResponseCallback() {
        return new Callback<ArticlesResponse>() {
            @Override
            public void onResponse(Call<ArticlesResponse> call, Response<ArticlesResponse> response) {
                ArticlesResponse reponse = response.body();
                articles.clear();
                articles.addAll(reponse.getResponse().getDocs());
                aToDoAdapter.notifyDataSetChanged();
                swipeContainer.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<ArticlesResponse> call, Throwable t) {
                //TODO change Toast to ???
                if (!Utils.isOnline()) {
                    Toast.makeText(MainActivity.this, R.string.network_connection_lost, Toast.LENGTH_LONG).show();
                }
                Log.d(this.getClass().getName(), "NEW Cannot load datax");
                swipeContainer.setRefreshing(false);
            }
        };
    }

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
                readItems();
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
        Log.d("MENU OPTIONS", "itemId = " + id + ", homeId = " + android.R.id.home);
        switch (id) {
            case android.R.id.home:
                mSearchParams.setQ(null);
                readItems();
                return super.onOptionsItemSelected(item);
            case R.id.miSearchItemAdvanced:

                return false;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
