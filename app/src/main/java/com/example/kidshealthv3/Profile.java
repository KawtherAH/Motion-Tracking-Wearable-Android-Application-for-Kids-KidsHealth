package com.example.kidshealthv3;

import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.HashMap;



public class Profile extends Fragment{

    private FirebaseUser firebaseUser;
    private DatabaseReference reference;
    private String UserID;
    private EditText NameText, AgeText, WeightText, HeightText, BMIText;
    private Button upButton;
    private ProgressBar progressBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        UserID = firebaseUser.getUid();

        progressBar = (ProgressBar) getView().findViewById(R.id.ProgressBarProfile);
        upButton = getView().findViewById(R.id.UpdateButton);

        NameText = (EditText) getView().findViewById(R.id.ChildNameEditText);
        AgeText = (EditText) getView().findViewById(R.id.AgeEditText);
        WeightText = (EditText) getView().findViewById(R.id.WeightEditText);
        HeightText = (EditText) getView().findViewById(R.id.HeightEditText);
        BMIText = (EditText) getView().findViewById(R.id.BMIEditText);

        // Retrieve Data from Realtime Database - 1st tutorial
        reference.child(UserID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User UserProfile = snapshot.getValue(User.class);

                if( UserProfile != null){

                    progressBar.setVisibility(View.GONE);

                    String childName = UserProfile.childName;
                    String childAge = UserProfile.age;
                    String childWeight = UserProfile.weight;
                    String childHeight = UserProfile.height;
                    String childBMI = UserProfile.BMI;

                    NameText.setText(childName);
                    AgeText.setText(childAge);
                    WeightText.setText(childWeight);
                    HeightText.setText(childHeight);

                    double BMIRange = Double.parseDouble(childBMI);
                    String BMIClass;
                    if(BMIRange < 18.5) {
                        BMIClass = " Underweight \nHealthy Weight \n(18.5 – 24.9)";
                    }else if (BMIRange >= 18.5 && BMIRange <= 24.9){
                        BMIClass = " Healthy Weight";
                    }else if (BMIRange >= 25.0 && BMIRange <= 29.9){
                        BMIClass = " Overweight \nHealthy Weight \n(18.5 – 24.9)";
                    }else {
                        BMIClass = " Obesity \nHealthy Weight \n(18.5 – 24.9)";
                    }
                    BMIText.setText(childBMI + " kg/m^2" + BMIClass);


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getActivity(),"Something wrong happened!", Toast.LENGTH_LONG).show();
            }
        });


        upButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ChildName = NameText.getText().toString();
                String Age = AgeText.getText().toString();
                String Weight = WeightText.getText().toString();
                String Height = HeightText.getText().toString();

                progressBar.setVisibility(View.VISIBLE);
                UpdateUserProfile(ChildName, Age, Weight, Height);
            }
        });
    }

    // Update Data in Realtime Database - 2nd tutorial
    private void UpdateUserProfile(String childName, String age, String weight, String height) {
        double Hcm = Double.parseDouble(height);
        double Wkg = Double.parseDouble(weight);
        double BMI = calculateBMI(Wkg, Hcm);
        String BMIstr = Double.toString(BMI);

        HashMap newUserValues = new HashMap();
        newUserValues.put("childName", childName);
        newUserValues.put("age", age);
        newUserValues.put("weight", weight);
        newUserValues.put("height", height);
        newUserValues.put("BMI", BMIstr);

        reference.child(UserID).updateChildren(newUserValues).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                if(task.isSuccessful()){
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getActivity(), "Updated Successfully", Toast.LENGTH_SHORT).show();

                }else{
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getActivity(), "Failed to Update.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // Calculate BMI to update it
    private double calculateBMI(double weight, double height) {
        height = height/100;
        double calBMI = (weight) / (height * height);

        DecimalFormat df = new DecimalFormat("#.#");
        double BMI = Double.parseDouble(df.format(calBMI));
        return BMI;
    }

}
