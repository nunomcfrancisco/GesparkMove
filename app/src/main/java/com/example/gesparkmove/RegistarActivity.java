package com.example.gesparkmove;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;

import java.sql.DriverManager;
import java.sql.ResultSet;

public class RegistarActivity extends AppCompatActivity {

    TextView text, errorText;
    Button show;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registar);



        text = (TextView) findViewById(R.id.textViewTesteDB);
        errorText = (TextView) findViewById(R.id.textViewNoError);
        show = (Button) findViewById(R.id.buttonShowRecords);

        show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Task().execute();
            }
        });
    }

    class Task extends AsyncTask<Void, Void, Void>{
        String records = "", error = "";
        @Override
        protected Void doInBackground(Void... voids){
            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection connection = (Connection) DriverManager.getConnection("jdbc:mysql://92.222.70.24:22/cgs_gespark", "gremote", "GespPW01");
                Statement statement = (Statement) connection.createStatement();
//                ResultSet resultSet = statement.executeQuery("SELECT * FROM utilizadores LIMIT 1");

//                while (resultSet.next()){
//                    records += resultSet.getInt(1);
//                }
            }
            catch (Exception e){
                error = e.toString();
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid){
            text.setText(records);
            if(error != "")
                errorText.setText(error);
            super.onPostExecute(aVoid);
        }
    }
}
