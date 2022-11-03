//register
package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;


public class RegisterUI extends AppCompatActivity {
    EditText username, email, password;
    Button register;
    TextView homePage;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_ui);

        // ...
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        username = (EditText) findViewById(R.id.usernameReg);
        email = (EditText) findViewById(R.id.accountReg);
        password = (EditText) findViewById(R.id.passwordReg);

        homePage = (TextView) findViewById(R.id.appName2);

        register = (Button) findViewById(R.id.btnRegisterReg);

        homePage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplication(), MainActivity.class);
                startActivity(intent);

            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String usernameAdd = username.getText().toString().trim();
                String emailAdd = email.getText().toString().trim();
                String passwordAdd = password.getText().toString().trim();
//    pop alarm information if the password or address is error structure
                if(usernameAdd.equals("")||emailAdd.equals("")||passwordAdd.equals("")){
                    Toast.makeText(RegisterUI.this,"All fields required.", Toast.LENGTH_SHORT).show();
                    username.requestFocus();
                    return;
                }else if (!Patterns.EMAIL_ADDRESS.matcher(emailAdd).matches()){
                    Toast.makeText(RegisterUI.this,"Input email address is invalid.", Toast.LENGTH_SHORT).show();
                    return;
                }else if(passwordAdd.length() < 6){
                    Toast.makeText(RegisterUI.this,"Please enter a no less than 6 digit password.", Toast.LENGTH_SHORT).show();
                    return;
                }
//                send the user information to the firebase
                // cited from google firebase open source code 
                mAuth.createUserWithEmailAndPassword(emailAdd, passwordAdd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            UserInfo userinfo = new UserInfo(usernameAdd, emailAdd);

                            FirebaseDatabase.getInstance().getReference("Players")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(userinfo).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                Toast.makeText(RegisterUI.this, "Successfully Registered.", Toast.LENGTH_SHORT).show();
                                                // Go back to Home page
                                                Intent intent = new Intent(getApplication(),MainActivity.class);
                                                startActivity(intent);
                                            }else{
                                                Toast.makeText(RegisterUI.this, "Register failed, please check your input and try it again.", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });

                        }else{
                            Toast.makeText(RegisterUI.this,"Register failed, please check your input and try it again.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

    }
}
