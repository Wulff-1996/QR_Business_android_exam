package com.example.qrbusiness.controller.Navigation;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.qrbusiness.R;
import com.example.qrbusiness.controller.Create.CreateActivity;
import com.example.qrbusiness.controller.Login.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;

public class NavigationActivity extends AppCompatActivity
        implements
        NavigationView.OnNavigationItemSelectedListener
{
    //  navigation view
    private DrawerLayout drawer;
    private Toolbar toolbar;
    private NavigationView navigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_view);

        init();
    }

    private void init()
    {
        this.toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        this.drawer = findViewById(R.id.drawer_layout);
        this.navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this,
                drawer,
                toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        String email = getIntent().getBundleExtra("BUNDLE").getString("EMAIL");

        if (email != null)
        {
            View hView =  navigationView.getHeaderView(0);
            TextView nav_user = (TextView)hView.findViewById(R.id.nav_header_username);
            nav_user.setText(email);
        }

        toolbar.setTitle("Library");
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, new LibraryFragment())
                .commit();
        navigationView.setCheckedItem(R.id.nav_library);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem)
    {
        switch (menuItem.getItemId())
        {
            case R.id.nav_library:
                toolbar.setTitle("Library");
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, new LibraryFragment())
                        .commit();
                break;

            case R.id.nav_new:
                final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Select QR type");

                String[] qrTypes = {"Contact", "URL", "WiFi", "Cancel"};
                builder.setItems(qrTypes, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        Intent i = new Intent(getApplicationContext(), CreateActivity.class);
                        switch (which)
                        {

                            case 0:
                                i.putExtra("TYPE", "vcard");
                                startActivity(i);
                                break;

                            case 1:
                                i.putExtra("TYPE", "web");
                                startActivity(i);
                                break;

                            case 2:
                                i.putExtra("TYPE", "wifi");
                                startActivity(i);
                                break;

                            case 3:
                                break;
                        }
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();

                break;

            case R.id.nav_sign_out:
                Toast.makeText(this, "Signed out", Toast.LENGTH_SHORT).show();
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                this.finish();
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed()
    {
        if(drawer.isDrawerOpen(GravityCompat.START))
        {
            drawer.closeDrawer(GravityCompat.START);
        }
        else
        {
            super.onBackPressed();
        }
    }
}


