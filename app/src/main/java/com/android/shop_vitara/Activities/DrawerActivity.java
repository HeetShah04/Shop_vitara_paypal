package com.android.shop_vitara.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.android.shop_vitara.Fragments.CartFragment;
import com.android.shop_vitara.Fragments.HomeFragment;
import com.android.shop_vitara.Model.PreferenceHelper;
import com.android.shop_vitara.R;
import com.google.android.material.navigation.NavigationView;

public class DrawerActivity extends AppCompatActivity {
    NavigationView navigationView;
    ActionBarDrawerToggle toggle;
    DrawerLayout drawerLayout;
    Toolbar toolbar;
    PreferenceHelper preferenceHelper;
    private AppBarConfiguration mAppBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);
      toolbar = findViewById(R.id.toolbar);
       setSupportActionBar(toolbar);
        navigationView = findViewById(R.id.nav_menu);
        drawerLayout = findViewById(R.id.drawerlayout);
        preferenceHelper = new PreferenceHelper(this);

        toggle = new ActionBarDrawerToggle(DrawerActivity.this, drawerLayout, toolbar, R.string.Open, R.string.Close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home,R.id.nav_about_us,R.id.nav_product_description,R.id.nav_view_cart,R.id.nav_orderhistory,
                R.id.nav_privacy_policy,R.id.nav_return_policy,R.id.nav_terms_and_condition,R.id.nav_contact_us,R.id.nav_logout)
                .setDrawerLayout(drawerLayout)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        Boolean a = preferenceHelper.getisLogin();
        if (a) {
            navigationView.getMenu().findItem(R.id.nav_logout).setVisible(true);
            navigationView.getMenu().findItem(R.id.nav_orderhistory).setVisible(true);
            View header = navigationView.getHeaderView(0);
            // View view=navigationView.inflateHeaderView(R.layout.nav_header_main);
            TextView name = header.findViewById(R.id.customer_name);
            TextView email = header.findViewById(R.id.customer_email);
            name.setVisibility(View.VISIBLE);
            email.setVisibility(View.VISIBLE);
            name.setText(preferenceHelper.getcustname(this));
            email.setText(preferenceHelper.getcustemail(this));
        }else {
            navigationView.getMenu().findItem(R.id.nav_logout).setVisible(false);
            navigationView.getMenu().findItem(R.id.nav_orderhistory).setVisible(false);
            View header = navigationView.getHeaderView(0);
            // View view=navigationView.inflateHeaderView(R.layout.nav_header_main);
            TextView name = header.findViewById(R.id.customer_name);
            TextView email = header.findViewById(R.id.customer_email);
                    name.setVisibility(View.GONE);
            email.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.cart,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId()==R.id.carticon){
         Boolean a=preferenceHelper.getisLogin();
         if (a){
             navigationView.getMenu().getItem(4).setChecked(true);
             FragmentManager fm = DrawerActivity.this.getSupportFragmentManager();
             FragmentTransaction ft = fm.beginTransaction();
             ft.hide(new HomeFragment());
             ft.replace(R.id.nav_host_fragment, new CartFragment());
             ft.addToBackStack(null);
             ft.commit();
         }else{
           final AlertDialog dialog=  new AlertDialog.Builder(this).setTitle("Login to view Cart").setMessage("Login").setPositiveButton(getString(R.string.login), new DialogInterface.OnClickListener() {
               @Override
               public void onClick(DialogInterface dialogInterface, int i) {
                   startActivity(new Intent(DrawerActivity.this,LoginActivity.class));
               }
           }).show();
           dialog.show();
         }
        }
        return super.onOptionsItemSelected(item);
    }
}