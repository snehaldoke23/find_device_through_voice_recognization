package speechrecognition.com.mm.speechrecognition.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import speechrecognition.com.mm.speechrecognition.R;

import static speechrecognition.com.mm.speechrecognition.activities.landingActivity.MyPREFERENCES;

/**
 * Created by unity2 on 12/19/2017.
 */

public class LoginActivity extends AppCompatActivity {


    DBhelper myDBhelper;
    EditText et_username, et_password;
    SharedPreferences.Editor editor;
    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        myDBhelper = new DBhelper(this);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        et_username = (EditText) findViewById(R.id.et_username);
        et_password = (EditText) findViewById(R.id.et_password);
        TextView _regUser = (TextView) findViewById(R.id.regUser);
        Button _login = (Button) findViewById(R.id.signIn);
      int regVal=  sharedpreferences.getInt("reg",0);
      if(regVal==1){
          _regUser.setVisibility(View.GONE);
      }
        _regUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);
            }
        });
        _login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uname = et_username.getText().toString().trim();
                String upass = et_password.getText().toString().trim();

                if (uname.isEmpty()) {
                    ShowError();
                } else if (upass.isEmpty()) {
                    ShowError();

                } else {
                    int a = myDBhelper.Login(uname, upass);
                    if (a == 1) {
                        Intent intent = new Intent(getApplicationContext(), landingActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(getApplicationContext(), " invalid username and password", Toast.LENGTH_LONG).show();
                    }
                }


            }
        });


    }

    public void ShowError() {
        Toast.makeText(getApplicationContext(), " please fill all filed", Toast.LENGTH_LONG).show();
    }
}
