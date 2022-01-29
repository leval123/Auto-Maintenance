package com.example.jultrautomaintenance;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;

    private EditText editTextFullName,editTextEmail,editTextPassword;
    private Button submit;
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth=FirebaseAuth.getInstance();

        editTextEmail = (EditText) findViewById(R.id.EmailAddress_main);
        editTextPassword = (EditText) findViewById(R.id.Password_main);
        editTextFullName = (EditText) findViewById(R.id.editTextTextFullName);

        submit = (Button) findViewById(R.id.Register_main);
        submit.setOnClickListener(v-> registerUser());


    }
    public void registerUser(){
        String email = editTextEmail.getText().toString().trim();
        String password= editTextPassword.getText().toString().trim();
        String name = editTextFullName.getText().toString().trim();

        if(email.isEmpty())
        {
            editTextEmail.setError("Email cannot be empty!");
            editTextEmail.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            editTextEmail.setError("Please provide valid email in the format example@mail.com");
            editTextEmail.requestFocus();
            return;
        }
        if(password.isEmpty())
        {
            editTextPassword.setError("Password cannot be empty!");
            editTextPassword.requestFocus();
            return;
        }
        if(name.isEmpty())
        {
            editTextFullName.setError(" Please Enter your name!");
            editTextFullName.requestFocus();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful())
                {
                    UserModel user = new UserModel(name,email);

                    FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser()
                            .getUid()).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(RegisterActivity.this,"User Registered Succesfully!",Toast.LENGTH_LONG).show();
                                startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
                            }
                            else
                            {
                                Toast.makeText(RegisterActivity.this,"Some error occurred during registration, try again!",Toast.LENGTH_LONG).show();
                            }
                        }
                    });


                }
                else{
                    Toast.makeText(RegisterActivity.this,"Some error occurred during registration, try again!",Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}


