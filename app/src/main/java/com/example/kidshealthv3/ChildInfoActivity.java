package com.example.kidshealthv3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DecimalFormat;

public class ChildInfoActivity extends AppCompatActivity implements View.OnClickListener{

    private FirebaseAuth mAuth;
    private EditText EditChildName, EditAge, EditWeight, EditHeight;
    private Button RegisterBtn;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child_info);

        mAuth = FirebaseAuth.getInstance();
        progressBar = (ProgressBar) findViewById(R.id.ProgressBarRegister);

        RegisterBtn = (Button)findViewById(R.id.childBtn);
        RegisterBtn.setOnClickListener(this);

        EditChildName = (EditText) findViewById(R.id.EditChildName);
        EditAge = (EditText) findViewById(R.id.EditAge);
        EditWeight = (EditText) findViewById(R.id.Editweight);
        EditHeight = (EditText) findViewById(R.id.Editheight);

    }

    @Override
    public void onClick(View view) {
        progressBar.setVisibility(View.VISIBLE);
        Registration();
    }

    private void Registration() {
        String ChildName = EditChildName.getText().toString().trim();
        String Age = EditAge.getText().toString().trim();
        String Weight = EditWeight.getText().toString().trim();
        String Height = EditHeight.getText().toString().trim();

        if(ChildName.isEmpty()){
            EditChildName.setError("Child name is required");
            EditChildName.requestFocus();
            return;
        }
        if(Age.isEmpty()){
            EditAge.setError("Age is required");
            EditAge.requestFocus();
            return;
        }
        if(Weight.isEmpty()){
            EditWeight.setError("Weight is required");
            EditWeight.requestFocus();
            return;
        }
        if(Height.isEmpty()){
            EditHeight.setError("Height is required");
            EditHeight.requestFocus();
            return;
        }

        double Hcm = Double.parseDouble(Height);
        double Wkg = Double.parseDouble(Weight);
        double BMI = calculateBMI(Wkg, Hcm);
        String BMIstr = Double.toString(BMI);

        Intent intentMsg = getIntent();
        String email = intentMsg.getStringExtra("USER_EMAIL");
        String password = intentMsg.getStringExtra("PASSWORD");
        String username = intentMsg.getStringExtra("USERNAME");

        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            User user = new User(username, email, ChildName, Age, Weight, Height, BMIstr);

                            // Add user to Firebase DB with the UserID (can use UserName or Email..)
                            // FirebaseFirestore.getInstance().collection("Users").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).set(user);

                            // Add user to Realtime DB with the UserID
                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if(task.isSuccessful()){
                                        progressBar.setVisibility(View.GONE);
                                        Toast.makeText(ChildInfoActivity.this, "Yor are Registered Successfully", Toast.LENGTH_LONG)
                                                .show();
                                        startActivity(new Intent(ChildInfoActivity.this, MainActivity.class));

                                    }else{
                                        progressBar.setVisibility(View.GONE);
                                        FirebaseAuthException e = (FirebaseAuthException )task.getException();
                                        Toast.makeText(ChildInfoActivity.this, "Failed Registration: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }else{
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(ChildInfoActivity.this, "Failed Registration", Toast.LENGTH_LONG)
                                    .show();
                        }
                    }
                });
    }

    private double calculateBMI(double weight, double height) {
        height = height/100;
        double calBMI = (weight) / (height * height);

        DecimalFormat df = new DecimalFormat("#.#");
        double BMI = Double.parseDouble(df.format(calBMI));
        return BMI;
    }


}