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

import org.json.JSONArray;
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
    private CodePays codePays;
    private EditText nomText;
    private TextView genderView;
    private TextView natiView;
    private TextView natiViewLabel;
    private TextView genderViewLabel;
    private TextView nameTextViewLabel;

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

        codePays = new CodePays();
        nameTextViewLabel = view.findViewById(R.id.nameTextViewLabel);
        nomText = view.findViewById(R.id.nomEditText);
        genderView = view.findViewById(R.id.genderView);
        natiView = view.findViewById(R.id.natiView);
        natiViewLabel = view.findViewById(R.id.natiViewLabel);
        genderViewLabel = view.findViewById(R.id.genderViewLabel);


        Button whoButton = view.findViewById(R.id.whoButton);

        whoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nomText.getText().toString().trim();
                if (name.isEmpty()) {
                    Toast.makeText(getContext(), "Entrez un nom s'il vous plaît!", Toast.LENGTH_SHORT).show();
                } else {
                    nomText.setText(name);
                    String majName=name.substring(0, 1).toUpperCase() + name.substring(1);
                    nameTextViewLabel.setText("Votre prénom :"+majName);
                    callAPIAndDisplayData(name);
                    hideKeyboard();

                    natiViewLabel.setVisibility(View.VISIBLE);
                    whoButton.setVisibility(View.VISIBLE);

                }
            }
        });

        return view;
    }

    private void callAPIAndDisplayData(String name) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());


        executor.execute(new Runnable() {
            @Override
            public void run() {
                String nationalizeAPIResult = getDataFromHTTP("https://api.nationalize.io/?name=" + name);

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject jsonObject = new JSONObject(nationalizeAPIResult);
                            JSONArray countries = jsonObject.getJSONArray("country");
                            StringBuilder nationalities = new StringBuilder();
                            for (int i = 0; i < countries.length(); i++) {
                                JSONObject country = countries.getJSONObject(i);
                                String countryCode = country.getString("country_id");
                                String countryName = codePays.mapCountryCodeToFullName(countryCode); // Mapping du code de pays au nom complet
                                String probabilityPercentage = String.format("%.2f%%", country.getDouble("probability") * 100);
                                nationalities.append(countryName)
                                        .append(getCountryFlagEmoji(countryCode))
                                        .append(": ")
                                        .append(probabilityPercentage)
                                        .append(" ")
                                        .append("\n");
                            }
                            natiView.setText(nationalities.toString());
                            natiView.setVisibility(View.VISIBLE);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });


        executor.execute(new Runnable() {
            @Override
            public void run() {
                String genderizeAPIResult = getDataFromHTTP("https://api.genderize.io/?name=" + name);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject jsonObject = new JSONObject(genderizeAPIResult);
                            String gender = jsonObject.getString("gender");
                            if (gender.equals("male")) {
                                genderView.setText("Homme \u2642"); // symbole ♂
                            } else {
                                genderView.setText("Femme \u2640"); // symbole ♀
                            }
                            genderView.setVisibility(View.VISIBLE);
                            genderViewLabel.setVisibility(View.VISIBLE); // Rendre visible le TextView pour le label du genre
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
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
    private String getCountryFlagEmoji(String countryCode) {
        int countryCodeOffset = 0x1F1E6;
        int firstChar = Character.codePointAt(countryCode, 0) - 0x41 + countryCodeOffset;
        int secondChar = Character.codePointAt(countryCode, 1) - 0x41 + countryCodeOffset;
        return new String(Character.toChars(firstChar)) + new String(Character.toChars(secondChar));
    }
    private void hideKeyboard() {
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}