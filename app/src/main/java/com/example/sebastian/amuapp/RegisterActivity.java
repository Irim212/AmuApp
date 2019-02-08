package com.example.sebastian.amuapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.sebastian.amuapp.Model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    FirebaseDatabase db;

    EditText phoneEditText, emailEditText, firstNameEditText, lastNameEditText, addressEditText, cityEditText, passwordEditText, confirmPasswordEditText;
    Button registerButton;

    String phoneNumber, email, firstName, lastName, address, city, password, confPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //Init Firebase
        db = FirebaseDatabase.getInstance();
        final DatabaseReference table_user = db.getReference("User");

        phoneEditText = (EditText)findViewById(R.id.phoneEditText);
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

                final ProgressDialog progressDialog = new ProgressDialog(RegisterActivity.this);
                progressDialog.setMessage("Trwa logowanie");
                progressDialog.show();

                table_user.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        //Check if user already exist
                        if(dataSnapshot.child(phoneEditText.getText().toString().trim()).exists())
                        {
                            progressDialog.dismiss();
                            Toast.makeText(RegisterActivity.this, "Numer telefonu zajęty", Toast.LENGTH_SHORT).show();
                            System.out.println("WBIAM DO SPRAWDZANIA CZY NUMER ISTNIEJE");
                        }else{
                            progressDialog.dismiss();
                            String email = emailEditText.getText().toString().trim();
                            Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(email);
                            System.out.println("Sprawdzam maila");
                            if (!matcher.find()) {
                                Toast.makeText(RegisterActivity.this, "Niepoprawny email", Toast.LENGTH_SHORT).show();
                            } else {
                                String password = passwordEditText.getText().toString().trim();
                                String confPassword = confirmPasswordEditText.getText().toString().trim();
                                System.out.println("Sprawdzam hasło");
                                if (password.equals(confPassword)) {
                                    if (password.length() >= 6){
                                        String firstName = firstNameEditText.getText().toString().trim();
                                        String lastName = lastNameEditText.getText().toString().trim();
                                        String address = addressEditText.getText().toString().trim();
                                        String city = cityEditText.getText().toString().trim();
                                        User user = new User(firstName, lastName, email, city, address, password);
                                        table_user.child(phoneEditText.getText().toString().trim()).setValue(user);
                                        Toast.makeText(RegisterActivity.this, "Zarejestrowano pomyślnie", Toast.LENGTH_SHORT).show();
                                        System.out.println("Wszystko git");

                                        Intent loginPage = new Intent(RegisterActivity.this, LoginActivity.class);
                                        startActivity(loginPage);
                                    } else {
                                        Toast.makeText(RegisterActivity.this, "Hasło powinno posiadać conajmniej 6 znaków", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(RegisterActivity.this, "Hasła nie są jednakowe", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
    }


}
