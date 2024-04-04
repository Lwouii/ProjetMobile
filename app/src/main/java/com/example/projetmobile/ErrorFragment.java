package com.example.projetmobile;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class ErrorFragment extends Fragment {

    public ErrorFragment() {
        // Required empty public constructor
    }

    public static ErrorFragment newInstance() {
        return new ErrorFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_error, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Gestion du clic sur le bouton "Réessayer"
        view.findViewById(R.id.ButtonRetry).setOnClickListener(v -> {
            // Redémarrer l'application
            restartApplication();
        });
    }

    private void restartApplication() {
        Intent intent = getActivity().getBaseContext().getPackageManager()
                .getLaunchIntentForPackage(getActivity().getBaseContext().getPackageName());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}