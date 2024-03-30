package com.example.projetmobile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BottomPersonFragment extends Fragment {

    private ImageView profileImageView;
    private TextView nameTextView;
    private TextView locationTextView;
    private TextView emailTextView;
    private TextView phoneTextView;

    public BottomPersonFragment() {
        // Required empty public constructor
    }

    public static BottomPersonFragment newInstance() {
        return new BottomPersonFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_bottom_person, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        profileImageView = view.findViewById(R.id.profile_image);
        nameTextView = view.findViewById(R.id.nameTextView);
        locationTextView = view.findViewById(R.id.addressTextView);
        emailTextView = view.findViewById(R.id.emailTextView);
        phoneTextView = view.findViewById(R.id.phoneTextView);

        fetchRandomUserData();
    }

    private void fetchRandomUserData() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                String data = Utils.getDataFromHTTP("https://randomuser.me/api/");
                try {
                    JSONObject jsonObject = new JSONObject(data);
                    JSONArray resultsArray = jsonObject.getJSONArray("results");
                    JSONObject userObject = resultsArray.getJSONObject(0);

                    final String name = userObject.getJSONObject("name").getString("title") +
                            " " + userObject.getJSONObject("name").getString("first") +
                            " " + userObject.getJSONObject("name").getString("last");
                    final String location = userObject.getJSONObject("location").getString("street") +
                            ", " + userObject.getJSONObject("location").getString("city") +
                            ", " + userObject.getJSONObject("location").getString("state") +
                            ", " + userObject.getJSONObject("location").getString("country");
                    final String email = userObject.getString("email");
                    final String phone = userObject.getString("phone");

                    // Update UI on the main thread
                    requireActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            nameTextView.setText(name);
                            locationTextView.setText(location);
                            emailTextView.setText(email);
                            phoneTextView.setText(phone);

                            // Load profile image using Glide library
                            String profileImageUrl = null;
                            try {
                                profileImageUrl = userObject.getJSONObject("picture").getString("large");
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                            RequestOptions requestOptions = new RequestOptions()
                                    .diskCacheStrategy(DiskCacheStrategy.ALL);
                            Glide.with(requireContext())
                                    .load(profileImageUrl)
                                    .apply(requestOptions)
                                    .into(profileImageView);
                        }
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                    // Handle JSON parsing error
                    requireActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(requireContext(), "Error parsing JSON data", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }
}
