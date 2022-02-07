package com.example.bbcnewsctw;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bbcnewsctw.databinding.FragmentArticleInfoBinding;
import com.example.bbcnewsctw.models.ArticleModel;

import java.util.Objects;

public class ArticleInfoFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentArticleInfoBinding binding = FragmentArticleInfoBinding.inflate(inflater, container, false);

        if (getArguments() != null) {
            ArticleModel article = (ArticleModel) getArguments().getSerializable(Utils.ARTICLE_PARAM);
            article.setPublishedAt(article.getPublishedAt().replace("\n", " "));
            binding.setArticle(article);
        }

        return binding.getRoot();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (getActivity() != null)
            Objects.requireNonNull(((MainActivity) getActivity()).getSupportActionBar()).setDisplayHomeAsUpEnabled(false);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getActivity() != null)
            Objects.requireNonNull(((MainActivity) getActivity()).getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
    }
}