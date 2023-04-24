package com.example.civicadvocacyapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.io.Serializable;
import java.util.ArrayList;

public class OfficialActivity extends AppCompatActivity{

    public Official official;

    public TextView locationHeader;
    public TextView nameDisplay;
    public TextView titleDisplay;
    public TextView addressDisplay;
    public TextView phoneDisplay;
    public TextView partyDisplay;
    public TextView emailDisplay;
    public TextView websiteDisplay;
    public ImageView picDisplay;
    public ImageView partyPicDisplay;
    public ImageView facebookDisplay;
    public ImageView twitterDisplay;
    public ImageView youtubeDisplay;

    private ActivityResultLauncher<Intent> photoActivityResultLauncher;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_official);

        locationHeader = findViewById(R.id.LocationDisplay_Official);
        nameDisplay = findViewById(R.id.Name_Official);
        titleDisplay = findViewById(R.id.Title_Official);
        partyDisplay = findViewById(R.id.Party_Official);
        addressDisplay = findViewById(R.id.Address_Official);
        phoneDisplay = findViewById(R.id.Phone_Official);
        emailDisplay = findViewById(R.id.Email_Official);
        websiteDisplay = findViewById(R.id.Website_Official);
        picDisplay = findViewById(R.id.Mugshot_Official);
        partyPicDisplay = findViewById(R.id.PartyPic_Official);
        facebookDisplay = findViewById(R.id.Facebook_Official);
        twitterDisplay = findViewById(R.id.Twitter_Official);
        youtubeDisplay = findViewById(R.id.Youtube_Official);

        photoActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), this::handlePhotoActivityResult);

        if(getIntent().hasExtra("OFFICIAL"))
        {
            official = (Official) getIntent().getSerializableExtra("OFFICIAL");

            locationHeader.setText(MainActivity.location);
            nameDisplay.setText(official.Name);
            titleDisplay.setText(official.Title);

            if (official.Address.isEmpty())
            {
                addressDisplay.setVisibility(View.GONE);
                findViewById(R.id.AddressLabel).setVisibility(View.GONE);

            }
            else
            {
                addressDisplay.setText(official.Address);
            }

            if(official.Phone.isEmpty())
            {
                phoneDisplay.setVisibility(View.GONE);
                findViewById(R.id.PhoneLabel).setVisibility(View.GONE);
            }
            else
                phoneDisplay.setText(official.Phone);

            if(official.Email.isEmpty())
            {
                emailDisplay.setVisibility(View.GONE);
                findViewById(R.id.EmailLabel).setVisibility(View.GONE);
            }
            else
                emailDisplay.setText(official.Email);

            if(official.Website.isEmpty())
            {
                websiteDisplay.setVisibility(View.GONE);
                findViewById(R.id.WebsiteLabel).setVisibility(View.GONE);
            }
            else
            {
                websiteDisplay.setText(official.Website);
            }

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

            if(official.Facebook.isEmpty())
            {
                facebookDisplay.setVisibility(View.GONE);
            }

            if(official.Twitter.isEmpty())
            {
                twitterDisplay.setVisibility(View.GONE);
            }

            if(official.Youtube.isEmpty())
            {
                youtubeDisplay.setVisibility(View.GONE);
            }

            if(official.Party.equals("Democratic Party"))
            {
                partyDisplay.setText(official.Party);
                partyPicDisplay.setImageResource(R.drawable.dem_logo);
                findViewById(R.id.Layout_Official).setBackgroundColor(Color.BLUE);

            }
            else if (official.Party.equals("Republican Party"))
            {
                partyDisplay.setText(official.Party);
                partyPicDisplay.setImageResource(R.drawable.rep_logo);
                findViewById(R.id.Layout_Official).setBackgroundColor(Color.RED);
            }
            else
            {
                partyDisplay.setText("NonPartisan");
                partyPicDisplay.setVisibility(View.INVISIBLE);
                findViewById(R.id.Layout_Official).setBackgroundColor(Color.BLACK);
            }
        }
        else
        {
            Toast.makeText(this, "Failed to Load Object", Toast.LENGTH_SHORT).show();
        }
    }

    public void handlePhotoActivityResult(ActivityResult result) {
        Toast.makeText(this, "Returned from Photo Page", Toast.LENGTH_SHORT).show();
    }

    public void onFacebookClick(View v)
    {
        Intent intent;

        try {
            // get the Twitter app if possible
            getPackageManager().getPackageInfo("com.facebook.katana", 0);
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("fb://facewebmodal/f?href=https://www.facebook.com/" + official.Facebook));
        } catch (Exception e) {
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/" + official.Facebook));
        }
        startActivity(intent);
    }

    public void onTwitterClick(View v)
    {
        Intent intent;
        try {
            // get the Twitter app if possible
            getPackageManager().getPackageInfo("com.twitter.android", 0);
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("twitter://user?screen_name=" + official.Twitter));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        } catch (Exception e) {
            // no Twitter app, revert to browser
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/" + official.Twitter));
        }
        startActivity(intent);
    }

    public void onYoutubeClick(View v)
    {
        Intent intent;
        try {
            // get the Twitter app if possible
            getPackageManager().getPackageInfo("com.google.android.youtube", 0);
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/" + official.Youtube));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        } catch (Exception e) {
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/" + official.Youtube));
        }
    }

    public void onPhotoClick(View v)
    {
        if(official.Pic.equals("missing"))
        {
            return;
        }
        Intent intent = new Intent(this, PhotoActivity.class);
        intent.putExtra("OFFICIAL", (Serializable) official);

        photoActivityResultLauncher.launch(intent);

    }

    public void onPartyClick(View v)
    {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        if(partyDisplay.getText().toString().equals("Democratic Party"))
        {
            intent.setData(Uri.parse("https://democrats.org/"));
        }
        else if(partyDisplay.getText().toString().equals("Republican Party"))
        {
            intent.setData(Uri.parse("https://www.gop.com/"));
        }
        else
        {
            return;
        }
        startActivity(intent);
    }

    public void onAddressClick(View v)
    {
        Intent intent;
        try {
            // get the Twitter app if possible
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("geo:0,0?q=" + Uri.encode(official.Address)));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(this,"Maps is not downloaded on your device", Toast.LENGTH_SHORT);
        }
    }

    public void onPhoneClick(View v) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + official.Phone));

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            Toast.makeText(this,"Phone is not downloaded on your device", Toast.LENGTH_SHORT);
        }
    }

    public void onEmailClick(View v) {
        String[] addresses = new String[]{official.Email};

        Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:"));

        intent.putExtra(Intent.EXTRA_EMAIL, addresses);
//        intent.putExtra(Intent.EXTRA_SUBJECT, "This comes from EXTRA_SUBJECT");
//        intent.putExtra(Intent.EXTRA_TEXT, "Email text body from EXTRA_TEXT...");

        // Check if there is an app that can handle mailto intents
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            Toast.makeText(this,"Phone is not downloaded on your device", Toast.LENGTH_SHORT);
        }
    }

    public void onWebsiteClick(View v) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(official.Website));
        startActivity(intent);
    }

}
