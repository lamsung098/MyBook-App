package com.mybook.main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mybook.main.object.User;

public class RegisterActivity extends AppCompatActivity {

    private EditText edtUsername, edtEmail, edtPhone, edtPassword, edtRepassword;
    private Button btnRegister;
    private TextView tvLogin;

    private FirebaseAuth firebaseAuth;

    private String username, email, phone, password, repassword;

    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        firebaseAuth = FirebaseAuth.getInstance();
        initView();

        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // validate data before register
                if(validate()) {
                    register();
                }
            }
        });
    }

    private void initView() {
        edtUsername = findViewById(R.id.edt_register_username);
        edtEmail = findViewById(R.id.edt_register_email);
        edtPhone = findViewById(R.id.edt_register_phone);
        edtPassword = findViewById(R.id.edt_register_password);
        edtRepassword = findViewById(R.id.edt_register_repassword);
        tvLogin = findViewById(R.id.tv_login);
        btnRegister = findViewById(R.id.btn_register);
        progressDialog = new ProgressDialog(RegisterActivity.this);
        progressDialog.setTitle("Please wait");
        progressDialog.setCanceledOnTouchOutside(false);
    }
    private boolean validate() {

        // Get data from edit text
        username = edtUsername.getText().toString().trim();
        email = edtEmail.getText().toString().trim();
        phone = edtPhone.getText().toString().trim();
        password = edtPassword.getText().toString().trim();
        repassword = edtRepassword.getText().toString().trim();

        // Check username is empty
        if(TextUtils.isEmpty(username)) {
            Toast.makeText(RegisterActivity.this, "Vui lòng nhập tên người dùng", Toast.LENGTH_SHORT).show();
            return false;
        }
        // Check email is empty
        else if(TextUtils.isEmpty(email)) {
            Toast.makeText(RegisterActivity.this, "Vui lòng nhập email", Toast.LENGTH_SHORT).show();
            return false;
        }
        // Check email is invalid
        else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(RegisterActivity.this, "Địa chỉ email không hợp lệ", Toast.LENGTH_SHORT).show();
            return false;
        }
        // Check phone is empty
        else if(TextUtils.isEmpty(phone)) {
            Toast.makeText(RegisterActivity.this, "Vui lòng nhập số điện thoại", Toast.LENGTH_SHORT).show();
            return false;
        }
        // Check password is empty
        else if(TextUtils.isEmpty(password)) {
            Toast.makeText(RegisterActivity.this, "Vui lòng nhập mật khẩu", Toast.LENGTH_SHORT).show();
            return false;
        }
        // Check repassword is empty
        else if(TextUtils.isEmpty(repassword)) {
            Toast.makeText(RegisterActivity.this, "Vui lòng nhập lại mật khẩu", Toast.LENGTH_SHORT).show();
            return false;
        }
        // Check password does not match
        else if(!password.equals(repassword)) {
            Toast.makeText(RegisterActivity.this, "Mật khẩu không trùng khớp", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void register() {
        progressDialog.setMessage("Đang tạo tài khoản");
        progressDialog.show();

        firebaseAuth.createUserWithEmailAndPassword(email,password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                registerUserInfo();
            }})
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(RegisterActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void registerUserInfo() {
        progressDialog.setMessage("Lưu thông tin tài khoản");
        String uid = firebaseAuth.getUid();

        User user = new User(uid,username,email,phone,0);

        // Get database reference
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.child(uid).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()) {
                    progressDialog.dismiss();
                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                    startActivity(intent);
                    Toast.makeText(RegisterActivity.this, "Tạo tài khoản thành công", Toast.LENGTH_SHORT).show();

                } else {
                    progressDialog.dismiss();
                    Toast.makeText(RegisterActivity.this, "Có lỗi xảy ray", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}