package com.example.gesparkmove;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

public class RegistarActivity extends AppCompatActivity {

    TextView text, errorText;
    Button show;
    Button query;
    String records = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registar);

        text = findViewById(R.id.textViewTesteDB);
        errorText = findViewById(R.id.textViewNoError);
        show = findViewById(R.id.buttonShowRecords);
        query = findViewById(R.id.buttonBD);

        show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Task().execute();
            }
        });
    }

    class Task extends AsyncTask<Void, Void, Void>{
        String error = "";
        @Override
        protected Void doInBackground(Void... voids){
            try {
                JSch jsch = new JSch();
                Session session = jsch.getSession("gpark", "92.222.70.24", 58022);
                session.setPassword("GespPW01");
                session.setPortForwardingL(3306, "127.0.0.1", 3306);

                Properties prop = new Properties();
                prop.put("StrictHostKeyChecking", "no");
                session.setConfig(prop);
                session.connect();
                try {
                    Class.forName("com.mysql.jdbc.Driver");
                    Connection connection = (Connection) DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/cgs_gespark", "gremote", "GespPW01");
                    Statement statement = (Statement) connection.createStatement();
                    ResultSet resultSet = statement.executeQuery("SELECT * FROM utilizadores LIMIT 1");
                    while (resultSet.next()){
                        records += " " + resultSet.getString(3);
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
            if(error != "") {
                errorText.setText(error);
            } else {
                text.setText("Ligado!");
                text.setText(records);
            }
            super.onPostExecute(aVoid);
        }
    }
}
