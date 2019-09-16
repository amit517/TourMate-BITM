package com.amitKundu.tourmate;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.amitKundu.tourmate.CurrentWeather.WeatherResponse;
import com.amitKundu.tourmate.Weither.WeatherResult;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WeatherActivity extends AppCompatActivity {

    private TextView currentWeatherDiscription, currentWeathertemp, currentWeatherWind, currentWeatherLocatonTv,currentWeatherHumidity;
    private ImageView currentWeatherIcon;

    private RecyclerView recyclerView;
    private WeitherAdapter weatherAdapter;
    private WeatherResult weatherResult;
    private ImageView backWIv;
    private WeatherResult currentWeatherResult;
    private double lat = 0;
    private double lon = 0;
    private ProgressDialog loadinbar;
    private String units = "metric";
    String url;
    String url1;

    FusedLocationProviderClient fusedLocationProviderClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        loadinbar = new ProgressDialog(WeatherActivity.this);
        currentWeatherDiscription = findViewById(R.id.cityNameCurrentTvId);
        currentWeatherIcon = findViewById(R.id.weatherCurrentIconIvId);
        currentWeathertemp = findViewById(R.id.tempCurrentWeitherTvId);
        currentWeatherWind = findViewById(R.id.windCurrentWeitherTvId);
        currentWeatherHumidity = findViewById(R.id.humidityCurrentWeitherTvId);
        currentWeatherLocatonTv = findViewById(R.id.cityStatusCurrentTvId);
        backWIv =findViewById(R.id.backWId);

        weatherResult = new WeatherResult();
        recyclerView = findViewById(R.id.weatherRecyclerViewId);
            //getLocationPermission();
/////////////permission///////////
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            getMyLocation();

        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }

        backWIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


    }

    private void getMyLocation() {

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        Task<Location> locationTask = fusedLocationProviderClient.getLastLocation();
        locationTask.addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                if(task.isSuccessful())
                {
                    Location location = task.getResult();
                    url1 = String.format("weather?lat=%f&lon=%f&units=%s&appid=%s", location.getLatitude(), location.getLongitude(), units, getResources().getString(R.string.appid1));
                    url =String.format("forecast?lat=%f&lon=%f&units=%s&appid=%s",location.getLatitude(),location.getLongitude(),units,getResources().getString(R.string.appid));
                   // Toast.makeText(WeatherActivity.this, String.valueOf(location.getLatitude()), Toast.LENGTH_SHORT).show();
                    loadinbar.setTitle("Weather");
                    loadinbar.setMessage("Loading 5 days weather");
                    loadinbar.show();
                    loadinbar.setCanceledOnTouchOutside(true);
                    getWeatherUpdate();
                }

            }
        });

    }


    private void getWeatherUpdate() {

        IOpenWeatherMap iOpenWeatherMap= RetrofitClass.getRetrofitInstance().create(IOpenWeatherMap.class);

//        String url =String.format("forecast?lat=%f&lon=%f&units=%s&appid=%s",lat,lon,units,getResources().getString(R.string.appid));
       //"forecast?lat=23.7533312&lon=90.3769738&units=metric&appid=a0e0d52b2dbb8228d3f19466bb398fd0"


        Call<WeatherResult> weatherResultCall= iOpenWeatherMap.getWeatherData(url);

        weatherResultCall.enqueue(new Callback<WeatherResult>() {
            @Override
            public void onResponse(Call<WeatherResult> call, Response<WeatherResult> response) {
                if(response.code()==200)
                {
                    weatherResult = response.body();
                    currentWeatherResult = weatherResult;
                    Toast.makeText(WeatherActivity.this, "success", Toast.LENGTH_SHORT).show();
                    weatherAdapter = new WeitherAdapter(WeatherActivity.this,weatherResult);
                    recyclerView.setLayoutManager(new LinearLayoutManager(WeatherActivity.this));
                    recyclerView.setAdapter(weatherAdapter);

                    loadinbar.dismiss();
                    //Toast.makeText(WeatherActivity.this, ""+weatherResult.getCity().getCountry(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<WeatherResult> call, Throwable t) {

            }
        });


        IOpenWeatherMap weatherService= RetrofitClass.getRetrofitInstance().create(IOpenWeatherMap.class);
        Call<WeatherResponse> weatherResponseCall = weatherService.getWeatherData1(url1);
        weatherResponseCall.enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {

                if(response.code()==200)
                {
                    WeatherResponse weatherResponse = response.body();
                    currentWeathertemp.setText(String.valueOf(weatherResponse.getMain().getTemp())+"°C");
                    currentWeatherLocatonTv.setText(String.valueOf(weatherResponse.getName()));
                    currentWeatherDiscription.setText(String.valueOf(weatherResponse.getWeather().get(0).getDescription()));
                    currentWeatherHumidity.setText("Humidity: "+(String.valueOf(weatherResponse.getMain().getHumidity()))+"%");
                    currentWeatherWind.setText("Wind       : "+(String.valueOf(weatherResponse.getWind().getSpeed()))+"km/h");
                    Picasso.get().load(new StringBuilder("https://openweathermap.org/img/w/")
                            .append(weatherResponse.getWeather().get(0).getIcon())
                            .append(".png").toString()).into(currentWeatherIcon);
                }

            }

            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable t) {


            }
        });
    }
}
