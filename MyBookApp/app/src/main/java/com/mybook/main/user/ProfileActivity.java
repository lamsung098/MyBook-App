package com.mybook.main.user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mybook.main.LoginActivity;
import com.mybook.main.MainActivity;
import com.mybook.main.R;
import com.mybook.main.object.User;

public class ProfileActivity extends AppCompatActivity {
    private TextView tvUsername;
    private Button btnSignout;

    private BottomNavigationView bottomNavigationView;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        firebaseAuth = FirebaseAuth.getInstance();
        initView();
        bottomNavigationView.setSelectedItemId(R.id.menu_user_profile);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_user_home:
                        startActivity(new Intent(ProfileActivity.this, MainActivity.class));
                        finish();
                        return true;
                    case R.id.menu_user_profile:

                        return true;
                }
                return false;
            }
        });

        firebaseUser = firebaseAuth.getCurrentUser();
        // Get database reference
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.child(firebaseUser.getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                       user = new User(firebaseUser.getUid(),snapshot.child("username").getValue().toString(),
                               snapshot.child("email").getValue().toString(),
                               snapshot.child("phone").getValue().toString()
                               ,0);
                       tvUsername.setText(user.getUsername());
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }});
        btnSignout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.signOut();
                startActivity(new Intent(ProfileActivity.this,LoginActivity.class));
                finish();
            }
        });
    }

    public void initView(){
        bottomNavigationView = findViewById(R.id.user_nav);
        tvUsername = findViewById(R.id.tv_username);
        btnSignout = findViewById(R.id.btn_signout);
    }
}