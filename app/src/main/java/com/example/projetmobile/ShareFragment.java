package com.example.projetmobile;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class ShareFragment extends Fragment {

    private Button partageButton;
    private TextView textViewMessage;

    public ShareFragment() {

    }

    public static ShareFragment newInstance(String param1, String param2) {
        ShareFragment fragment = new ShareFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_share, container, false);


        partageButton = rootView.findViewById(R.id.buttonShare);
        textViewMessage = rootView.findViewById(R.id.textViewMessage);


        partageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                partagerMessage();
            }
        });

        return rootView;
    }

    private void partagerMessage() {

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, "Je m'amuse bien sur l'appli Who Am I");


        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivity(Intent.createChooser(intent, "Partager via"));
        }
    }
}