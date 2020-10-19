package com.projects.mychat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.projects.mychat.databinding.ActivityMainBinding;

import java.util.Objects;

import sdk.chat.core.session.ChatSDK;
import sdk.guru.common.RX;

public class MainActivity extends AppCompatActivity{
    ActivityMainBinding binding;
    NavHostFragment navHostFragment;
    NavController navController;
    NavigationView navigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        navigationView = findViewById(R.id.navView);
         navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.myNavHostFragment);
         navController = Objects.requireNonNull(navHostFragment).getNavController();
        AppBarConfiguration appBarConfiguration =
                new AppBarConfiguration.Builder(navController.getGraph()).setOpenableLayout(binding.drawerLayout).build();
        NavigationUI.setupActionBarWithNavController(this,navController,appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView,navController);


        // prevent nav gesture if not on start destination
        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            if(destination.getId()==R.id.loginFragment||destination.getId()==R.id.signUpFragment)
            {
                binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            }else
            {
                binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            }

        });

        navigationView.setNavigationItemSelectedListener(item -> {
            if(item.getItemId()==R.id.logout_item)
        {
            Log.d("logout","Logout");
            ChatSDK.auth().logout()
                    .observeOn(RX.main())
                    .subscribe(()->{
                              navController.navigate(navController.getGraph().getStartDestination());
                        Log.d("logout","Logout");},
                            t->Log.e("login Not working", t.toString()));
            return true;
        }else
            {
                return false;
            }

        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        return NavigationUI.navigateUp(navController,binding.drawerLayout);
    }



//    @Override
//    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//        if(item.getItemId()==R.id.logout_item)
//        {
//            Log.d("logout","Logout");
//            ChatSDK.auth().logout()
//                    .observeOn(RX.main())
//                    .subscribe(()->{
//                              navController.navigate(navController.getGraph().getStartDestination());
//                        Log.d("logout","Logout");},
//                            t->Log.e("login Not working", t.toString()));
//        }
//        return true;
//    }
}