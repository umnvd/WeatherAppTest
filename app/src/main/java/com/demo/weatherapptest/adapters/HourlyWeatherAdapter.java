package com.demo.weatherapptest.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.weatherapptest.R;
import com.demo.weatherapptest.pojo.Hour;
import com.demo.weatherapptest.utils.SvgUtils;
import com.demo.weatherapptest.utils.WeatherUtils;

import java.util.ArrayList;
import java.util.List;

public class HourlyWeatherAdapter extends RecyclerView.Adapter<HourlyWeatherAdapter.HourlyWeatherViewHolder>{

    private List<Hour> hours = new ArrayList<>();

    public void setHours(List<Hour> hours) {
        this.hours = hours;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public HourlyWeatherViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_hour_weather, parent, false);
        return new HourlyWeatherViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HourlyWeatherViewHolder holder, int position) {
        Hour hour = hours.get(position);
        long hourTs = hour.getHourTs();
        String formattedHour = WeatherUtils.getFormattedHour(hourTs);
        String icon = hour.getIcon();
        int temp = hour.getTemp();
        String formattedTemp = WeatherUtils.getFormattedTemp(temp);

        holder.textViewHourTitle.setText(formattedHour);
        SvgUtils.fetchSvg(holder.imageViewHourWeatherIcon.getContext(), icon, holder.imageViewHourWeatherIcon);
        holder.textViewHourTemp.setText(formattedTemp);
    }

    @Override
    public int getItemCount() {
        return hours.size();
    }


    public class HourlyWeatherViewHolder extends RecyclerView.ViewHolder {

        private TextView textViewHourTitle;
        private ImageView imageViewHourWeatherIcon;
        private TextView textViewHourTemp;

        public HourlyWeatherViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewHourTitle = itemView.findViewById(R.id.textViewHourTitle);
            imageViewHourWeatherIcon = itemView.findViewById(R.id.imageViewHourWeatherIcon);
            textViewHourTemp = itemView.findViewById(R.id.textViewHourTemp);
        }
    }
}
