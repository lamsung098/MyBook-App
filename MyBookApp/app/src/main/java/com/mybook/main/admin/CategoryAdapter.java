package com.mybook.main.admin;

import android.content.Context;
import android.content.DialogInterface;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mybook.main.R;
import com.mybook.main.object.Category;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> implements Filterable {

    private ArrayList<Category> categoryArrayList, filterList;
    private Context context;

    private FilterCategory filterCategory;

    public CategoryAdapter(ArrayList<Category> categoryArrayList, Context context) {
        this.categoryArrayList = categoryArrayList;
        this.context = context;
        this.filterList = categoryArrayList;
    }

    @NonNull
    @Override
    public CategoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.category_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryAdapter.ViewHolder holder, int position) {
        Category c = categoryArrayList.get(position);
        holder.tvCategory.setText(c.getCategory());
        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Confirm");
                builder.setMessage("Are you sure to remove all event");
                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        deleteCategory(c);
                    }
                });
                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // do noting
                    }
                });
                AlertDialog confirmDialog = builder.create();
                confirmDialog.show();
            }
        });
    }

    private void deleteCategory(Category c) {
        String id = c.getId();

        // Get firebase reference
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Categories");
        ref.child(id).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()) {
                    Toast.makeText(context, "Remove category successful", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return categoryArrayList.size();
    }

    public void setCategoryArrayList(ArrayList<Category> categories) {
        this.categoryArrayList = categories;
        notifyDataSetChanged();
    }

    @Override
    public Filter getFilter() {
        if(filterCategory == null) {
            filterCategory = new FilterCategory(filterList, this);
        }
        return filterCategory;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvCategory;
        Button btnDelete;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCategory = itemView.findViewById(R.id.tv_category);
            btnDelete = itemView.findViewById(R.id.btn_delete_category);
        }
    }
}
