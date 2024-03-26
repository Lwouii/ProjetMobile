package com.example.projetmobile;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BottomCatFragment extends Fragment {

    public BottomCatFragment() {
        // Required empty public constructor
    }

    public static BottomCatFragment newInstance(String param1, String param2) {
        BottomCatFragment fragment = new BottomCatFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bottom_cat, container, false);

        EditText catText = view.findViewById(R.id.catText);
        TextView catName = view.findViewById(R.id.catName);
        ImageView catImageView = view.findViewById(R.id.catImageView);
        Button catButton = view.findViewById(R.id.catButton);

        catButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = catText.getText().toString().trim();
                if (name.isEmpty()) {
                    Toast.makeText(getContext(), "Compliqué de générer un chat sans nom...", Toast.LENGTH_SHORT).show();
                } else {
                    catName.setText(name);
                    callAPIAndDisplayCat(catImageView);
                    catText.setText(""); // Efface le contenu de l'EditText après l'appui sur le bouton
                    hideKeyboard(); // Cache le clavier après l'appui sur le bouton
                }
            }
        });

        return view;
    }

    private void callAPIAndDisplayCat(ImageView catImageView) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());
        executor.execute(new Runnable() {
            @Override
            public void run() {
                String http = getDataFromHTTP("https://api.thecatapi.com/v1/images/search");
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONArray jsonArray = new JSONArray(http);
                            JSONObject jo = jsonArray.getJSONObject(0);
                            String imageUrl = jo.getString("url");

                            // Load image into ImageView
                            Picasso.get().load(imageUrl).into(catImageView);

                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });
            }
        });
    }

    public String getDataFromHTTP(String param) {
        StringBuilder result = new StringBuilder();
        HttpURLConnection connexion = null;
        try {
            URL url = new URL(param);
            connexion = (HttpURLConnection) url.openConnection();
            connexion.setRequestMethod("GET");
            InputStream inputStream = connexion.getInputStream();
            InputStreamReader inputStreamReader = new
                    InputStreamReader(inputStream);
            BufferedReader bf = new BufferedReader(inputStreamReader);
            String ligne = "";
            while ((ligne = bf.readLine()) != null) {
                result.append(ligne);
            }
            inputStream.close();
            bf.close();
            connexion.disconnect();
        } catch (Exception e) {
            result = new StringBuilder("Erreur ");
        }
        return result.toString();
    }

    private void hideKeyboard() {
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
