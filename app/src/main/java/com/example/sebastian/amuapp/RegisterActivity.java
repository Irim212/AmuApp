package com.example.sebastian.amuapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    public static final Pattern VALID_ZIP_CODE_REGEX =
            Pattern.compile("^[0-9]{2}(?:-[0-9]{3})?$", Pattern.CASE_INSENSITIVE);

    DBHelper db;

    EditText emailEditText;
    EditText firstNameEditText;
    EditText lastNameEditText;
    EditText addressEditText;
    EditText cityEditText;
    EditText passwordEditText;
    EditText confirmPasswordEditText;
    Button registerButton;

    String email;
    String firstName;
    String lastName;
    String address;
    String city;
    String password;
    String confPassword;


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
                email = emailEditText.getText().toString().trim();
                firstName = firstNameEditText.getText().toString().trim();
                lastName = lastNameEditText.getText().toString().trim();
                address = addressEditText.getText().toString().trim();
                city = cityEditText.getText().toString().trim();
                password = passwordEditText.getText().toString().trim();
                confPassword = confirmPasswordEditText.getText().toString().trim();

                if (validateEmail() && validatePassword() && validateZipCode()) {
                    long validate = db.AddUser(email, firstName, lastName, address, city, password);

                    if (validate > 0) {
                        Toast.makeText(RegisterActivity.this, "Zarejestrowano pomyślnie", Toast.LENGTH_SHORT).show();
                        Intent loginPage = new Intent(RegisterActivity.this, LoginActivity.class);
                        startActivity(loginPage);
                    } else {
                        Toast.makeText(RegisterActivity.this, "Błąd rejestracji", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(RegisterActivity.this, "Błąd rejestracji", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private boolean validatePassword() {
        if (password.equals(confPassword)) {
            if (password.length() > 6) {
                return true;
            } else {
                Toast.makeText(RegisterActivity.this, "Hasło powinno posiadać conajmniej 6 znaków", Toast.LENGTH_SHORT).show();
                return false;
            }
        } else {
            Toast.makeText(RegisterActivity.this, "Hasła nie są jednakowe", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private boolean validateEmail() {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(email);
        if (!matcher.find()) {
            Toast.makeText(RegisterActivity.this, "Niepoprawny email", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

    private boolean validateZipCode() {
        Matcher matcher = VALID_ZIP_CODE_REGEX.matcher(email);
        if (!matcher.find()) {
            Toast.makeText(RegisterActivity.this, "Niepoprawny kod pocztowy", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }

    }
}
