package com.rafiul.expensemaster.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rafiul.expensemaster.R;
import com.rafiul.expensemaster.databinding.SampleCategoryItemBinding;
import com.rafiul.expensemaster.models.Category;

import java.util.ArrayList;

public class CategoryAdapters extends RecyclerView.Adapter<CategoryAdapters.CategoryHolder> {

    Context context;
    ArrayList<Category> categoryArrayList;

    public interface CategoryClickListener{
        void onCategoryClicked(Category category);
    }

    CategoryClickListener categoryClickListener;

    public CategoryAdapters(Context context, ArrayList<Category> categoryArrayList, CategoryClickListener categoryClickListener) {
        this.context = context;
        this.categoryArrayList = categoryArrayList;
        this.categoryClickListener = categoryClickListener;
    }


    @NonNull
    @Override
    public CategoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CategoryHolder(LayoutInflater.from(context).inflate(R.layout.sample_category_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryHolder holder, int position) {
        Category category = categoryArrayList.get(position);

        holder.categoryItemBinding.categoryText.setText(category.getCategoryName());
        holder.categoryItemBinding.categoryIcon.setImageResource(category.getCategoryImage());
        holder.categoryItemBinding.categoryIcon.setBackgroundTintList(context.getColorStateList(category.getCategoryColor()));

        holder.itemView.setOnClickListener(view -> {
            categoryClickListener.onCategoryClicked(category);
        });

    }

    @Override
    public int getItemCount() {
        return categoryArrayList.size();
    }


    public class CategoryHolder extends RecyclerView.ViewHolder {

        SampleCategoryItemBinding categoryItemBinding;

        public CategoryHolder(@NonNull View itemView) {

            super(itemView);

            categoryItemBinding = SampleCategoryItemBinding.bind(itemView);
        }
    }
}
