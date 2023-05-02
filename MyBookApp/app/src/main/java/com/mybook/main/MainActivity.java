package com.mybook.main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mybook.main.user.ProfileActivity;

public class MainActivity extends AppCompatActivity {

    private TextView tvEmail;

    private FirebaseAuth firebaseAuth;
    private BottomNavigationView bottomNavigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseAuth = FirebaseAuth.getInstance();
        tvEmail = findViewById(R.id.tv_email);

        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        tvEmail.setText(firebaseUser.getEmail());
        bottomNavigationView = findViewById(R.id.user_nav);
        bottomNavigationView.setSelectedItemId(R.id.menu_user_home);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_user_home:
                        return true;
                    case R.id.menu_user_profile:
                        startActivity(new Intent(MainActivity.this, ProfileActivity.class));
                        finish();
                        return true;
                }
                return false;
            }
        });
    }
}