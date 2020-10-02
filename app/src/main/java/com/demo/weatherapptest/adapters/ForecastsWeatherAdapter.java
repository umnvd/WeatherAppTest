package com.demo.weatherapptest.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.weatherapptest.R;
import com.demo.weatherapptest.pojo.Forecast;
import com.demo.weatherapptest.pojo.Part;
import com.demo.weatherapptest.pojo.WeatherResponse;
import com.demo.weatherapptest.utils.SvgUtils;
import com.demo.weatherapptest.utils.WeatherUtils;

import java.util.ArrayList;
import java.util.List;

public class ForecastsWeatherAdapter extends RecyclerView.Adapter<ForecastsWeatherAdapter.ForecastsWeatherViewHolder>{

    private List<Forecast> forecasts = new ArrayList<>();
    private static long offset;

    public void setWeatherResponse(WeatherResponse response) {
        forecasts = response.getForecasts();
        offset = response.getInfo().getTzinfo().getOffset();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ForecastsWeatherViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_short_weather, parent, false);
        return new ForecastsWeatherViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ForecastsWeatherViewHolder holder, int position) {
        Forecast forecast = forecasts.get(position);
        Part dayPart = forecast.getParts().getDay();
        Part nightPart = forecast.getParts().getNight();

        long dateTs = forecast.getDateTs();
        String formattedDate = WeatherUtils.getFormattedDate(dateTs, offset);
        String icon = dayPart.getIcon();
        int maxTemp = dayPart.getTempMax();
        String formattedMaxTemp = WeatherUtils.getFormattedTemp(maxTemp);
        int minTemp = nightPart.getTempMin();
        String formattedMinTemp = WeatherUtils.getFormattedTemp(minTemp);
        String condition = dayPart.getCondition();
        String formattedCondition = WeatherUtils.getFormattedCondition(condition);

        holder.textViewShortDate.setText(formattedDate);
        SvgUtils.fetchSvg(holder.imageViewShortWeatherIcon.getContext(), icon, holder.imageViewShortWeatherIcon);
        holder.textViewShortMaxTemp.setText(formattedMaxTemp);
        holder.textViewShortMinTemp.setText(formattedMinTemp);
        holder.textViewShortCondition.setText(formattedCondition);
    }

    @Override
    public int getItemCount() {
        return forecasts.size();
    }

    public class ForecastsWeatherViewHolder extends RecyclerView.ViewHolder {

        private TextView textViewShortDate;
        private ImageView imageViewShortWeatherIcon;
        private TextView textViewShortMaxTemp;
        private TextView textViewShortMinTemp;
        private TextView textViewShortCondition;

        public ForecastsWeatherViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewShortDate = itemView.findViewById(R.id.textViewShortDate);
            imageViewShortWeatherIcon = itemView.findViewById(R.id.imageViewShortWeatherIcon);
            textViewShortMaxTemp = itemView.findViewById(R.id.textViewShortMaxTemp);
            textViewShortMinTemp = itemView.findViewById(R.id.textViewShortMinTemp);
            textViewShortCondition = itemView.findViewById(R.id.textViewShortCondition);

        }
    }
}
