package com.example.gesparkmove;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.content.Intent;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

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

        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.containerFragment, new dashboardFragment());
        fragmentTransaction.commit();

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

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        drawerLayout.closeDrawer(GravityCompat.START);
        switch(item.getItemId()){
            case R.id.menuItemDashboard:
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.containerFragment, new dashboardFragment());
                fragmentTransaction.commit();
            break;
            case R.id.menuItemAdicionar:
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.containerFragment, new adicionarFragment());
                fragmentTransaction.commit();
            break;
            case R.id.menuItemAlterar:
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.containerFragment, new alterarFragment());
                fragmentTransaction.commit();
            break;
            case R.id.menuItemEstacionamentos:
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.containerFragment, new estacionamentosFragment());
                fragmentTransaction.commit();
            break;
            case R.id.menuItemInactivos:
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.containerFragment, new inactivosFragment());
                fragmentTransaction.commit();
            break;
            case R.id.menuItemPagamentos:
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.containerFragment, new pagamentosFragment());
                fragmentTransaction.commit();
            break;
            case R.id.menuItemLogout:
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();
            break;
        }
        return true;
    }

    private class loadUserTask extends AsyncTask<String, Integer, Void>{
        @Override
        protected Void doInBackground(String... params) {
            try {
                JSch jsch = new JSch();
                Session session = jsch.getSession(g.getSshUsername(), g.getSshHost(), g.getSshPort());
                session.setPassword(g.getSshPass());
                session.setPortForwardingL(g.getSshPFLPort(), g.getSshPFHost(), g.getSshPFRPort());
                Properties prop = new Properties();
                prop.put("StrictHostKeyChecking", "no");
                session.setConfig(prop);
                session.connect();
                try {
                    Class.forName("com.mysql.jdbc.Driver");
                    Connection connection = (Connection) DriverManager.getConnection(g.getMySqlUrl(), g.getMySqlUsername(), g.getMySqlPass());
                    Statement statement = (Statement) connection.createStatement();
                    ResultSet rs = statement.executeQuery(params[0]);
                    while(rs.next()){
                        //user.setNif(rs.getInt(1));
                        //user.setNome(rs.getString(2));
                        //user.setMorada(rs.getString(3));
                        //user.setCp(rs.getString(4));
                        //user.setMail(rs.getString(5));
                    }
                    connection.close();
                } catch (ClassNotFoundException | SQLException e) {
                    e.printStackTrace();
                }
                session.disconnect();
            } catch (JSchException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
        }

        @Override
        protected void onPostExecute(Void aVoid) {
        }
    }
}
