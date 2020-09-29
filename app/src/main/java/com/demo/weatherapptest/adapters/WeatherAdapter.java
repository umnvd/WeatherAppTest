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

public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.WeatherResponseViewHolder> {

    private List<WeatherResponse> weathers = new ArrayList<>();
    private OnWeatherClickListener onWeatherClickListener;

    public List<WeatherResponse> getWeathers() {
        return weathers;
    }

    public void setWeathers(List<WeatherResponse> weathers) {
        this.weathers = weathers;
        notifyDataSetChanged();
    }

    public void setOnWeatherClickListener(OnWeatherClickListener onWeatherClickListener) {
        this.onWeatherClickListener = onWeatherClickListener;
    }

    @NonNull
    @Override
    public WeatherResponseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.city_item, parent, false);
        return new WeatherResponseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WeatherResponseViewHolder holder, int position) {
        WeatherResponse response = weathers.get(position);
        String cityName = response.getInfo().getName();
        String temp = WeatherUtils.getFormattedTemp(response.getFact().getTemp());
        String icon = response.getFact().getIcon();
        int backgroundResource = WeatherUtils.getBackgroundResource(response.getFact().getDaytime());

        holder.textViewCityName.setText(cityName);
        holder.textViewCityCurrentTemp.setText(temp);
        SvgUtils.fetchSvg(holder.imageViewCityCurrentWeatherIcon.getContext(), icon, holder.imageViewCityCurrentWeatherIcon);
        holder.imageViewCityBackground.setImageResource(backgroundResource);
    }

    @Override
    public int getItemCount() {
        return weathers.size();
    }

    public class WeatherResponseViewHolder extends RecyclerView.ViewHolder {

        private TextView textViewCityName;
        private TextView textViewCityCurrentTemp;
        private ImageView imageViewCityCurrentWeatherIcon;
        private ImageView imageViewCityBackground;

        public WeatherResponseViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewCityName = itemView.findViewById(R.id.textViewCityName);
            textViewCityCurrentTemp = itemView.findViewById(R.id.textViewCityCurrentTemp);
            imageViewCityCurrentWeatherIcon = itemView.findViewById(R.id.imageViewCityCurrentWeatherIcon);
            imageViewCityBackground = itemView.findViewById(R.id.imageViewCityBackground);
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
