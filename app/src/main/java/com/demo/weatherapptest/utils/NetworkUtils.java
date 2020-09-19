package com.demo.weatherapptest.utils;

import android.content.Context;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;
import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class NetworkUtils {

    private static final String API_HEADER = "X-Yandex-API-Key";
    private static final String API_KEY = "e3131f67-a5d8-4094-a587-0bfe876fb1bc";

    private static final String BASE_URL = "https://api.weather.yandex.ru/v2/forecast";

    public static URL getUrlByParams(double lat, double lon, int days) {
        URL result = null;
        Uri uri = Uri.parse(BASE_URL).buildUpon()
                .appendQueryParameter("lat", Double.toString(lat))
                .appendQueryParameter("lon", Double.toString(lon))
                .appendQueryParameter("lang", "ru-RU")
                .appendQueryParameter("limit", Integer.toString(days))
                .appendQueryParameter("hours", "true")
                .appendQueryParameter("extra", "true")
                .build();
        try {
            result = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static JSONObject getJSONByURL(URL url) {
        if (url == null) {
            return null;
        }
        JSONObject result = null;
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty(API_HEADER, API_KEY);
            Scanner scanner = new Scanner(connection.getInputStream());
            StringBuilder builder = new StringBuilder();
            while (scanner.hasNextLine()) {
                builder.append(scanner.nextLine());
            }
            result = new JSONObject(builder.toString());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        return result;
    }

    public static Observable<JSONObject> getObservable(URL url) {
        return Observable.fromCallable(() -> {
            if (url == null) {
                return null;
            }
            JSONObject result = null;
            HttpURLConnection connection = null;
            try {
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setRequestProperty(API_HEADER, API_KEY);
                Scanner scanner = new Scanner(connection.getInputStream());
                StringBuilder builder = new StringBuilder();
                while (scanner.hasNextLine()) {
                    builder.append(scanner.nextLine());
                }
                result = new JSONObject(builder.toString());
            /*} catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();*/
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
            return result;
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
