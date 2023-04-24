package com.example.civicadvocacyapp;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements  View.OnClickListener{

    public static String location = "Chicago,IL";

    public TextView LocationText;

    private RecycleViewAdapter adapter;
    private LinearLayoutManager linearLayoutManager;
    private RecyclerView recyclerView;

    private ActivityResultLauncher<Intent> aboutActivityResultLauncher;
    private ActivityResultLauncher<Intent> officialActivityResultLauncher;

    private ArrayList<Official> officials;

    private FusedLocationProviderClient mFusedLocationClient;
    private static final int LOCATION_REQUEST = 111;

    private Menu Menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LocationText = findViewById(R.id.LocationDisplay_Main);
        LocationText.setText(location);

        aboutActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), this::handleAboutActivityResult);
        officialActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), this::handleOfficialActivityResult);

        officials = new ArrayList<>();
        recyclerView = findViewById(R.id.OfficialsRecycler);
        adapter = new RecycleViewAdapter(officials ,this);
        recyclerView.setAdapter(adapter);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        //TODO Get user's current location
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        determineLocation();

        //TODO populate recycler
        API.getSource(this);
    }

    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        Menu = menu;
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.About) {
            Intent intent = new Intent(this, AboutActivity.class);
            aboutActivityResultLauncher.launch(intent);
        }
        else if(item.getItemId() == R.id.Location)
        {
            AskLocation();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view)
    {
        int pos = recyclerView.getChildLayoutPosition(view);
        Official official = officials.get(pos);

        Intent intent = new Intent(this, OfficialActivity.class);
        intent.putExtra("OFFICIAL", official);

        officialActivityResultLauncher.launch(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == LOCATION_REQUEST) {
            if (permissions[0].equals(Manifest.permission.ACCESS_FINE_LOCATION)) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    determineLocation();
                }
                else
                {
                    LocationText.setText("Unable to retrieve data without location permission");
                }
            }
        }
    }

    public void handleAboutActivityResult(ActivityResult result) {
        Toast.makeText(this, "Returned from About Page", Toast.LENGTH_SHORT).show();
    }

    public void handleOfficialActivityResult(ActivityResult result) {
        Toast.makeText(this, "Returned from Official Page", Toast.LENGTH_SHORT).show();
    }

    private void determineLocation() {
        // Check perm - if not then start the  request and return
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST);
            return;
        }
        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, getLocation -> {
                    // Got last known location. In some situations this can be null.
                    if (getLocation != null) {
                        location = getPlace(getLocation);
//                        LocationText.setText(location);
                    }
                })
                .addOnFailureListener(this, e ->
                        Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show());
    }

    private String getPlace(Location loc) {

        StringBuilder sb = new StringBuilder();

        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses;

        try {
            addresses = geocoder.getFromLocation(loc.getLatitude(), loc.getLongitude(), 1);
            sb.append(addresses.get(0).getAddressLine(0));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    private void AskLocation() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        final EditText et = new EditText(this);
        et.setInputType(InputType.TYPE_CLASS_TEXT);
        et.setGravity(Gravity.CENTER_HORIZONTAL);
        builder.setView(et);

        builder.setPositiveButton("OK", (dialog, id) -> {
            location = et.getText().toString();
//            LocationText.setText(location);
            API.getSource(this);
//            setTitle(location);
        });

        builder.setNegativeButton("CANCEL", (dialog, id) -> {});

        builder.setTitle("Enter Address");

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void downloadFailed() {
        officials.clear();
        adapter.notifyItemRangeChanged(0, officials.size());
    }

    public void updateRecyclerData(ArrayList<Official> items) {
        officials.clear();
        adapter.notifyItemRangeChanged(0, officials.size());
        officials.addAll(items);
        adapter.notifyItemRangeChanged(0, officials.size());

    }
}