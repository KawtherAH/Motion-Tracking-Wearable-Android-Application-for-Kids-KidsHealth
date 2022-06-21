package com.example.kidshealthv3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ResetPassword extends AppCompatActivity {

    private EditText emailEdit;
    private Button resetPasswordBtn;
    private ProgressBar progressBar;

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        emailEdit = (EditText) findViewById(R.id.EditTextEmail);
        resetPasswordBtn = (Button) findViewById(R.id.ResetBtn);
        progressBar = (ProgressBar) findViewById(R.id.ProgressBarResetPW);

        auth = FirebaseAuth.getInstance();
        resetPasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Email = emailEdit.getText().toString().trim();

                if(Email.isEmpty()){
                    emailEdit.setError("Email is Required.");
                    emailEdit.requestFocus();
                    return;
                }
                if(!Patterns.EMAIL_ADDRESS.matcher(Email).matches()){
                    emailEdit.setError("Please Provide Valid Email.");
                    emailEdit.requestFocus();
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);
                auth.sendPasswordResetEmail(Email).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(ResetPassword.this, "Check Your Email to Reset Password ", Toast.LENGTH_LONG).show();
                        }else{
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(ResetPassword.this, "Try Again Later", Toast.LENGTH_LONG).show();
                        }
                    }
                });

            }
        });

    }
}

