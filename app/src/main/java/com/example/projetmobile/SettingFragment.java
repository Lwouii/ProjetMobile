package com.example.projetmobile;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Button;
import android.widget.Toast;

public class SettingFragment extends Fragment {
    private Switch switchTheme;
    private SharedPreferences sharedPreferences;

    public SettingFragment() {
        // Required empty public constructor
    }

    public static SettingFragment newInstance() {
        return new SettingFragment();
    }
    private DBHandler dbHandler;
    private Button clearDB;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting, container, false);
        switchTheme = view.findViewById(R.id.switchTheme);
        sharedPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);

        // Restaurer l'état du switch à partir des préférences partagées
        boolean isNightMode = sharedPreferences.getBoolean("nightMode", false);
        switchTheme.setChecked(isNightMode);

        // Ajouter un écouteur sur le Switch pour détecter les changements d'état
        switchTheme.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Enregistrer l'état du switch dans les préférences partagées
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("nightMode", isChecked);
                editor.apply();

                if (isChecked) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                } else { // Sinon, basculer vers le mode clair
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                }
            }
        });



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