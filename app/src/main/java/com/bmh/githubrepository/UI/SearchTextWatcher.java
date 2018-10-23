package com.bmh.githubrepository.UI;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.bmh.githubrepository.Adapter.RepoAdapter;

public class SearchTextWatcher implements TextWatcher {
    private RepoAdapter repoAdapter;

    public SearchTextWatcher(EditText etSearch,RepoAdapter repoAdapter){
        this.repoAdapter=repoAdapter;
        etSearch.addTextChangedListener(this);

    }
    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        // When user changed the Text
        repoAdapter.getFilter().filter(charSequence);
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }
}
