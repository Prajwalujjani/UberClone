package com.ujjani.uberclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseAnonymousUtils;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnSignUpLogin, btnOneTimeLogin;
    private RadioButton driverRadioButton, passengerRadioButton;
    private EditText edtUserName, edtPassword, edtDiverOrPassenger;



    @Override
    public void onClick(View v) {

        if(edtDiverOrPassenger.getText().toString().equals("Driver") || edtDiverOrPassenger.getText().toString().equals("Passenger")){


            if (ParseUser.getCurrentUser() == null) {

                ParseAnonymousUtils.logIn(new LogInCallback() {
                    @Override
                    public void done(ParseUser user, ParseException e) {
                    if(user!= null && e == null){

                        Toast.makeText(MainActivity.this, "We have an anonymous user", Toast.LENGTH_SHORT).show();
                        user.put("as",edtDiverOrPassenger.getText().toString());

                        user.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                Intent intent = new Intent(MainActivity.this,PassengerActivity.class);
                                startActivity(intent);
                            }
                        });

                    }
                    }
                });

            }


        }

    }

    enum State{
        SIGNUP, LOGIN
    }
    private State state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Parse.enableLocalDatastore(MainActivity.this);
        Parse.initialize(new Parse.Configuration.Builder(MainActivity.this)
                .applicationId("OCmbEPW5KmQzXUvMzK5EYcXFaFYClhHK4wrsHhqM")
                .clientKey("V7GwTw2SJh4Kgnaf1EAXRB9j2Gxj2oJ9tQpc9oLg")
                .server("https://parseapi.back4app.com/")
                .enableLocalDataStore()
                .build());

        if(ParseUser.getCurrentUser() != null){

            Intent intent = new Intent(MainActivity.this,PassengerActivity.class);
            startActivity(intent);


        }

        btnSignUpLogin = findViewById(R.id.btnSignUp);
        btnOneTimeLogin = findViewById(R.id.btnOneTimeLogin);
        driverRadioButton = findViewById(R.id.rdbDriver);
        passengerRadioButton = findViewById(R.id.rdbPassenger);
        edtUserName = findViewById(R.id.edtUserName);
        edtPassword = findViewById(R.id.edtPassword);
        edtDiverOrPassenger = findViewById(R.id.edtDorP);

        btnOneTimeLogin.setOnClickListener(this);

        state = State.SIGNUP;

        btnSignUpLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (state == State.SIGNUP){

                    if (driverRadioButton.isChecked() == false && passengerRadioButton.isChecked() == false){

                        Toast.makeText(MainActivity.this, "Are you driver or passenger", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    ParseUser appUser = new ParseUser();
                    appUser.setUsername(edtUserName.getText().toString());
                    appUser.setPassword(edtPassword.getText().toString());

                    if (driverRadioButton.isChecked()){

                        appUser.put("as","driver");

                    }else if(passengerRadioButton.isChecked()){

                        appUser.put("as","passenger");
                    }
                    appUser.signUpInBackground(new SignUpCallback() {
                        @Override
                        public void done(ParseException e) {
                            if(e == null)

                                Toast.makeText(MainActivity.this, "Signed Up!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(MainActivity.this,PassengerActivity.class);
                            startActivity(intent);

                        }
                    });
                }else if(state == State.LOGIN){

                    ParseUser.logInInBackground(edtUserName.getText().toString(), edtPassword.getText().toString(), new LogInCallback() {
                        @Override
                        public void done(ParseUser user, ParseException e) {

                            Toast.makeText(MainActivity.this, "Logged In!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(MainActivity.this,PassengerActivity.class);
                            startActivity(intent);
                        }
                    });
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
       getMenuInflater().inflate(R.menu.menu_sigup_activity,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){

            case R.id.loginItem:
                if(state==State.SIGNUP){

                    state = State.LOGIN;
                    item.setTitle("Sign Up");
                    btnSignUpLogin.setText("Login");
                }else if(state == State.LOGIN){

                    state = State.SIGNUP;
                    item.setTitle("Login");
                    btnSignUpLogin.setText("SignUp");

                }

        }

        return super.onOptionsItemSelected(item);
    }

    private void transitionToPassengerActivity() {

        if (ParseUser.getCurrentUser() != null) {

            if (ParseUser.getCurrentUser().get("as").equals("passenger")) {

                Intent intent = new Intent(MainActivity.this, PassengerActivity.class);
                startActivity(intent);
            }

        }

    }
}
