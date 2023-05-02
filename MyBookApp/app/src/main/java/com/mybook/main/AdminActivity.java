package com.mybook.main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.ktx.Firebase;
import com.mybook.main.admin.AddBookActivity;
import com.mybook.main.admin.AddCategory;
import com.mybook.main.admin.CategoryAdapter;
import com.mybook.main.object.Category;

import java.util.ArrayList;

public class AdminActivity extends AppCompatActivity {
    private Button btnAddCategory, btnAddBook;
    private EditText edtSearchCategory;
    private FirebaseAuth firebaseAuth;

    private RecyclerView rvCategory;
    private ArrayList<Category> categoryArrayList;
    private CategoryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);

        initView();
        loadCategories();
        btnAddCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminActivity.this, AddCategory.class));
            }
        });

        btnAddBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminActivity.this, AddBookActivity.class));
            }
        });

        edtSearchCategory.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void initView() {
        edtSearchCategory = findViewById(R.id.edt_search_category);
        rvCategory = findViewById(R.id.rv_category);
        btnAddCategory = findViewById(R.id.btn_add_category);
        btnAddBook = findViewById(R.id.btn_add_book);
    }

    private void loadCategories(){
        categoryArrayList = new ArrayList<>();

        // Get firebase reference
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Categories");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                categoryArrayList.clear();
                for (DataSnapshot c: snapshot.getChildren()) {
                    Category category = new Category(c.child("id").getValue().toString(),
                            c.child("category").getValue().toString(),
                            Long.parseLong(c.child("timestamp").getValue().toString()));
                    categoryArrayList.add(category);
                }
                adapter = new CategoryAdapter(categoryArrayList,AdminActivity.this);
                rvCategory.setAdapter(adapter);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(AdminActivity.this);
                rvCategory.setLayoutManager(linearLayoutManager);
                DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(AdminActivity.this, linearLayoutManager.getOrientation());
                rvCategory.addItemDecoration(dividerItemDecoration);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}