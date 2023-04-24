package com.example.civicadvocacyapp;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

public class RecycleViewHolder extends RecyclerView.ViewHolder {

    public ImageView Pic;
    public TextView Title;
    public TextView NameParty;

    RecycleViewHolder(View view) {
        super(view);
        Title = view.findViewById(R.id.Title_Recycle);
        NameParty = view.findViewById(R.id.NameParty_Recycle);
        Pic = view.findViewById(R.id.Mugshot_Recycle);
    }

    public void setPic(String url)
    {
        if(url == "missing")
        {
            Pic.setImageResource(R.drawable.missing);
        }
        else
        {
            Glide.with(itemView.getContext())
                    .load(url)
                    .placeholder(R.drawable.brokenimage)
                    .into(Pic);
        }
    }
}
