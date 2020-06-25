package speechrecognition.com.mm.speechrecognition.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import speechrecognition.com.mm.speechrecognition.R;

import static speechrecognition.com.mm.speechrecognition.activities.landingActivity.MyPREFERENCES;

/**
 * Created by unity2 on 12/19/2017.
 */

public class RegisterActivity extends AppCompatActivity {

    EditText _username;
    EditText _password;
    EditText _confpassword;
    EditText _email;
    EditText _mobno;
    EditText _location;
    EditText _state;
    SharedPreferences.Editor editor;

    DBhelper myDBhelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Button _reg=(Button) findViewById(R.id.Register);
        myDBhelper= new DBhelper(this);
        _username=(EditText)findViewById(R.id.et_username) ;
        _password=(EditText)findViewById(R.id.et_password);
        editor = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE).edit();

        _confpassword=(EditText)findViewById(R.id.et_confpass) ;
        _email=(EditText)findViewById(R.id.et_email);
        _mobno=(EditText)findViewById(R.id.et_mobno) ;
        _location=(EditText)findViewById(R.id.et_location);

        _state=(EditText)findViewById(R.id.et_state) ;


        _reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username=_username.getText().toString().trim();
                String password=_password.getText().toString().trim();
                String confpass=_confpassword.getText().toString().trim();
                String email=_email.getText().toString().trim();

                String mobno=_mobno.getText().toString().trim();
                String location=_mobno.getText().toString().trim();

                String state=_state.getText().toString().trim();


                if(username.isEmpty()){
                    ShowError();
                }
                else  if(password.isEmpty()){
                    ShowError();
                }
                else  if(confpass.isEmpty()){
                    ShowError();
                }
                else  if(email.isEmpty()){
                    ShowError();
                }
                else  if(mobno.isEmpty()){
                    ShowError();
                }
                else  if(location.isEmpty()){
                    ShowError();
                }
                else  if(state.isEmpty()){
                    ShowError();
                }else {

                    Boolean isTrue= myDBhelper.insertdetails(username,password,email,mobno,location,state);

                    if(isTrue==true){
                        Toast.makeText(getApplicationContext(),"  registered",Toast.LENGTH_LONG).show();
                        Intent intent=new Intent(getApplicationContext(),LoginActivity.class);
                        startActivity(intent);
                        editor.putInt("reg",1);
                        editor.commit();
                    }else {
                        Toast.makeText(getApplicationContext()," not register",Toast.LENGTH_LONG).show();
                    }

                }






            }
        });




    }

    public void ShowError(){
        Toast.makeText(getApplicationContext()," please fill all filed",Toast.LENGTH_LONG).show();
    }
}
