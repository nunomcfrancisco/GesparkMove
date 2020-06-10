package com.example.gesparkmove;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationView;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;
import com.squareup.picasso.Picasso;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import de.hdodenhof.circleimageview.CircleImageView;

public class UtilizadorActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    Toolbar toolbar;
    NavigationView navigationView;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    Globals g = new Globals();
    private Handler utilizadorHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_utilizador);
        toolbar = findViewById(R.id.drawertoolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer);
        navigationView = findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(this);

        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.openDrawer, R.string.closeDrawer);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        actionBarDrawerToggle.syncState();

        Bundle data = getIntent().getExtras();
        Utilizador user = data.getParcelable("USER");
        String avatar = "https://gespark.pt/" + user.getAvatar();
        View headerView = navigationView.getHeaderView(0);
        TextView navusername = headerView.findViewById(R.id.textViewDrawerHeadName);
        TextView navusermail = headerView.findViewById(R.id.textViewDrawerHeadMail);
        CircleImageView civ = headerView.findViewById(R.id.imageViewAvatar);
        navusername.setText(user.getNome());
        navusermail.setText(user.getMail());

        Picasso.get().load(avatar).into(civ);

        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.containerFragment, new dashboardFragment(), "dashboard").commit();
        //fragmentTransaction.commit();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        drawerLayout.closeDrawer(GravityCompat.START);
        switch(item.getItemId()){
            case R.id.menuItemDashboard:
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.containerFragment, new dashboardFragment(), "dashboard").commit();
            break;
            case R.id.menuItemAdicionar:
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.containerFragment, new adicionarFragment(), "adicionar").commit();
            break;
            case R.id.menuItemConsultar:
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.containerFragment, new consultarFragment(), "consultar").commit();
            break;
            case R.id.menuItemEstacionamentos:
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.containerFragment, new estacionamentosFragment(), "estacionamento").commit();
            break;
            case R.id.menuItemInactivos:
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.containerFragment, new inactivosFragment(), "inactivos").commit();
            break;
            case R.id.menuItemPagamentos:
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.containerFragment, new pagamentosFragment(), "pagamentos").commit();
            break;
            case R.id.menuItemLogout:
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();
            break;
        }
        return true;
    }
}
