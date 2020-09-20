package com.demo.weatherapptest.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.weatherapptest.R;
import com.demo.weatherapptest.pojo.WeatherResponse;
import com.demo.weatherapptest.utils.SvgUtils;

import java.util.ArrayList;
import java.util.List;

public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.WeatherResponseViewHolder> {

    private List<WeatherResponse> responses = new ArrayList<>();

    public void setResponses(List<WeatherResponse> responses) {
        this.responses = responses;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public WeatherResponseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.city_item, parent, false);
        return new WeatherResponseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WeatherResponseViewHolder holder, int position) {
        WeatherResponse response = responses.get(position);
        String cityName = response.getInfo().getCityName();
        String temp = Integer.toString(response.getFact().getTemp());
        String icon = response.getFact().getIcon();

        holder.cityName.setText(cityName);
        holder.cityCurrentTemp.setText(temp);
        SvgUtils.fetchSvg(holder.cityCurrentWeatherIcon.getContext(), icon, holder.cityCurrentWeatherIcon);
    }

    @Override
    public int getItemCount() {
        return responses.size();
    }

    public class WeatherResponseViewHolder extends RecyclerView.ViewHolder {

        private TextView cityName;
        private TextView cityCurrentTemp;
        private ImageView cityCurrentWeatherIcon;

        public WeatherResponseViewHolder(@NonNull View itemView) {
            super(itemView);
            cityName = itemView.findViewById(R.id.cityName);
            cityCurrentTemp = itemView.findViewById(R.id.cityCurrentTemp);
            cityCurrentWeatherIcon = itemView.findViewById(R.id.cityCurrentWeatherIcon);
        }
    }
}
