package com.bmh.githubrepository.Activity;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.bmh.githubrepository.Adapter.OnRecyclerItemClickListener;
import com.bmh.githubrepository.Adapter.RepoAdapter;
import com.bmh.githubrepository.Model.AccessToken;
import com.bmh.githubrepository.Model.Repo;
import com.bmh.githubrepository.R;
import com.bmh.githubrepository.Service.RefreshService;
import com.bmh.githubrepository.Tools.LoadingRepoPages;
import com.bmh.githubrepository.Tools.StaticValues;
import com.bmh.githubrepository.UI.Dialogs;
import com.bmh.githubrepository.UI.DialogsInterface;
import com.bmh.githubrepository.WebService.RepoAPI;
import com.bmh.githubrepository.WebService.RepoRetrofit;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements OnRecyclerItemClickListener, DialogsInterface, SwipeRefreshLayout.OnRefreshListener, Callback<AccessToken> {
    private EditText etSearch;
    private Repo repo;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private LinearLayoutManager linearLayoutManager;
    private SwipeRefreshLayout swipeRefreshLayout;

    private RepoAdapter repoAdapter;
    private LoadingRepoPages loadingRepoPages;
    private String code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("https://github.com/login/oauth/authorize?client_id=" + StaticValues.CLIENT_ID
                        + "&redirect_uri=" + StaticValues.REDIRECT_URI));
        startActivity(intent);
        initComponents();
        //refresh service that
        startService(new Intent(this, RefreshService.class));
    }

    private void initComponents() {
        progressBar = findViewById(R.id.main_progress);
        recyclerView = findViewById(R.id.recycle_view);
        etSearch = findViewById(R.id.etSearch);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Uri uri = getIntent().getData();
        if (uri != null && uri.toString().startsWith(StaticValues.REDIRECT_URI)) {
            code = uri.getQueryParameter("code");
            RepoAPI repoAPI = new RepoRetrofit(this).getRepoAPI();
            Call<AccessToken> call = repoAPI.getAccessToken(StaticValues.CLIENT_ID, StaticValues.CLIENT_SECRET, code);
            call.enqueue(this);
        }

        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        repoAdapter = new RepoAdapter(this);

        loadingRepoPages = new LoadingRepoPages(this, repoAdapter, recyclerView, etSearch, linearLayoutManager, swipeRefreshLayout);
        loadingRepoPages.loadFirstPage(1);
        swipeRefreshLayout.setOnRefreshListener(this);
    }

    @Override
    public void onRecyclerItemClick(Repo repo) {
        this.repo = repo;
        Dialogs dialogs = Dialogs.newInstance("Do you want to go repository or owner repository", StaticValues.DIALOG_TYPE_YES_NO);
        dialogs.show(getSupportFragmentManager(), "dialog");
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        //go to owner repository
        openBrowser(repo.getOwner().getOwnerURL());
        dialog.dismiss();
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        //go to repository
        openBrowser(repo.getHTML_URL());
        dialog.dismiss();

    }

    private void openBrowser(String url) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(browserIntent);
    }


    @Override
    public void onRefresh() {
        RepoRetrofit.clearCache();
        loadingRepoPages.loadFirstPage(1);
        onResume();
    }

    @Override
    public void onResponse(@NonNull Call<AccessToken> call, @NonNull Response<AccessToken> response) {
        if (response.isSuccessful()) {
            if(response.body() != null) {
                AccessToken accessToken=   response.body();
                if(accessToken !=null) {
                    StaticValues.ACCESS_TOKEN =accessToken.getAccessToken();
                }

            }
        }
    }

    @Override

    public void onFailure(@NonNull Call<AccessToken> call, @NonNull Throwable  t) {

    }
}
