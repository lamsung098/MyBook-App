package com.mybook.main.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mybook.main.AdminActivity;
import com.mybook.main.MainActivity;
import com.mybook.main.R;
import com.mybook.main.RegisterActivity;
import com.mybook.main.object.Category;

public class AddCategory extends AppCompatActivity {

    private EditText edtCategory;
    private Button btnAdd;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_add_category);

        initView();

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String category = edtCategory.getText().toString();
                if(TextUtils.isEmpty(category)) {
                    Toast.makeText(AddCategory.this, "Vui lòng nhập thể loại", Toast.LENGTH_SHORT).show();
                } else {
                    long timestamp = System.currentTimeMillis();
                    Category c = new Category(timestamp + "",
                            category,
                            timestamp);
                    progressDialog.setMessage("Đang thêm thể loại");
                    progressDialog.show();
                    addNewCategory(c);
                }
            }
        });
    }

    private void addNewCategory(Category c) {
        progressDialog.setMessage("Đang lưu thông tin thể loại");
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Categories");
        databaseReference.child("" + c.getId()).setValue(c).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()) {
                    progressDialog.dismiss();
                    Intent intent = new Intent(AddCategory.this, AdminActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    progressDialog.dismiss();
                }
            }
        });
    }

    private void initView() {
        edtCategory = findViewById(R.id.edt_category);
        btnAdd = findViewById(R.id.btn_add);
        progressDialog = new ProgressDialog(AddCategory.this);
        progressDialog.setTitle("Vui lòng chờ");
    }
}