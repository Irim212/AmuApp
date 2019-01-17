package com.example.sebastian.amuapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity {

    DBHelper db;

    EditText emailEditText;
    EditText firstNameEditText;
    EditText lastNameEditText;
    EditText addressEditText;
    EditText cityEditText;
    EditText passwordEditText;
    EditText confirmPasswordEditText;
    Button registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        db = new DBHelper(this);

        emailEditText = (EditText) findViewById(R.id.emailEditText);
        firstNameEditText = (EditText) findViewById(R.id.firstNameEditText);
        lastNameEditText = (EditText) findViewById(R.id.lastNameEditText);
        addressEditText = (EditText) findViewById(R.id.addressEditText);
        cityEditText = (EditText) findViewById(R.id.cityEditText);
        passwordEditText = (EditText) findViewById(R.id.passwordEditText);
        confirmPasswordEditText = (EditText) findViewById(R.id.confirmPasswordEditText);
        registerButton = (Button) findViewById(R.id.registerButton);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEditText.getText().toString().trim();
                String firstName = firstNameEditText.getText().toString().trim();
                String lastName = lastNameEditText.getText().toString().trim();
                String address = addressEditText.getText().toString().trim();
                String city = cityEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();
                String confPassword = confirmPasswordEditText.getText().toString().trim();

                if(password.equals(confPassword))
                {
                    long validate = db.AddUser(email, firstName, lastName, address, city, password);

                    if(validate>0) {
                        Toast.makeText(RegisterActivity.this, "Zarejestrowano pomyślnie", Toast.LENGTH_SHORT).show();
                        Intent loginPage = new Intent(RegisterActivity.this, LoginActivity.class);
                        startActivity(loginPage);
                    }else{
                        Toast.makeText(RegisterActivity.this, "Błąd rejestracji", Toast.LENGTH_SHORT).show();
                    }

                }else{
                    Toast.makeText(RegisterActivity.this, "Błąd rejestracji", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
