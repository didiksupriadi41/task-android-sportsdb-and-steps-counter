package com.example.bolasepak;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Callback;
import android.util.Log;

import androidx.recyclerview.widget.RecyclerView;
import androidx.annotation.NonNull;

import java.util.ArrayList;

public class CardViewAdapter extends RecyclerView.Adapter<CardViewAdapter.MyViewHolder>{
    private Context context;
    private ArrayList<SoccerMatch> listSoccerMatch;

    public ArrayList<SoccerMatch> getListMatch() {
        return listSoccerMatch;
    }

    public void setListMatch(ArrayList<SoccerMatch> listSoccerMatch) {
        this.listSoccerMatch = listSoccerMatch;
    }

    public CardViewAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public CardViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_bolasepak, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewAdapter.MyViewHolder MyViewHolder, int i) {
        SoccerMatch match = getListMatch().get(i);

        //Glide.with(context).load(match.getPhoto()).apply(new RequestOptions().override(350,550)).into(cardViewViewHolder.imgPhoto);

        MyViewHolder.name1.setText(match.getName1());
        MyViewHolder.name2.setText(match.getName2());
        MyViewHolder.score1.setText(match.getScore1());
        MyViewHolder.score2.setText(match.getScore2());
        MyViewHolder.datematch.setText(match.getDateMatch());
        String imageUri1 = match.getImg1();
        Picasso.get().load(imageUri1).into(MyViewHolder.img1);
        String imageUri2 = match.getImg2();
        Picasso.get().load(imageUri2).into(MyViewHolder.img2);

    }

    @Override
    public int getItemCount() {
        return getListMatch().size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView img1, img2;
        TextView name1,name2,score1, score2, datematch;

        MyViewHolder(View itemView){
            super(itemView);
            img1 = itemView.findViewById(R.id.img1);
            img2 = itemView.findViewById(R.id.img2);
            name1 = itemView.findViewById(R.id.name1);
            name2 = itemView.findViewById(R.id.name2);
            score1 = itemView.findViewById(R.id.score1);
            score2 = itemView.findViewById(R.id.score2);
            datematch = itemView.findViewById(R.id.dateMatch);

        }
    }


}
