package com.bmh.githubrepository.Adapter;


import com.bmh.githubrepository.Model.Repo;

// This interface allows us to handle clicks outside of the adapter, i.e. the AlertDialog in the MainActivity
// This adapter can now be used again to make another list that does something different when clicked e.g. a Toast
public interface OnRecyclerItemClickListener {
    void onRecyclerItemClick(Repo repo);

}
