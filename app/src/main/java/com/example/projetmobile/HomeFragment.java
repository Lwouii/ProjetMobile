package com.example.projetmobile;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.squareup.picasso.Picasso;

public class HomeFragment extends Fragment {
    private DBHandler dbHandler;
    private Button clearDB;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        clearDB=view.findViewById(R.id.clearButton);
        clearDB.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dbHandler.deleteDB(requireContext());
            }


        });

        dbHandler = new DBHandler(getContext());
        LinearLayout dogsLayout = view.findViewById(R.id.dogsLayout);

        Cursor cursor = dbHandler.getAllDogs();
        if (cursor.moveToFirst()) {
            do {
                String name = cursor.getString(cursor.getColumnIndex(DBContract.DogEntry.COLUMN_NAME));
                String imageUrl = cursor.getString(cursor.getColumnIndex(DBContract.DogEntry.COLUMN_IMAGE_URL));

                // Create a new LinearLayout for each dog
                LinearLayout dogInfoLayout = new LinearLayout(getContext());
                dogInfoLayout.setOrientation(LinearLayout.VERTICAL);

                // Create TextView for dog name
                TextView dogNameTextView = new TextView(getContext());
                dogNameTextView.setText(name);

                // Create ImageView for dog image
                ImageView dogImageView = new ImageView(getContext());
                Picasso.get().load(imageUrl).into(dogImageView);

                // Add TextView and ImageView to the dog LinearLayout
                dogInfoLayout.addView(dogNameTextView);
                dogInfoLayout.addView(dogImageView);

                // Add dog LinearLayout to the main LinearLayout
                dogsLayout.addView(dogInfoLayout);
            } while (cursor.moveToNext());
        }
        cursor.close();

        return view;
    }
}
