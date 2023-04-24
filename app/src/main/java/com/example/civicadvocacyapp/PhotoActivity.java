package com.example.civicadvocacyapp;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

public class PhotoActivity extends AppCompatActivity {

    public Official official;

    public TextView locationHeader;
    public TextView nameDisplay;
    public TextView titleDisplay;
    public ImageView picDisplay;
    public ImageView partyDisplay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);

        locationHeader = findViewById(R.id.LocationDisplay_Photo);
        nameDisplay = findViewById(R.id.Name_Photo);
        titleDisplay = findViewById(R.id.Title_Photo);
        picDisplay = findViewById(R.id.Mugshot_Photo);
        partyDisplay = findViewById(R.id.Party_Photo);

        official = (Official) getIntent().getSerializableExtra("OFFICIAL");

        locationHeader.setText(MainActivity.location);
        nameDisplay.setText(official.Name);
        titleDisplay.setText(official.Title);

        if(official.Pic.equals("missing"))
        {
            picDisplay.setImageResource(R.drawable.missing);
        }
        else
        {
            Glide.with(this)
                    .load(official.Pic)
                    .placeholder(R.drawable.brokenimage)
                    .into(picDisplay);
        }

        if(official.Party.equals("Democratic Party"))
        {
            partyDisplay.setImageResource(R.drawable.dem_logo);
            findViewById(R.id.Layout_Photo).setBackgroundColor(Color.BLUE);
        }
        else if(official.Party.equals("Republican Party"))
        {
            partyDisplay.setImageResource(R.drawable.rep_logo);
            findViewById(R.id.Layout_Photo).setBackgroundColor(Color.RED);
        }
        else
        {
            partyDisplay.setVisibility(View.INVISIBLE);
            findViewById(R.id.Layout_Photo).setBackgroundColor(Color.BLACK);
        }
    }

}
