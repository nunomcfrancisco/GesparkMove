package com.example.gesparkmove;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class activityUtilizador extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    //declaração das variavies
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    Toolbar toolbar;
    NavigationView navigationView;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_utilizador);
        toolbar = findViewById(R.id.drawertoolbar);
        setSupportActionBar(toolbar);

        //inicialização dos elementos visuais
        drawerLayout = findViewById(R.id.drawer);
        navigationView = findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(this);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.openDrawer, R.string.closeDrawer);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        actionBarDrawerToggle.syncState();
        Bundle data = getIntent().getExtras();
        //inicialização de um objeto com a informação do utilizador logado
        Utilizador user = data.getParcelable("USER");
        String avatar = "https://gespark.pt/" + user.getAvatar();
        View headerView = navigationView.getHeaderView(0);
        TextView navusername = headerView.findViewById(R.id.textViewDrawerHeadName);
        TextView navusermail = headerView.findViewById(R.id.textViewDrawerHeadMail);
        CircleImageView civ = headerView.findViewById(R.id.imageViewAvatar);
        navusername.setText(user.getNome());
        navusermail.setText(user.getMail());
        Picasso.get().load(avatar).into(civ);
        //chamada do fragment dashboard
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.containerFragment, new fragmentDashboard(), "dashboard").commit();
    }

    //ação de selecionar elementos no menu lateral
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        drawerLayout.closeDrawer(GravityCompat.START);
        switch(item.getItemId()){
            case R.id.menuItemDashboard:
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.containerFragment, new fragmentDashboard(), "dashboard").commit();
            break;
            case R.id.menuItemAdicionar:
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.containerFragment, new fragmentAdicionar(), "adicionar").commit();
            break;
            case R.id.menuItemConsultar:
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.containerFragment, new fragmentConsultar(), "consultar").commit();
            break;
            case R.id.menuItemEstacionamentos:
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.containerFragment, new fragmentEstacionamentos(), "estacionamento").commit();
            break;
            case R.id.menuItemPagamentos:
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.containerFragment, new fragmentPagamentos(), "pagamentos").commit();
            break;
            case R.id.menuItemLogout:
                Intent intent = new Intent(this, activityMain.class);
                startActivity(intent);
                finish();
            break;
        }
        return true;
    }
}
