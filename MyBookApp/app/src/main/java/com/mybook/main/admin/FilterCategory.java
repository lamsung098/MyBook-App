package com.mybook.main.admin;

import android.widget.Filter;

import com.mybook.main.object.Category;

import java.util.ArrayList;

public class FilterCategory extends Filter {

    private ArrayList<Category> filterList;
    private CategoryAdapter adapter;

    public FilterCategory(ArrayList<Category> filterList, CategoryAdapter adapter) {
        this.filterList = filterList;
        this.adapter = adapter;
    }

    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        FilterResults results = new FilterResults();
        Category c;
        if(constraint!=null && constraint.length()>0) {
            constraint = constraint.toString().toLowerCase();
            ArrayList<Category> listAfterFiltered = new ArrayList<>();
            for (int i = 0 ; i < filterList.size(); i++) {
                c = filterList.get(i);
                if(filterList.get(i).getCategory().toLowerCase().contains(constraint)) {
                    listAfterFiltered.add(c);
                }
            }
            results.count = listAfterFiltered.size();
            results.values = listAfterFiltered;
        } else {
            results.count = filterList.size();
            results.values = filterList;
        }
        return results;
    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {
        adapter.setCategoryArrayList((ArrayList<Category>) results.values);
    }
}
