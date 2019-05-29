package com.example.weatherapp;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.URL;

public class MainActivity extends AppCompatActivity implements LocationListener {
    private RequestQueue mQueue;
    private String choice;
    protected LocationManager locationManager;
    protected LocationListener locationListener;
    protected Double latitude, longitude;
    private static final int MY_PERMISSIONS_1 = 1;
    private static final int MY_PERMISSIONS_2 = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_1);
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, MY_PERMISSIONS_2);
            return;
        }

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);

        Button gps = findViewById(R.id.button_GPS);
        final Button buttonList = findViewById(R.id.button_listX);

        mQueue = Volley.newRequestQueue(this);

        gps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "http://api.openweathermap.org/data/2.5/weather?lat=" + latitude + "&lon=" + longitude + "&units=metric&appid=0cfc82d6eca370e26b474c674857d3fa";
                jsonParse(url);
            }
        });
        Spinner spinner = findViewById(R.id.spinnerX);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                choice = parent.getItemAtPosition(position).toString();
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        buttonList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(choice.equals("Cracow")) { jsonParse("http://api.openweathermap.org/data/2.5/weather?lat=50.0646501&lon=19.9449799&units=metric&appid=0cfc82d6eca370e26b474c674857d3fa");
                }else if (choice.equals("Warsaw")){ jsonParse("http://api.openweathermap.org/data/2.5/weather?lat=52.2297700&lon=21.0117800&units=metric&appid=0cfc82d6eca370e26b474c674857d3fa");
                }else if (choice.equals("Stalowa Wola")){ jsonParse("http://api.openweathermap.org/data/2.5/weather?lat=50.5828600&lon=22.0533400&units=metric&appid=0cfc82d6eca370e26b474c674857d3fa");
                }else if (choice.equals("Hrubieszów")){ jsonParse("http://api.openweathermap.org/data/2.5/weather?lat=50.8050200&lon=23.8925100&units=metric&appid=0cfc82d6eca370e26b474c674857d3fa");
                }else if (choice.equals("Rzeszów")){ jsonParse("http://api.openweathermap.org/data/2.5/weather?lat=50.0413200&lon=21.9990100&units=metric&appid=0cfc82d6eca370e26b474c674857d3fa");
                }
            }
        });
    }

    public void jsonParse(String url) {

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        TextView loc = (TextView)findViewById(R.id.locationX);
                        TextView tmp = findViewById(R.id.tempX);
                        TextView prs = findViewById(R.id.pressX);
                        TextView wsp = findViewById(R.id.windX);
                        TextView lonX = findViewById(R.id.lonX);
                        TextView latX = findViewById(R.id.latX);
                        TextView nameX = findViewById(R.id.nameX);
                        ImageView iconX = findViewById(R.id.iconX);
                        TextView locO = (TextView)findViewById(R.id.locationX);
                        TextView tmpO = findViewById(R.id.tempXO);
                        TextView prsO = findViewById(R.id.pressXO);
                        TextView wspO = findViewById(R.id.windXO);
                        TextView lonXO = findViewById(R.id.lonXO);
                        TextView latXO = findViewById(R.id.latXO);
                        TextView cooXO = findViewById(R.id.cooXO);

                        try {
                            JSONArray array = response.getJSONArray("weather");
                            JSONObject weather = array.getJSONObject(0);
                            JSONObject main = response.getJSONObject("main");
                            JSONObject wind = response.getJSONObject("wind");
                            JSONObject coordinates = response.getJSONObject("coord");
                            String location = response.getString("name");

                            String icon_name = weather.getString("icon");
                            String url = "http://openweathermap.org/img/w/" + icon_name + ".png";
                            String temp = main.getString("temp");
                            String pressure = main.getString("pressure");
                            String wind_speed = wind.getString("speed");
                            String lon = coordinates.getString("lon");
                            String lat = coordinates.getString("lat");

                            new DownLoadImageTask(iconX).execute(url);
                            loc.setText(location);
                            tmp.setText(temp + " \u00b0C");
                            prs.setText(pressure + " hPa");
                            wsp.setText(wind_speed + " m/s");
                            lonX.setText(lon);
                            latX.setText(lat);

                            nameX.setVisibility(TextView.INVISIBLE);
                            loc.setVisibility(TextView.VISIBLE);
                            tmp.setVisibility(TextView.VISIBLE);
                            prs.setVisibility(TextView.VISIBLE);
                            wsp.setVisibility(TextView.VISIBLE);
                            lonX.setVisibility(TextView.VISIBLE);
                            latX.setVisibility(TextView.VISIBLE);
                            locO.setVisibility(TextView.VISIBLE);
                            tmpO.setVisibility(TextView.VISIBLE);
                            prsO.setVisibility(TextView.VISIBLE);
                            wspO.setVisibility(TextView.VISIBLE);
                            lonXO.setVisibility(TextView.VISIBLE);
                            latXO.setVisibility(TextView.VISIBLE);
                            cooXO.setVisibility(TextView.VISIBLE);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        mQueue.add(request);
    }

    @Override
    public void onLocationChanged(Location location) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    private class DownLoadImageTask extends AsyncTask<String,Void, Bitmap> {
        ImageView imageView;

        public DownLoadImageTask(ImageView imageView){
            this.imageView = imageView;
        }

        protected Bitmap doInBackground(String...urls){
            String urlOfImage = urls[0];
            Bitmap logo = null;
            try{
                InputStream is = new URL(urlOfImage).openStream();
                logo = BitmapFactory.decodeStream(is);
            }catch(Exception e){
                e.printStackTrace();
            }
            return logo;
        }
        protected void onPostExecute(Bitmap result){
            imageView.setImageBitmap(result);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_1: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {
                    TextView loc = (TextView)findViewById(R.id.locationX);
                    loc.setText("Location not granted!");
                }
                return;
            }
            case MY_PERMISSIONS_2: {
                if (grantResults.length > 0
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                } else {
                    TextView loc = (TextView)findViewById(R.id.locationX);
                    loc.setText("Location not granted!");
                }
                return;
            }
        }
    }
}