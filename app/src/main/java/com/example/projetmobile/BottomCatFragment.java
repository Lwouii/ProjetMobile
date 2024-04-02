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

import com.example.projetmobile.DBHandler;
import com.example.projetmobile.R;
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
    private DBHandler dbHandler;

    EditText catText;
    TextView catName;
    String imageUrl;
    String name;

    public BottomCatFragment() {

    }

    public static BottomCatFragment newInstance() {
        return new BottomCatFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        dbHandler = new DBHandler(getContext());
        View view = inflater.inflate(R.layout.fragment_bottom_cat, container, false);

        catText = view.findViewById(R.id.catText);
        catName = view.findViewById(R.id.catName);
        ImageView catImageView = view.findViewById(R.id.catImageView);
        Button catButton = view.findViewById(R.id.catButton);

        catButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = catText.getText().toString().trim();
                if (name.isEmpty()) {
                    Toast.makeText(getContext(), "Entrez un nom je vous prie !", Toast.LENGTH_SHORT).show();
                } else {
                    callAPIAndDisplayCat(catImageView);
                    System.out.println("LE NOM EST " + name);

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
                            JSONObject jsonObject = jsonArray.getJSONObject(0);
                            String message = jsonObject.getString("url");
                            imageUrl = message;
                            Picasso.get().load(message).into(catImageView);
                            boolean isInserted = dbHandler.addCat(name, imageUrl);
                            if (isInserted) {
                                Toast.makeText(getContext(), "Chat ajouté à la base de données", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getContext(), "Erreur lors de l'ajout du chat à la base de données", Toast.LENGTH_SHORT).show();
                            }
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
