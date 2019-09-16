package com.amitKundu.tourmate;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.amitKundu.tourmate.Weither.WeatherResult;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;

public class WeitherAdapter extends RecyclerView.Adapter<WeitherAdapter.ViewGroup> {

    Context context;
    WeatherResult weatherResult;
    private ProgressDialog loadinbar;

    public WeitherAdapter(Context context, WeatherResult weatherResult) {
        this.context = context;
        this.weatherResult = weatherResult;
    }

    @NonNull
    @Override
    public ViewGroup onCreateViewHolder(@NonNull android.view.ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.weither_items, viewGroup, false);

        return new ViewGroup(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewGroup viewGroup, int i) {
        loadinbar = new ProgressDialog(context);

        Picasso.get().load(new StringBuilder("https://openweathermap.org/img/w/")
                .append(weatherResult.getList().get(i).getWeather().get(0).getIcon())
                .append(".png").toString()).into(viewGroup.weitherIcon);

        SimpleDateFormat dateandTimeSDF = new SimpleDateFormat("dd MMMM yyyy");

        Date date = new Date();
        date.setDate((weatherResult.getList().get(i).getDt()));
        viewGroup.weitherDate.setText("Date   : " + weatherResult.getList().get(i).getDt_txt());

//        viewGroup.weitherDate.setText("Date   : " + weatherResult.getList().get(i).getDt());


        viewGroup.weitherDescription.setText("Status : " + weatherResult.getList().get(i).getWeather().get(0).getDescription());
        viewGroup.weitherTemp.setText(("Temp  : " + weatherResult.getList().get(i).getMain().getTemp() + " °C"));
        viewGroup.weitherWind.setText("Wind : " + weatherResult.getList().get(i).getWind().getSpeed() + " km/h");
        loadinbar.dismiss();
        //viewGroup.weatherLoactionTv.setText(""+weatherResult.getCity().getCountry());
        //viewGroup.weitherHumidity.setText(("Humidity :"+weatherResult.getList().get(i).getMain().getHumidity()+" %"));
    }

    @Override
    public int getItemCount() {
        return weatherResult.getList().size();
    }

    public class ViewGroup extends RecyclerView.ViewHolder {
        private ImageView weitherIcon;
        private TextView weitherDate, weitherTemp, weitherWind, weitherHumidity, weitherDescription, weatherLoactionTv;

        public ViewGroup(@NonNull View itemView) {
            super(itemView);

            weitherIcon = itemView.findViewById(R.id.weatherItemIvId);
            weitherDate = itemView.findViewById(R.id.dateWeitherItemTvId);
            weitherTemp = itemView.findViewById(R.id.tempWeitherItemTvId);
            weitherWind = itemView.findViewById(R.id.windWeitherItemTvId);
            weitherDescription = itemView.findViewById(R.id.weitherDiscriptionTvId);
            //weatherLoactionTv = itemView.findViewById(R.id.cityStatusCurrentTvId);
            //weitherHumidity = itemView.findViewById(R.id.humidityWeitherItemTvId);


        }
    }
}

