package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import org.w3c.dom.Text;
//this is the login class
public class MainActivity extends AppCompatActivity {

    // cited from google firebase open source code
    private FirebaseAuth mAuth;
    
    EditText account, password;
    Button login, register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // cited from google firebase open source code 
        mAuth = FirebaseAuth.getInstance();

        account = (EditText) findViewById(R.id.account);
        password = (EditText) findViewById(R.id.password);
        login = (Button) findViewById(R.id.btnLogin);
        register = (Button) findViewById(R.id.btnRegister);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user = account.getText().toString().trim();
                String key = password.getText().toString().trim();
                if(user.equals("")||key.equals("")){
                    Toast.makeText(MainActivity.this, "All fields required", Toast.LENGTH_SHORT).show();
                    account.requestFocus();
                    return;
                }
//                sign button click
                // cited from google firebase open source code 
                mAuth.signInWithEmailAndPassword(user, key).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        pop window about the state of login
                        if(task.isSuccessful()){
                            // Intent intent = new Intent(getApplication(), nextActivity.class);
                            // startActivity(intent);
                            Intent intent =new Intent(getApplicationContext(),Home.class);
                            startActivity(intent);
                            Toast.makeText(MainActivity.this, "Successfully login.", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(MainActivity.this, "Login failed, please check your input and try it again.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });



            }
        });

//        jump to the registor window
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(getApplicationContext(),RegisterUI.class);
                startActivity(intent);
            }
        });

    }
}
