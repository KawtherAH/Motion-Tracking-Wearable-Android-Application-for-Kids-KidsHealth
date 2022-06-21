package com.example.kidshealthv3;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class HomePage extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home_page, container, false);
    }
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();

        Button TodayBtn = view.findViewById(R.id.TodayBtn);
        TodayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentManager.beginTransaction().setCustomAnimations(R.anim.slide_in,R.anim.fade_out)
                        .replace(R.id.Framelayout, new TodayFragment()).addToBackStack(null).commit();
            }
        });

        Button RecordsBtn = view.findViewById(R.id.RecordsBtn);
        RecordsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentManager.beginTransaction().setCustomAnimations(R.anim.slide_in,R.anim.fade_out)
                        .replace(R.id.Framelayout, new RecordsFragment()).addToBackStack(null).commit();
            }
        });
        Button ProgressBtn = view.findViewById(R.id.ProgressBtn);
        ProgressBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentManager.beginTransaction().setCustomAnimations(R.anim.slide_in,R.anim.fade_out)
                        .replace(R.id.Framelayout, new ProgressFragment()).addToBackStack(null).commit();
            }
        });
    }
}
