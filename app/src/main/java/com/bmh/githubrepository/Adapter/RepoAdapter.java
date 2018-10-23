package com.bmh.githubrepository.Adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bmh.githubrepository.Model.Repo;
import com.bmh.githubrepository.Tools.StaticValues;
import com.bmh.recycle.R;

import java.util.ArrayList;
import java.util.List;

public class RepoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {

    private OnRecyclerItemClickListener onRecyclerItemClickListener;
    private List<Repo> data;
    private List<Repo> dataFiltered;
    private ItemFilter mFilter = new ItemFilter();
    private boolean isLoadingAdded = false;


    Activity activity;

    public RepoAdapter(Activity activity) {
        this.activity = activity;
        data=new ArrayList<>();
        this.onRecyclerItemClickListener= (OnRecyclerItemClickListener) activity;
    }
    public List<Repo> getRepos() {
        return data;
    }

    public void setRepos(List<Repo> repoList) {

        this.data = repoList;
        this.dataFiltered = data;

    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        switch (viewType) {
            case StaticValues.ITEM:
                View view = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.adapter_repo, viewGroup, false);
                viewHolder = new RepoViewHolder(view, onRecyclerItemClickListener);
                break;
            case StaticValues.LOADING:
                View view2 = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_progress, viewGroup, false);
                viewHolder = new LoadingVH(view2);
                break;
        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        switch (getItemViewType(position)) {
            case StaticValues.ITEM:
                ((RepoViewHolder) viewHolder).setData((Repo) data.get(position));
                break;
            case StaticValues.LOADING:
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        return (position == data.size() - 1 && isLoadingAdded) ? StaticValues.LOADING : StaticValues.ITEM;
    }

    @Override
    public int getItemCount() {
        if(dataFiltered!=null)
        return dataFiltered.size();
        return 0;
    }

    public Filter getFilter() {
        return mFilter;
    }

    public void add(Repo repo) {
        data.add(repo);
        notifyItemInserted(data.size() - 1);
    }
    public void addAll(List<Repo> repoList) {
        for (Repo repo : repoList) {
            add(repo);
        }
    }
    public void remove(Repo repo) {
        int position = data.indexOf(repo);
        if (position > -1) {
            data.remove(position);
            notifyItemRemoved(position);
        }
    }
    public void clear() {
        isLoadingAdded = false;
        while (getItemCount() > 0) {
            remove(getItem(0));
        }
    }
    public boolean isEmpty() {
        return getItemCount() == 0;
    }
    public void addLoadingFooter() {
        isLoadingAdded = true;
        add(new Repo());
    }
    public void removeLoadingFooter() {
        isLoadingAdded = false;

        int position = data.size() - 1;
        Repo repo = getItem(position);

        if (repo != null) {
            data.remove(position);
            notifyItemRemoved(position);
        }
    }

    public Repo getItem(int position) {
        return data.get(position);
    }
    protected class RepoViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {
        public TextView tvRepoName, tvDescription, tvUserName;
        public View view;
        public LinearLayout layout;
        private OnRecyclerItemClickListener onRecyclerItemClickListener;
        private Repo repo;

        public RepoViewHolder(View view, OnRecyclerItemClickListener onRecyclerItemClickListener) {
            super(view);
            this.view = view;
            tvRepoName = (TextView) view.findViewById(R.id.tvRepoName);
            tvDescription = (TextView) view.findViewById(R.id.tvDescription);
            tvUserName = (TextView) view.findViewById(R.id.tvUserName);
            layout = (LinearLayout) view.findViewById(R.id.layoutMain);

            this.onRecyclerItemClickListener = onRecyclerItemClickListener;
            view.setOnLongClickListener(this);
        }

        public void setData(Repo repo) {
            this.repo = repo;
            tvRepoName.setText(repo.getName());
            tvDescription.setText(repo.getDescription());
            tvUserName.setText(repo.getOwner().getLogin());
            if (repo.isFork()) {
                layout.setBackgroundColor(activity.getResources().getColor(android.R.color.white));
            } else {
                layout.setBackgroundColor(activity.getResources().getColor(android.R.color.holo_green_light));

            }
        }

        @Override
        public boolean onLongClick(View view) {
            onRecyclerItemClickListener.onRecyclerItemClick(repo);
            return true;
        }
    }

    protected class LoadingVH extends RecyclerView.ViewHolder {

        public LoadingVH(View itemView) {
            super(itemView);
        }
    }

    private class ItemFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            int count = data.size();
            final ArrayList<Repo> nlist = new ArrayList<Repo>(count);
            for (int i = 0; i < count; i++) {
                if (data.get(i).getName().toLowerCase().contains(constraint.toString().toLowerCase())) {
                    nlist.add(data.get(i));
                }
            }
            results.values = nlist;
            results.count = nlist.size();
            return results;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, Filter.FilterResults results) {
            dataFiltered = (ArrayList<Repo>) results.values;
            notifyDataSetChanged();
        }

    }
}
