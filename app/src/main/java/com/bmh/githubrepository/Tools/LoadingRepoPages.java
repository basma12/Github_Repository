package com.bmh.githubrepository.Tools;

import android.app.Activity;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.EditText;
import android.widget.Toast;

import com.bmh.githubrepository.Adapter.PaginationScrollListener;
import com.bmh.githubrepository.Adapter.RepoAdapter;
import com.bmh.githubrepository.Model.Repo;
import com.bmh.githubrepository.UI.SearchTextWatcher;
import com.bmh.githubrepository.WebService.RepoAPI;
import com.bmh.githubrepository.WebService.RepoRetrofit;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoadingRepoPages {
    private RepoAdapter repoAdapter;
    private Activity activity;
    private EditText etSearch;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private SwipeRefreshLayout swipeRefreshLayout;
    private NetworkState networkState;

    private boolean isLoading = false;
    private boolean isLastPage = false;
    private int currentPage = StaticValues.PAGE_START;

    public LoadingRepoPages(Activity activity, RepoAdapter repoAdapter, RecyclerView recyclerView, EditText etSearch, LinearLayoutManager linearLayoutManager, SwipeRefreshLayout swipeRefreshLayout) {
        this.activity = activity;
        this.repoAdapter = repoAdapter;
        this.recyclerView = recyclerView;
        this.etSearch = etSearch;
        this.linearLayoutManager = linearLayoutManager;
        this.swipeRefreshLayout = swipeRefreshLayout;
        networkState = new NetworkState(activity);
    }

    public void loadFirstPage(int pageNumber) {
        RepoAPI repoAPI = new RepoRetrofit(activity).getRepoAPI();
        Call<List<Repo>> call = repoAPI.loadRepo(StaticValues.ACCESS_TOKEN, StaticValues.PAGES_SIZE, pageNumber);
        call.enqueue(new Callback<List<Repo>>() {

            @Override
            public void onResponse(Call<List<Repo>> call, Response<List<Repo>> response) {
                if (response.isSuccessful()) {
                    List<Repo> repoList = response.body();
                    repoAdapter.setRepos(repoList);
                    recyclerView.setAdapter(repoAdapter);

                    recyclerView.addOnScrollListener(new PaginationScrollListener(linearLayoutManager) {
                        @Override
                        protected void loadMoreItems() {
                            isLoading = true;
                            currentPage += 1;

                            // mocking network delay for API call
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    loadNextPage();
                                }
                            }, 1000);
                        }

                        @Override
                        public int getTotalPageCount() {
                            return StaticValues.TOTAL_PAGES;
                        }

                        @Override
                        public boolean isLastPage() {
                            return isLastPage;
                        }

                        @Override
                        public boolean isLoading() {
                            return isLoading;

                        }
                    });

                    //here the adapter is filled, so we can add text Watcher to our edit text
                    new SearchTextWatcher(etSearch, repoAdapter);

                }else {
                    if(!networkState.hasNetwork()){
                        recyclerView.setAdapter(repoAdapter);
                        Toast.makeText(activity,"No Internet Available",Toast.LENGTH_LONG).show();
                    }
                }
                swipeRefreshLayout.setRefreshing(false);

            }

            @Override
            public void onFailure(Call<List<Repo>> call, Throwable t) {
            }
        });
    }

    private void loadNextPage() {

        RepoAPI repoAPI = new RepoRetrofit(activity).getRepoAPI();
        Call<List<Repo>> call = repoAPI.loadRepo(StaticValues.ACCESS_TOKEN, StaticValues.PAGES_SIZE, currentPage);
        call.enqueue(new Callback<List<Repo>>() {
            @Override
            public void onResponse(Call<List<Repo>> call, Response<List<Repo>> response) {
                if (response.isSuccessful()) {

                    repoAdapter.removeLoadingFooter();
                    isLoading = false;

                    List<Repo> repoList = response.body();
                    repoAdapter.addAll(repoList);

                    if (currentPage != StaticValues.TOTAL_PAGES) repoAdapter.addLoadingFooter();
                    else isLastPage = true;
                }
                else {
                    if(!networkState.hasNetwork()){
                        Toast.makeText(activity,"No Internet Available",Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Repo>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}
