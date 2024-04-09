package com.example.projetmobile;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PetAdapter extends RecyclerView.Adapter<PetAdapter.ViewHolder> {

    private Context context;
    private List<Pet> pets;


    public PetAdapter(Context context, List<Pet> pets){
        this.context = context;
        this.pets = pets;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;

            view = LayoutInflater.from(context).inflate(R.layout.one_pet, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Pet pet = pets.get(position);
        holder.nameTextView.setText(pet.getNom());
        String posterPath = pet.getUrl();
        if (posterPath != null && !posterPath.isEmpty()) {
            Picasso.get().load(posterPath).into(holder.imageView);
        }
        // image placeholder a ajouter
        Picasso.get().load(R.drawable.ic_launcher_foreground).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        if (pets == null)
            return 0;
        return pets.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;
        RoundedImageView imageView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.dogName);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }
}