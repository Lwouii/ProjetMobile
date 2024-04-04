package com.example.projetmobile;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;


public class SettingFragment extends Fragment {
    private DBHandler dbHandler;
    private Button clearDB;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting, container, false);

        dbHandler = new DBHandler(getContext());
        Button clearDB = view.findViewById(R.id.clearButton);
        clearDB.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Toast.makeText(getContext(), "Base de données vidée !", Toast.LENGTH_SHORT).show();
                dbHandler.deleteDB(requireContext());
            }

        });
        return view;
    }
}