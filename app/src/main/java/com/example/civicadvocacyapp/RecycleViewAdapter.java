package com.example.civicadvocacyapp;

import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

public class RecycleViewAdapter extends RecyclerView.Adapter<RecycleViewHolder>
{

    private final List<Official> Items;
    private final MainActivity mainAct;

    RecycleViewAdapter(List<Official> empList, MainActivity ma) {
        this.Items = empList;
        mainAct = ma;
    }

    @NonNull
    @Override
    public RecycleViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_list_row, parent, false);

        itemView.setOnClickListener(mainAct);

        return new RecycleViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecycleViewHolder holder, int position) {

        Official item = Items.get(position);
        holder.Title.setText(item.Title);
        holder.NameParty.setText(item.Name + "(" + item.Party + ")");
        holder.setPic(item.Pic);
    }

    @Override
    public int getItemCount() {
        if (Items == null){
            return 0;
        }
        return Items.size();
    }

}
