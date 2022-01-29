package com.example.jultrautomaintenance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    private Button register, login;
    private EditText email, password;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        login = (Button) findViewById(R.id.login_main);

        login.setOnClickListener(view -> home());
        email = (EditText) findViewById(R.id.EmailAddress_main);
        password = (EditText) findViewById(R.id.Password_main);

        register = (Button) findViewById(R.id.Register_main);

        register.setOnClickListener(v->registerMethod());
        mAuth=FirebaseAuth.getInstance();
    }

    public void registerMethod(){
        startActivity(new Intent(this,RegisterActivity.class));
    }
    public void home(){
        String emailText = email.getText().toString().trim();
        String passwordText = password.getText().toString().trim();

        if(emailText.isEmpty())
        {
            email.setError("Email cannot be empty!");
            email.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(emailText).matches())
        {
            email.setError("Enter valid email!");
            email.requestFocus();
            return;
        }
        if (passwordText.isEmpty())
        {
            password.setError("Password cannot be empty!");
            password.requestFocus();
            return;
        }

        mAuth.signInWithEmailAndPassword(emailText,passwordText).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    startActivity(new Intent(LoginActivity.this,HomeActivity.class));
                }
                else{
                    Toast.makeText(LoginActivity.this,"Error logging in! Try again!",Toast.LENGTH_LONG).show();
                }
            }
        });

    }

}