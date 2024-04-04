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
import android.widget.Adapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.squareup.picasso.Picasso;

import java.util.List;

public class HomeFragment extends Fragment {
    private DBHandler dbHandler;

    List<Pet> dogList;
    List<Pet> catList;
    private PetAdapter adapter;
    private Button clearDB;
    private RecyclerView dogRecyclerView;
    private RecyclerView catRecyclerView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);


        dbHandler = new DBHandler(getContext());

        dogList=dbHandler.getAllDogs();
        catList=dbHandler.getAllCats();

        dogRecyclerView=view.findViewById(R.id.dogsLayout);
        catRecyclerView = view.findViewById(R.id.catsLayout);


        adapter=new PetAdapter(requireContext(),dogList);
        dogRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        dogRecyclerView.setAdapter(adapter);

        adapter=new PetAdapter(requireContext(),catList);
        catRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        catRecyclerView.setAdapter(adapter);





        return view;
    }


}