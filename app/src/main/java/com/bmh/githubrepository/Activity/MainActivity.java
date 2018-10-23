package com.bmh.githubrepository.Activity;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.DialogFragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.bmh.githubrepository.Adapter.OnRecyclerItemClickListener;
import com.bmh.githubrepository.Adapter.RepoAdapter;
import com.bmh.githubrepository.Model.Repo;
import com.bmh.githubrepository.Service.RefreshService;
import com.bmh.githubrepository.Tools.LoadingRepoPages;
import com.bmh.githubrepository.Tools.StaticValues;
import com.bmh.githubrepository.UI.Dialogs;
import com.bmh.githubrepository.UI.DialogsInterface;
import com.bmh.githubrepository.WebService.RepoRetrofit;
import com.bmh.recycle.R;

public class MainActivity extends AppCompatActivity implements OnRecyclerItemClickListener, DialogsInterface, SwipeRefreshLayout.OnRefreshListener {
    private EditText etSearch;
    private Repo repo;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private LinearLayoutManager linearLayoutManager;
    private SwipeRefreshLayout swipeRefreshLayout;

    private RepoAdapter repoAdapter;
    private LoadingRepoPages loadingRepoPages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initComponents();
        //refresh service that
        startService(new Intent(this,RefreshService.class));
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
}
