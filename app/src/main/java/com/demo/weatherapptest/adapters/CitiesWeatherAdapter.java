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
import com.demo.weatherapptest.utils.WeatherUtils;

import java.util.ArrayList;
import java.util.List;

public class CitiesWeatherAdapter extends RecyclerView.Adapter<CitiesWeatherAdapter.WeatherResponseViewHolder> {

    private List<WeatherResponse> weathers = new ArrayList<>();
    private OnWeatherClickListener onWeatherClickListener;

    private int dayIndex = 0;
    private int nightIndex = 1;

    public List<WeatherResponse> getWeathers() {
        return weathers;
    }

    public void setWeathers(List<WeatherResponse> weathers) {
        this.weathers = weathers;
        notifyDataSetChanged();
    }

    public void setWhen(boolean when) {
        if (when) { // tomorrow
            dayIndex = 1;
            nightIndex = 2;
        } else { // today
            dayIndex = 0;
            nightIndex = 1;
        }
        notifyDataSetChanged();
    }

    public void setOnWeatherClickListener(OnWeatherClickListener onWeatherClickListener) {
        this.onWeatherClickListener = onWeatherClickListener;
    }

    @NonNull
    @Override
    public WeatherResponseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_city_weather, parent, false);
        return new WeatherResponseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WeatherResponseViewHolder holder, int position) {
        WeatherResponse response = weathers.get(position);
        String cityName = response.getInfo().getName();
        int dayTemp = response.getForecasts().get(dayIndex).getParts().getDay().getTempAvg();
        String formattedDayTemp = WeatherUtils.getFormattedTemp(dayTemp);
        int nightTemp = response.getForecasts().get(nightIndex).getParts().getNight().getTempMin();
        String formattedNightTemp = WeatherUtils.getFormattedTemp(nightTemp);
        String icon = response.getForecasts().get(dayIndex).getParts().getDay().getIcon();

        holder.textViewCityName.setText(cityName);
        holder.textViewCityDayTemp.setText(formattedDayTemp);
        holder.textViewCityNightTemp.setText(formattedNightTemp);
        SvgUtils.fetchSvg(holder.imageViewCityCurrentWeatherIcon.getContext(), icon, holder.imageViewCityCurrentWeatherIcon);
    }

    @Override
    public int getItemCount() {
        return weathers.size();
    }

    public class WeatherResponseViewHolder extends RecyclerView.ViewHolder {

        private TextView textViewCityName;
        private TextView textViewCityDayTemp;
        private TextView textViewCityNightTemp;
        private ImageView imageViewCityCurrentWeatherIcon;

        public WeatherResponseViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewCityName = itemView.findViewById(R.id.textViewCityName);
            textViewCityDayTemp = itemView.findViewById(R.id.textViewCityDayTemp);
            textViewCityNightTemp = itemView.findViewById(R.id.textViewCityNightTemp);
            imageViewCityCurrentWeatherIcon = itemView.findViewById(R.id.imageViewCityCurrentWeatherIcon);
            itemView.setOnClickListener(view -> {
                if (onWeatherClickListener != null) {
                    onWeatherClickListener.onWeatherClick(getAdapterPosition());
                }
            });

        }
    }

    public interface OnWeatherClickListener {
        void onWeatherClick(int position);
    }
}
