package com.example.gesparkmove;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;
import com.squareup.picasso.Picasso;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

public class RegistarActivity extends AppCompatActivity {

    TextView text, errorText, textmd5, textresultado;
    String url = "https://gespark.pt/imgs/users/1587122670.JPG";
    ImageView iv;
    Button show;
    Button query;
    String records = "";
    Globals g = new Globals();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registar);

        iv = findViewById(R.id.imageViewTesteURL);
        text = findViewById(R.id.textViewTesteDB);
        errorText = findViewById(R.id.textViewNoError);
        show = findViewById(R.id.buttonShowRecords);
        query = findViewById(R.id.buttonBD);
        textmd5 = findViewById(R.id.textViewTesteMD5);
        textresultado = findViewById(R.id.textViewTesteIguais);
        loadImageFromUrl(url);

    }

    private void loadImageFromUrl(String url) {
        Picasso.get().load(url).into(iv);
    }

    class Task extends AsyncTask<Void, Void, Void>{
        String error = "";
        @Override
        protected Void doInBackground(Void... voids){
            try {
                JSch jsch = new JSch();
                Session session = jsch.getSession(g.getSshUsername(), g.getSshHost(), 58022);
                session.setPassword(g.getSshPass());
                session.setPortForwardingL(3306, "127.0.0.1", 3306);

                Properties prop = new Properties();
                prop.put("StrictHostKeyChecking", "no");
                session.setConfig(prop);
                session.connect();
                try {
                    Class.forName("com.mysql.jdbc.Driver");
                    Connection connection = (Connection) DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/cgs_gespark", "gremote", "GespPW01");
                    Statement statement = (Statement) connection.createStatement();
                    ResultSet resultSet = statement.executeQuery("SELECT password FROM utilizadores WHERE id = 40");
                    while (resultSet.next()){
                        records += resultSet.getString(1);
                    }
                    connection.close();
                } catch (SQLException | ClassNotFoundException e){}
                session.disconnect();
            }
            catch (Exception e){
                error = e.toString();
            }

            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid){
            text.setText(records);
            textmd5.setText(new md5Tools().encode("Carapau33!"));
            if(records.equals(new md5Tools().encode("Carapau33!"))){
                textresultado.setText("São iguais!");
            }else{
                textresultado.setText("Não são iguais!");
            }
            super.onPostExecute(aVoid);
        }
    }
}
