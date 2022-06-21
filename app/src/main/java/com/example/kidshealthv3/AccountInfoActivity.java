package com.example.kidshealthv3;


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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Pattern;

public class AccountInfoActivity extends AppCompatActivity implements View.OnClickListener{

    private FirebaseAuth mAuth;

    private EditText EditUsername, EditEmail, EditPassword, confirmPW;
    private String childName="", age="", weight="", height="", BMI="";
    private Button nextInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_info);

        mAuth = FirebaseAuth.getInstance();

        nextInfo = (Button) findViewById(R.id.accountBtn);
        nextInfo.setOnClickListener(this);

        EditUsername = (EditText) findViewById(R.id.editPersonName);
        EditEmail = (EditText) findViewById(R.id.editEmail);
        EditPassword = (EditText) findViewById(R.id.EditPW);
        confirmPW = (EditText) findViewById(R.id.ConfirmPW);

    }

    @Override
    public void onClick(View view) {
        SendUserInfo();
    }

    private void SendUserInfo() {
        String email = EditEmail.getText().toString().trim();
        String password = EditPassword.getText().toString().trim();
        String username = EditUsername.getText().toString().trim();
        String confirm = confirmPW.getText().toString().trim();

        if(username.isEmpty()){
            EditUsername.setError("Username is required");
            EditUsername.requestFocus();
            return;
        }
        if(email.isEmpty()){
            EditEmail.setError("Email is required");
            EditEmail.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            EditEmail.setError("Email not valid!");
            EditEmail.requestFocus();
            return;
        }
        if(password.isEmpty()){
            EditPassword.setError("Password is required");
            EditPassword.requestFocus();
            return;
        }
        if(password.length() < 6){
            EditPassword.setError("Min password length should be 6 characters");
            EditPassword.requestFocus();
            return;
        }
        if(confirm.isEmpty()){
            confirmPW.setError("Confirm password is required");
            confirmPW.requestFocus();
            return;
        }
        if (!confirm.equals(password)){
            confirmPW.setError("Password does not match!");
            confirmPW.requestFocus();
            return;
        }

        Intent intent = new Intent(AccountInfoActivity.this, ChildInfoActivity.class);
        intent.putExtra("USER_EMAIL", email);
        intent.putExtra("PASSWORD", password);
        intent.putExtra("USERNAME", username);
        startActivity(intent);

    }

}
