package com.example.projetmobile;

import static androidx.core.content.ContentProviderCompat.requireContext;

import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.ColorStateList;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
        ConnectivityReceiver.ConnectivityReceiverListener{

    private DrawerLayout drawerLayout;
    BottomNavigationView bottomNavigationView;
    FragmentManager fragmentManager;
    Toolbar toolbar;
    private ConnectivityReceiver connectivityReceiver;


    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        connectivityReceiver = new ConnectivityReceiver(this);

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_AIRPLANE_MODE_CHANGED);
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(connectivityReceiver, intentFilter);


        fab = findViewById(R.id.fab);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav, R.string.close_nav);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setBackground(null);
        bottomNavigationView.setItemIconTintList(ColorStateList.valueOf(getColor(R.color.black)));
        bottomNavigationView.setItemTextColor(ColorStateList.valueOf(getColor(R.color.black)));


        fab.setImageTintList(ColorStateList.valueOf(getColor(R.color.white)));


        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment = null;

                switch (item.getItemId()) {
                    case R.id.bottom_person:
                        fragment = new BottomPersonFragment();
                        break;
                    case R.id.bottom_cat:
                        fragment = new BottomCatFragment();
                        break;
                    case R.id.bottom_dog:
                        fragment = new BottomDogFragment();
                        break;
                    case R.id.bottom_nationality:
                        fragment = new BottomNationalityFragment();
                        break;
                }

                if (fragment != null) {
                    openFragment(fragment);

                    fab.setImageTintList(ColorStateList.valueOf(getColor(R.color.black)));

                    int[][] states = new int[][] {
                            new int[] { android.R.attr.state_checked}, // État : cliqué
                            new int[] {-android.R.attr.state_checked} // État : non cliqué
                    };
                    int[] colors = new int[] {
                            getColor(R.color.white), // Couleur état cliqué
                            getColor(R.color.black)  // Couleur état non cliqué
                    };

                    ColorStateList textColorStateList = new ColorStateList(states, colors);
                    ColorStateList colorStateList = new ColorStateList(states, colors);
                    bottomNavigationView.setItemIconTintList(colorStateList);
                    bottomNavigationView.setItemTextColor(textColorStateList);


                    return true;
                }
                return false;
            }
        });

        fragmentManager = getSupportFragmentManager();

        openFragment(new HomeFragment());
        fab.performClick();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFragment(new HomeFragment());
                fab.setImageTintList(ColorStateList.valueOf(getColor(R.color.white)));
                bottomNavigationView.setItemIconTintList(ColorStateList.valueOf(getColor(R.color.black)));
                bottomNavigationView.setItemTextColor(ColorStateList.valueOf(getColor(R.color.black)));


            }
        });

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Supprime BroadcastReceiver quand on destroy l'activité
        unregisterReceiver(connectivityReceiver);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int itemId = menuItem.getItemId();
        if (itemId == R.id.nav_home) {
            openFragment(new HomeFragment());
        } else if (itemId == R.id.nav_setting) {
            openFragment(new SettingFragment());
        } else if (itemId == R.id.nav_share) {
            openFragment(new ShareFragment());
        } else if (itemId == R.id.nav_about) {
            openFragment(new AboutFragment());
        } else if (itemId == R.id.nav_logout) {
            finish();
            System.exit(0);
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void openFragment(Fragment fragment) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.commit();
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        if (!isConnected) {
            // Afficher le fragment d'erreur si pas de connexion démarrage
            ErrorFragment errorFragment = new ErrorFragment();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(android.R.id.content, errorFragment);
            transaction.commit();
        }
    }

    @Override
    public void onAirplaneModeChanged(boolean isAirplaneModeOn) {
        if (isAirplaneModeOn) {
            Intent intent = new Intent(Settings.ACTION_AIRPLANE_MODE_SETTINGS);
            startActivity(intent);
            Toast.makeText(this, "Veuillez désactiver le mode avion s'il vous plaît", Toast.LENGTH_SHORT).show();
        }
    }
}