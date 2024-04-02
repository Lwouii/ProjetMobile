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
        clearDB = view.findViewById(R.id.clearButton);
        clearDB.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dbHandler.deleteDB(requireContext());
            }
        });

        dbHandler = new DBHandler(getContext());
        LinearLayout dogsLayout = view.findViewById(R.id.dogsLayout);
        LinearLayout catsLayout = view.findViewById(R.id.catsLayout);

        // Afficher les chiens
        displayPets(dbHandler.getAllDogs(), dogsLayout);

        // Afficher les chats
        displayPets(dbHandler.getAllCats(), catsLayout);

        return view;
    }

    private void displayPets(Cursor cursor, LinearLayout layout) {
        if (cursor.moveToFirst()) {
            do {
                String nameColumn = DBContract.DogEntry.COLUMN_NAME;
                String imageUrlColumn = DBContract.DogEntry.COLUMN_IMAGE_URL;

                @SuppressLint("Range")
                String name = cursor.getString(cursor.getColumnIndex(nameColumn));
                @SuppressLint("Range")
                String imageUrl = cursor.getString(cursor.getColumnIndex(imageUrlColumn));

                // Créer un nouveau LinearLayout pour chaque animal
                LinearLayout petInfoLayout = new LinearLayout(getContext());
                petInfoLayout.setOrientation(LinearLayout.VERTICAL);

                // Créer TextView pour le nom de l'animal
                TextView petNameTextView = new TextView(getContext());
                petNameTextView.setText(name);

                // Créer ImageView pour l'image de l'animal
                ImageView petImageView = new ImageView(getContext());
                Picasso.get().load(imageUrl).into(petImageView);

                // Ajouter TextView et ImageView au LinearLayout de l'animal
                petInfoLayout.addView(petNameTextView);
                petInfoLayout.addView(petImageView);

                // Ajouter le LinearLayout de l'animal au LinearLayout principal
                layout.addView(petInfoLayout);
            } while (cursor.moveToNext());
        }
        cursor.close();
    }
}
