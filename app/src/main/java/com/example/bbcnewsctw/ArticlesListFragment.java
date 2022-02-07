package com.example.bbcnewsctw;

import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.bbcnewsctw.adapters.NewsAdapter;
import com.example.bbcnewsctw.databinding.FragmentArticlesListBinding;
import com.example.bbcnewsctw.interfaces.OnClickLayoutListener;
import com.example.bbcnewsctw.interfaces.OnClickRowListener;
import com.example.bbcnewsctw.models.ArticleModel;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ArticlesListFragment extends Fragment implements OnClickRowListener, SwipeRefreshLayout.OnRefreshListener, OnClickLayoutListener {

    private FragmentArticlesListBinding binding;
    private ProgressBar progressBar;
    private RecyclerView recyclerView;
    private NewsAdapter newsAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    //To avoid fast clicks on RecyclerView Row
    private boolean canClickRow;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentArticlesListBinding.inflate(inflater, container, false);

        setupLayoutComponents();
        fetchNews();

        return binding.getRoot();
    }

    private void setupLayoutComponents() {
        binding.setClickRefresh(this);
        progressBar = binding.progressBar;
        swipeRefreshLayout = binding.swipeRefreshLayout;
        if (getContext() != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            progressBar.setIndeterminateTintList(ColorStateList.valueOf(getContext().getColor(R.color.purple_500)));
            swipeRefreshLayout.setProgressBackgroundColorSchemeColor(getContext().getColor(R.color.purple_500));
        }
        swipeRefreshLayout.setOnRefreshListener(this);
    }

    /**
     * Getting news trough API with request method from NewsService Class
     * After callback completion...
     * ... If response is succeed, the recyclerview is populated with the received data
     * ... If there was an error, the Error Layout appears
     */
    private void fetchNews() {
        swipeRefreshLayout.setRefreshing(false);
        progressBar.setVisibility(View.VISIBLE);

        if (getActivity() != null) {
            Utils.getNewsServices().fetchTopHeadlines(getActivity(), progressBar, value ->
                    getActivity().runOnUiThread(() -> setupNewsList(value != null ? value.getArticles() : null)));
        }
    }

    private void setupNewsList(List<ArticleModel> articlesList) {
        verifyConnection(articlesList == null);
        if (getContext() != null && articlesList!=null) {
            if (recyclerView == null) {
                recyclerView = binding.recyclerView;
                newsAdapter = new NewsAdapter(getActivity(), this);
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
                recyclerView.setAdapter(newsAdapter);
            }

            if (!articlesList.isEmpty()) {
                if (getActivity() != null)
                    ((MainActivity) getActivity()).setupActionBarTitle(articlesList.get(0).getSource().getName());
                sortListByDate(articlesList);
            }

            newsAdapter.updateList(articlesList);
            progressBar.setVisibility(View.GONE);
        }
    }

    public void verifyConnection(boolean connHasProblem) {
        binding.errorConstraintLayout.setVisibility(connHasProblem ? View.VISIBLE : View.GONE);
        binding.recyclerView.setVisibility(connHasProblem ? View.GONE : View.VISIBLE);
    }

    /**
     * Change articles date original format, received from API
     * Sort articles list by date (first to last)
     *
     * @param articlesList - List of Articles
     */
    private void sortListByDate(List<ArticleModel> articlesList) {
        for (ArticleModel article : articlesList)
            article.setPublishedAt(Utils.changeDateFormat(article.getPublishedAt()));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            Collections.sort(articlesList, Comparator.comparing(ArticleModel::getPublishedAt));
    }

    /**
     * When recyclerview row is clicked, open chosen article in new fragment
     * The chosen article is passed by parameter to the new fragment
     *
     * @param article - Chosen article
     */
    private void openArticle(ArticleModel article) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(Utils.ARTICLE_PARAM, article);

        ArticleInfoFragment articleInfoFragment = new ArticleInfoFragment();
        articleInfoFragment.setArguments(bundle);

        if (getActivity() != null) {
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction()
                    .setCustomAnimations(R.anim.slide_in, R.anim.fade_out, R.anim.fade_in, R.anim.slide_out);
            transaction.add(((MainActivity) getActivity()).getBinding().fragmentContainerView.getId(), articleInfoFragment, "ArticleInfoFragment");
            transaction.hide(ArticlesListFragment.this);
            transaction.addToBackStack("ArticleInfoFragment");
            transaction.commit();
        }

    }

    @Override
    public void onClickRow(int position) {
        if (canClickRow)
            openArticle(newsAdapter.getList().get(position));
    }

    @Override
    public void onRefresh() {
        fetchNews();
    }

    @Override
    public void onStart() {
        super.onStart();

        canClickRow = true;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);

        canClickRow = !hidden;
    }

    @Override
    public void onClickLayout(View view) {
        if (view == binding.errorConstraintLayout)
            fetchNews();
    }
}