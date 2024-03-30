package com.example.projetmobile;

import android.content.Context;
import android.os.Bundle;
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

import androidx.fragment.app.Fragment;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BottomDogFragment extends Fragment {
    private DBHandler dbHandler;

    EditText dogText ;
    TextView dogName ;
    String imageUrl ;

    public BottomDogFragment() {

    }

    public static BottomDogFragment newInstance() {
        return new BottomDogFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        dbHandler = new DBHandler(getContext());
        View view = inflater.inflate(R.layout.fragment_bottom_dog, container, false);

        dogText = view.findViewById(R.id.dogText);
         dogName = view.findViewById(R.id.dogName);
        ImageView dogImageView = view.findViewById(R.id.dogImageView);
        Button dogButton = view.findViewById(R.id.dogButton);

        dogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = dogText.getText().toString().trim();
                if (name.isEmpty()) {
                    Toast.makeText(getContext(), "Entrez un nom je vous prie !", Toast.LENGTH_SHORT).show();
                } else {
                    dogName.setText(name);
                    callAPIAndDisplayDog(dogImageView);
                    dogText.setText(""); // Efface le contenu de l'EditText après l'appui sur le bouton
                    hideKeyboard(); // Cache le clavier après l'appui sur le bouton
                }
            }
        });

        return view;
    }

    private void callAPIAndDisplayDog(ImageView dogImageView) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());
        executor.execute(new Runnable() {
            @Override
            public void run() {
                String http = getDataFromHTTP("https://dog.ceo/api/breeds/image/random");
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject jsonObject = new JSONObject(http);
                            String message = jsonObject.getString("message");
                            imageUrl=message;
                            Picasso.get().load(message).into(dogImageView);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });
            }
        });
        String name = dogText.getText().toString().trim();
        boolean isInserted = dbHandler.addDog(name, imageUrl);
        if (isInserted) {
            Toast.makeText(getContext(), "Chien ajouté à la base de données", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "Erreur lors de l'ajout du chien à la base de données", Toast.LENGTH_SHORT).show();
        }

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
