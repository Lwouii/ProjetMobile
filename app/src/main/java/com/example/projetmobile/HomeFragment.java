package com.example.projetmobile;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.squareup.picasso.Picasso;

public class HomeFragment extends Fragment {
    private DBHandler dbHandler;
    private Button clearDB;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);


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

                // nouveau LinearLayout pour chaque animal
                LinearLayout petInfoLayout = new LinearLayout(getContext());
                petInfoLayout.setOrientation(LinearLayout.VERTICAL);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(0, 10, 0, 40);
                petInfoLayout.setLayoutParams(layoutParams);
                petInfoLayout.setGravity(Gravity.CENTER);



                //TextView nom de l'animal
                TextView petNameTextView = new TextView(getContext());
                petNameTextView.setText(name+" :");
                petNameTextView.setLayoutParams(layoutParams);
                petNameTextView.setGravity(Gravity.CENTER_VERTICAL);
                petNameTextView.setTextColor(Color.WHITE);
                petNameTextView.setTextSize(20);


                // Créer ImageView pour l'image de l'animal
                ImageView petImageView = new ImageView(getContext());
                Picasso.get().load(imageUrl).into(petImageView);



                // Ajouter TextView et ImageView au LinearLayout de l'animal
                petInfoLayout.addView(petNameTextView);
                petInfoLayout.addView(petImageView);
                //petImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                petImageView.getLayoutParams().height = 550;



                // Ajouter le LinearLayout de l'animal au LinearLayout principal
                layout.addView(petInfoLayout);
            } while (cursor.moveToNext());
        }
        cursor.close();
    }
}