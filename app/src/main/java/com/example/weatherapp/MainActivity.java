package com.example.weatherapp;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.concurrent.locks.ReadWriteLock;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {

    LinearLayoutCompat linearLayoutCompat;
    ImageView imageView;
    TextView textView;
    CardView cardView;
    private LocationManager locationManager;
    private Location currentLocation;
    final String APP_ID = "12cc9b7c2eed9b785313c7d6b109fdb7";
    final String WEATHER_URL = "https://api.openweathermap.org/data/2.5/weather";

    final long MIN_TIME = 5000;
    final float MIN_DISTANCE = 1000;
    final int REQUEST_CODE = 101;
    TextView NameofCity, weatherState, Temperature, MinTemperature, MaxTemperature; //additional code (Min temp)
    ImageView mweatherIcon;

    LinearLayoutCompat mCityFinder, mMainBackground;
    String Location_Provider = LocationManager.GPS_PROVIDER;
    LocationManager mLocationManager;
    LocationListener mLocationListner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


//        ImageView imageView = findViewById(R.id.imageView1);
//        imageView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(MainActivity.this, "Click on Image", Toast.LENGTH_SHORT).show();
//            }
//        });

        linearLayoutCompat = findViewById(R.id.LocationLinearLayout);
        linearLayoutCompat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "Click on linear layout", Toast.LENGTH_SHORT).show();
            }
        });

        imageView = findViewById(R.id.settingIcon);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "Clicked on setting app", Toast.LENGTH_SHORT).show();
            }
        });

//        BACKGROUND CHANGE ON THE BASIS OF TEMPERATURE
//        linearLayoutCompat = findViewById(R.id.main_background);
//        cardView = findViewById(R.id.cardView0);
//        imageView = findViewById(R.id.TemperatureCenterIcon);
//        textView = findViewById(R.id.TemperatureName);
//        if (textView == "Sunny"){
//
//        } else if (textView == "Storm") {
//            linearLayoutCompat.setBackgroundColor(getResources().getColor(R.color.UI_DARK_PURPLE));
//        }

//        Changed Image and Colors

//        linearLayoutCompat.setBackgroundColor(getResources().getColor(R.color.UI_DARK_PURPLE));
//
//        cardView.setCardBackgroundColor(getResources().getColor(R.color.UI_PURPLE_TRANSPARENT));
//
//        String uri = "@drawable/outline_thunderstorm_24";
//        int imageResource = getResources().getIdentifier(uri, null, getPackageName());
//
//        imageView = (ImageView)findViewById(R.id.TemperatureCenterIcon);
//        Drawable res = getResources().getDrawable(imageResource);
//        imageView.setImageDrawable(res);


//        Location Function
//
//        FusedLocationProviderClient mfusedLocationClient;
//        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);


        weatherState = findViewById(R.id.TemperatureCondition);
        Temperature = findViewById(R.id.TemperatureDigit);
        mweatherIcon = findViewById(R.id.TemperatureCenterIcon);
        mCityFinder = findViewById(R.id.LocationLinearLayout);
        NameofCity = findViewById(R.id.LocationText);
        mMainBackground = findViewById(R.id.main_background);
        MinTemperature = findViewById(R.id.todayMinTemp); //Additional Code
        MaxTemperature = findViewById(R.id.todayMaxTemp); //Additional code

        mCityFinder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, cityFinder.class);
                startActivity(intent);
            }
        });

    }

 /*   @Override
   protected void onResume() {
       super.onResume();
       getWeatherForCurrentLocation();
    }*/

    @Override
    protected void onResume() {
        super.onResume();
        Intent mIntent=getIntent();
        String city= mIntent.getStringExtra("City");
        if(city!=null)
        {
            getWeatherForNewCity(city);
        }
        else
        {
            getWeatherForCurrentLocation();
        }


    }


    private void getWeatherForNewCity(String city)
    {
        RequestParams params=new RequestParams();
        params.put("q",city);
        params.put("appid",APP_ID);
        letsdoSomeNetworking(params);

    }




    private void getWeatherForCurrentLocation() {

        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        mLocationListner = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                String Latitude = String.valueOf(location.getLatitude());
                String Longitude = String.valueOf(location.getLongitude());

                RequestParams params =new RequestParams();
                params.put("lat" ,Latitude);
                params.put("lon",Longitude);
                params.put("appid",APP_ID);
                letsdoSomeNetworking(params);




            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {
                //not able to get location
            }
        };


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_CODE);
            return;
        }
        mLocationManager.requestLocationUpdates(Location_Provider, MIN_TIME, MIN_DISTANCE, mLocationListner);

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);


        if(requestCode==REQUEST_CODE)
        {
            if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED)
            {
                Toast.makeText(MainActivity.this,"Location get Successfully",Toast.LENGTH_SHORT).show();
                getWeatherForCurrentLocation();
            }
            else
            {
                //user denied the permission
            }
        }


    }



    private  void letsdoSomeNetworking(RequestParams params)
    {
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(WEATHER_URL,params,new JsonHttpResponseHandler()
        {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                Toast.makeText(MainActivity.this,"Data Get Success",Toast.LENGTH_SHORT).show();

                weatherData weatherD=weatherData.fromJson(response);
                updateUI(weatherD);


                // super.onSuccess(statusCode, headers, response);
            }


            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                //super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });



    }

    private  void updateUI(weatherData weather){




        Temperature.setText(weather.getmTemperature());
        NameofCity.setText(weather.getMcity());
        weatherState.setText(weather.getmWeatherType());
        int resourceID=getResources().getIdentifier(weather.getMicon(),"drawable",getPackageName());
        mweatherIcon.setImageResource(resourceID);

        MinTemperature.setText(weather.getMinTemperature()); // Addition code (Min temp)
        MaxTemperature.setText(weather.getMaxTemperature()); // Additional code (Max temp)

//        if (weather == )
//        int resorceId = getResources().getIdentifier(weather.getMicon(), "drawable", getPackageName());
//        mMainBackground.setBackgroundColor(resorceId);


    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mLocationManager!=null)
        {
            mLocationManager.removeUpdates(mLocationListner);
        }
    }

}