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
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BottomNationalityFragment extends Fragment {

    public BottomNationalityFragment() {
        // Required empty public constructor
    }

    public static BottomNationalityFragment newInstance(String param1, String param2) {
        BottomNationalityFragment fragment = new BottomNationalityFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bottom_nationality, container, false);

        EditText nomText = view.findViewById(R.id.nomEditText);
        TextView genderView = view.findViewById(R.id.genderView);
        Button whoButton = view.findViewById(R.id.whoButton);

        whoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nomText.getText().toString().trim();
                if (name.isEmpty()) {
                    Toast.makeText(getContext(), "Entrez un nom s'il vous plaît!", Toast.LENGTH_SHORT).show();
                } else {
                    nomText.setText(name);
                    callAPIAndDisplayGender(name, genderView);
                    hideKeyboard(); // Cache le clavier après l'appui sur le bouton
                }
            }
        });

        return view;
    }

    private void callAPIAndDisplayGender(String name, TextView genderView) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    // Encodage du nom pour éviter les erreurs liées aux caractères spéciaux
                    String encodedName = URLEncoder.encode(name, "UTF-8");

                    // Appel à l'API genderize.io
                    String apiUrl = "https://api.genderize.io/?name=" + encodedName;
                    String responseData = getDataFromHTTP(apiUrl);

                    // Vérification de la réponse de l'API
                    if (!responseData.isEmpty()) {
                        // Mise à jour de l'UI sur le thread principal
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    JSONObject jsonObject = new JSONObject(responseData);
                                    if (jsonObject.has("gender")) {
                                        String gender = jsonObject.getString("gender");
                                        genderView.setText("Vous êtes un(e): " + gender);
                                    } else {
                                        genderView.setText("Genre non trouvé pour ce nom.");
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    genderView.setText("Erreur lors de l'analyse des données.");
                                }
                            }
                        });
                    } else {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                genderView.setText("Aucune réponse de l'API.");
                            }
                        });
                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            genderView.setText("Erreur lors de l'encodage du nom.");
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            genderView.setText("Erreur lors de la requête à l'API.");
                        }
                    });
                }
            }
        });
    }
    public String getDataFromHTTP(String param) {
        StringBuilder result = new StringBuilder();
        HttpURLConnection connection = null;
        try {
            URL url = new URL(param);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            InputStream inputStream = connection.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                result.append(line);
            }
            inputStream.close();
            bufferedReader.close();
        } catch (Exception e) {
            result = new StringBuilder("Erreur ");
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
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