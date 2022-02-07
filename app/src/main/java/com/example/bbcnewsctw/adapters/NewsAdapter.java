package com.example.bbcnewsctw.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bbcnewsctw.BR;
import com.example.bbcnewsctw.databinding.NewsItemBinding;
import com.example.bbcnewsctw.interfaces.OnClickRowListener;
import com.example.bbcnewsctw.models.ArticleModel;

import java.util.ArrayList;
import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {

    OnClickRowListener onClickRowListener;
    Context context;
    List<ArticleModel> articleList = new ArrayList<>();

    public NewsAdapter(Context context, OnClickRowListener onClickRowListener) {
        this.context = context;
        this.onClickRowListener = onClickRowListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        NewsItemBinding binding = NewsItemBinding.inflate(LayoutInflater.from(context), parent, false);
        return new ViewHolder(binding.getRoot(), onClickRowListener);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ArticleModel articleModel = articleList.get(position);
        NewsItemBinding binding = holder.getBinding();
        binding.setVariable(BR.articleItem, articleModel);
    }

    @Override
    public int getItemCount() {
        return articleList.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateList(List<ArticleModel> list){
        articleList.clear();
        articleList.addAll(list);
        notifyDataSetChanged();
    }

    public List<ArticleModel> getList(){
        return articleList;
    }

    protected static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(@NonNull View itemView, OnClickRowListener onClickRowListener) {
            super(itemView);

            itemView.setOnClickListener(view -> {
                if(getAdapterPosition() != RecyclerView.NO_POSITION)
                    onClickRowListener.onClickRow(getAdapterPosition());
            });
        }

        public NewsItemBinding getBinding(){return DataBindingUtil.getBinding(itemView);}
    }
}
