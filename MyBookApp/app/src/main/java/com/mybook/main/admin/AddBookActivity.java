package com.mybook.main.admin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mybook.main.AdminActivity;
import com.mybook.main.R;
import com.mybook.main.object.Book;
import com.mybook.main.object.Category;

import java.util.ArrayList;

public class AddBookActivity extends AppCompatActivity {
    private EditText edtBookTitle, edtBookDescription, edtBookPDFName;
    private Button btnSubmit;
    private TextView tvBookCategory, tvChoosePDF;

    private static int PDF_CHOOSER_CODE = 100;
    private Uri pdfURI;

    private ProgressDialog progressDialog;

    private String bookTitle, bookDescription, bookCategory;

    private String[] categories;
    private ArrayList<Category> categoryArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_add_book);

        initView();
        loadCategories();
        tvBookCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCategoriesDialog();
            }
        });

        edtBookPDFName.setEnabled(false);

        // set PDF chooser for tvChoosePDF to pick pdf file
        tvChoosePDF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosePDF();
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validate()) {
                    uploadBookToFirebase();
                }
            }
        });
    }

    private void showCategoriesDialog() {
        String[] categories = new String[categoryArrayList.size()];
        int i = 0;
        for (Category c: categoryArrayList) {
            categories[i] = c.getCategory();
            i++;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(AddBookActivity.this);
        builder.setTitle("Select book category");
        builder.setItems(categories, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                tvBookCategory.setText(categories[which]);
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private boolean validate() {
        bookTitle = edtBookTitle.getText().toString().trim();
        bookDescription = edtBookDescription.getText().toString().trim();
        bookCategory = tvBookCategory.getText().toString().trim();

        if(TextUtils.isEmpty(bookTitle)) {
            Toast.makeText(AddBookActivity.this, "Vui lòng nhập tiêu đề sách", Toast.LENGTH_SHORT).show();
            return false;
        } else if(TextUtils.isEmpty(bookDescription)) {
            Toast.makeText(AddBookActivity.this, "Vui lòng nhập mô tả sách", Toast.LENGTH_SHORT).show();
            return false;
        } else if(TextUtils.isEmpty(bookCategory)) {
            Toast.makeText(AddBookActivity.this, "Vui lòng chọn thể loại sách", Toast.LENGTH_SHORT).show();
            return false;
        } else if(TextUtils.isEmpty(edtBookPDFName.getText().toString().trim())) {
            Toast.makeText(AddBookActivity.this, "Vui lòng chọn file pdf", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void uploadBookToFirebase() {
        progressDialog.setMessage("Uploading book to firebase...");
        progressDialog.show();

        long dayPublish = System.currentTimeMillis();

        Log.d("UPLOAD PDF", "Upload pdf to firebase");
        String reference = "Books/" + dayPublish;
        StorageReference storageReference = FirebaseStorage.getInstance().getReference(reference);
        storageReference.putFile(pdfURI).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Log.d("Upload PDF file Success","Success to upload PDF to firebase storage");
                Log.d("Upload Book info","Upload book info....");

                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                while(!uriTask.isSuccessful());
                String pdfURL = uriTask.getResult().toString();
                Book uploadedBook = new Book(dayPublish+"",bookTitle, bookDescription,bookCategory,pdfURL,dayPublish);
                uploadBookInfo(uploadedBook);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(AddBookActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void uploadBookInfo(Book uploadedBook) {
        progressDialog.setMessage("Uploading book information...");
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Books");
        reference.child(uploadedBook.getId())
                .setValue(uploadedBook)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        progressDialog.dismiss();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(AddBookActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void choosePDF() {
        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select pdf"),PDF_CHOOSER_CODE);
    }

    private void initView() {
        edtBookTitle = findViewById(R.id.edt_book_title);
        edtBookDescription = findViewById(R.id.edt_book_description);
        edtBookPDFName = findViewById(R.id.edt_book_pdf_name);
        tvBookCategory = findViewById(R.id.tv_book_category);
        tvChoosePDF = findViewById(R.id.tv_choose_pdf);
        btnSubmit = findViewById(R.id.btn_submit);
        progressDialog = new ProgressDialog(AddBookActivity.this);
        progressDialog.setTitle("Please wait...");
        progressDialog.setCanceledOnTouchOutside(false);
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
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && requestCode == PDF_CHOOSER_CODE) {
            pdfURI = data.getData();
            edtBookPDFName.setText(pdfURI.toString());
        } else {
            Toast.makeText(AddBookActivity.this, "Cancel", Toast.LENGTH_SHORT).show();
        }
    }
}