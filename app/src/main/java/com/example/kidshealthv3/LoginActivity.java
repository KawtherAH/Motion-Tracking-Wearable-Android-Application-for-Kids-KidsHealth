package com.example.kidshealthv3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    private FirebaseAuth mAuth;

    private TextView register, ForgotPWBtn;
    private EditText EditEmail, EditPassword;
    private Button LoginBtn;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        progressBar = (ProgressBar) findViewById(R.id.ProgressBarLogin);

        register = (TextView) findViewById(R.id.RegisterBtn);
        register.setOnClickListener(this);

        ForgotPWBtn = (TextView) findViewById(R.id.ForgotPassword);
        ForgotPWBtn.setOnClickListener(this);

        LoginBtn = (Button) findViewById(R.id.LoginBtn);
        LoginBtn.setOnClickListener(this);

        EditEmail = (EditText) findViewById(R.id.EditUserEmail);
        EditPassword = (EditText) findViewById(R.id.EditPassword);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.RegisterBtn:
                startActivity(new Intent(this, AccountInfoActivity.class));
                break;
            case R.id.LoginBtn:
                UserSignIn();
                break;
            case R.id.ForgotPassword:
                startActivity(new Intent(this, ResetPassword.class));
                break;
        }
    }

    private void UserSignIn() {
        String UserEmail = EditEmail.getText().toString().trim();
        String Password = EditPassword.getText().toString().trim();

        if(UserEmail.isEmpty()){
            EditEmail.setError("Email is required");
            EditEmail.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(UserEmail).matches()){
            EditEmail.setError("Email not valid!");
            EditEmail.requestFocus();
            return;
        }
        if(Password.isEmpty()){
            EditPassword.setError("Password is required");
            EditPassword.requestFocus();
            return;
        }
        if(Password.length() < 6){
            EditPassword.setError("Min password length should be 6 characters");
            EditPassword.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        mAuth.signInWithEmailAndPassword(UserEmail, Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(LoginActivity.this, "Welcome Back..", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                }else {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(LoginActivity.this, "Username / Password is Wrong!", Toast.LENGTH_LONG)
                            .show();
                }
            }
        });
    }
}


