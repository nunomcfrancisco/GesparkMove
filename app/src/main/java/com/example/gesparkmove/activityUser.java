package com.example.gesparkmove;

import android.content.Intent;
import android.os.Bundle;
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

public class activityUser extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    //declaração das variávies
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    Toolbar toolbar;
    NavigationView navigationView;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        toolbar = findViewById(R.id.drawertoolbar);
        setSupportActionBar(toolbar);
        //assiciação das varáveis aos elementos visuais
        drawerLayout = findViewById(R.id.drawer);
        navigationView = findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(this);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.openDrawer, R.string.closeDrawer);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        actionBarDrawerToggle.syncState();
        //carrega um objeto user com a informação do utilizador logado
        Bundle data = getIntent().getExtras();
        User user = data.getParcelable("USER");
        View headerView = navigationView.getHeaderView(0);
        TextView navusername = headerView.findViewById(R.id.textViewDrawerHeadName);
        TextView navusermail = headerView.findViewById(R.id.textViewDrawerHeadMail);
        CircleImageView civ = headerView.findViewById(R.id.imageViewAvatar);
        navusername.setText(user.getName());
        navusermail.setText(user.getMail());
        if(user.getAvatar().equals(""))
            Picasso.get().load("https://gespark.pt/imgs/users/avatar.png").into(civ);
        else
            Picasso.get().load("https://gespark.pt/" + user.getAvatar()).into(civ);
        //chamada do fragment dashboard
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.containerFragment, new fragmentDashboard(), "dashboard").commit();
        //ação de clickar o avatar do utilizador no menu
        civ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.closeDrawer(GravityCompat.START);
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.containerFragment, new fragmentUser(), "utilizador").commit();
            }
        });
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
            case R.id.menuItemAdd:
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.containerFragment, new fragmentAddCar(), "adicionar").commit();
            break;
            case R.id.menuItemListCar:
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.containerFragment, new fragmentListCar(), "consultar").commit();
            break;
            case R.id.menuItemParking:
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.containerFragment, new fragmentParked(), "estacionamento").commit();
            break;
            case R.id.menuItemPayment:
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.containerFragment, new fragmentPayment(), "pagamentos").commit();
            break;
            case R.id.menuItemContacts:
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.containerFragment, new fragmentContacts(), "contactos").commit();
            break;
            case R.id.menuItemLogout:
                Intent intent = new Intent(this, activityMain.class);
                startActivity(intent);
                finish();
            break;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
    }
}
