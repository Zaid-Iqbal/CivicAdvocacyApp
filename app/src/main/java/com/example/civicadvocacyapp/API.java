package com.example.civicadvocacyapp;

import android.location.Address;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class API {

    private static final String RAW_URL = "https://www.googleapis.com/civicinfo/v2/representatives?key=AIzaSyBpP1bA_Mxyn7V8k7tZD53wNUe9othx-6w&address=LOCATION";

    public static void getSource(MainActivity mainActivity) {
        RequestQueue queue = Volley.newRequestQueue(mainActivity);

        String DATA_URL = RAW_URL.replace("LOCATION",MainActivity.location);

        Uri.Builder buildURL = Uri.parse(DATA_URL).buildUpon();
        String urlToUse = buildURL.build().toString();

        Response.Listener<JSONObject> listener =
                response -> handleResults(mainActivity, response.toString());

        Response.ErrorListener error = error1 -> {
//            JSONObject jsonObject;
//            try {
//                jsonObject = new JSONObject(new String(error1.networkResponse.data));
//                handleResultsRecycler(mainActivity, null);
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
        };

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(urlToUse, listener, error);

        // Add the request to the RequestQueue.
        queue.add(jsonObjectRequest);
    }

    private static void handleResults(MainActivity mainActivity, String s)
    {
        if (s == null) {
            mainActivity.downloadFailed();
            return;
        }

        ArrayList<Official> officials = parseJSON(mainActivity, s);
        if (officials != null)
            Toast.makeText(mainActivity, "Loaded Recycler Data.", Toast.LENGTH_SHORT).show();
        mainActivity.updateRecyclerData(officials);
    }

    private static ArrayList<Official> parseJSON(MainActivity mainActivity, String s) {

        ArrayList<Official> returnData = new ArrayList<>();

        try {
            JSONObject data = new JSONObject(s);

            JSONObject normalizedInput = data.getJSONObject("normalizedInput");
            JSONArray offices = data.getJSONArray("offices");
            JSONArray officials = data.getJSONArray("officials");

            String[] addressParts = {"line1", "line2", "city", "state", "zip"};
            String address = "";
            for(String str : addressParts)
            {
                if(normalizedInput.has(str))
                {
                    if(str.equals("city") && address.length() > 1)
                    {
                        address = address.substring(0,address.length()-1) + ", " + normalizedInput.getString(str) + ", ";
                    }
                    else
                    {
                        address += normalizedInput.getString(str) + " ";
                    }
                }
            }

            MainActivity.location = address;
            mainActivity.LocationText.setText(address);

            for(int i = 0; i < offices.length(); i++)
            {
                String title = ((JSONObject)offices.get(i)).getString("name");

                JSONArray officialIndices  = ((JSONObject)offices.get(i)).getJSONArray("officialIndices");

                for(int j = 0; j < officialIndices.length(); j++)
                {
                    int idx = officialIndices.getInt(j);
                    JSONObject jsonOfficial = ((JSONObject)officials.get(idx));

                    String name = jsonOfficial.getString("name");

                    address = "";
                    if(jsonOfficial.has("address"))
                    {
                        JSONObject addressContents = (JSONObject) jsonOfficial.getJSONArray("address").get(0);
                        for (String str : addressParts)
                        {
                            if(addressContents.has(str))
                            {
                                if(str == "city")
                                {
                                    address += "\n" + addressContents.getString(str) + ", ";
                                }
                                else
                                {
                                    address += addressContents.getString(str) + " ";
                                }
                            }
                        }
                    }

                    String party = "";
                    String phone = "";
                    String website = "";
                    String email = "";
                    String pic = "missing";
                    String facebook = "";
                    String twitter = "";
                    String youtube = "";

                    if(jsonOfficial.has("party"))
                    {
                        party = jsonOfficial.getString("party");
                    }
                    if(jsonOfficial.has("phones"))
                    {
                        phone = jsonOfficial.getJSONArray("phones").get(0).toString();
                    }
                    if(jsonOfficial.has("urls"))
                    {
                        website = jsonOfficial.getJSONArray("urls").get(0).toString();
                    }
                    if(jsonOfficial.has("emails"))
                    {
                        email = jsonOfficial.getJSONArray("emails").get(0).toString();
                    }
                    if(jsonOfficial.has("photoUrl"))
                    {
                        pic = jsonOfficial.getString("photoUrl");
                    }
                    if(jsonOfficial.has("channels"))
                    {
                        JSONArray channels = jsonOfficial.getJSONArray("channels");
                        for(int k = 0; k < channels.length(); k++)
                        {
                            if(((JSONObject)channels.get(k)).getString("type").equals("Facebook"))
                            {
                                facebook = ((JSONObject)channels.get(k)).getString("id");
                            }
                            else if(((JSONObject)channels.get(k)).getString("type").equals("Twitter"))
                            {
                                twitter = ((JSONObject)channels.get(k)).getString("id");
                            }
                            else if(((JSONObject)channels.get(k)).getString("type").equals("YouTube"))
                            {
                                youtube = ((JSONObject)channels.get(k)).getString("id");
                            }
                        }
                    }
                    returnData.add(new Official(name, title, address, party, phone, website, email, pic, facebook, twitter, youtube));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return returnData;
    }

}
