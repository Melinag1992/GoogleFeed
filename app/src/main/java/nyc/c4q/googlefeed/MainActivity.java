package nyc.c4q.googlefeed;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "mainActivity";
    TextView displayWeather;
    TextView dateDisplay;
    TextView displayTemp;
    ImageView displayIcon;

    private String result= "";


    private static final String WEATHER_API_KEY = "d5730a368a881b4061f35adf65c2da29";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        displayWeather= findViewById(R.id.data);
        dateDisplay= findViewById(R.id.date);
        displayTemp= findViewById(R.id.min_temp);
        displayIcon= findViewById(R.id.weather_image);

        WeatherApi();

    }

    public void WeatherApi() {
//        private String result= "";
        final String  url="https://api.darksky.net/forecast/d5730a368a881b4061f35adf65c2da29/";

        Retrofit retrofit= new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        WeatherApi weatherservice= retrofit.create(WeatherApi.class);

        Call<GetCurrently> currentlyResponse= weatherservice.getResponse();

        currentlyResponse.enqueue(new Callback <GetCurrently>() {
            @Override
            public void onResponse(Call <GetCurrently> call, Response<GetCurrently> response) {

                long unixTime =response.body().getCurrently().getTime();
                Date date= new Date(unixTime*1000L);

                Log.e(TAG, "onResponse: "+ date);

                DateFormat ddf2= DateFormat.getDateInstance(DateFormat.DEFAULT, Locale.ENGLISH);

                Log.e(TAG, "onResponse: "+ ddf2);

                ddf2.setTimeZone(TimeZone.getTimeZone("EST"));
                String format3= ddf2.format(date);
                Log.e(TAG, "onResponse: "+ format3);

                Currently currently= response.body().getCurrently();

                String temp = String.valueOf((int) currently.getTemperature()) + "\u2109\n";

//                double temp= Double.parseDouble(String.valueOf((currently.getTemperature())));
//                double tempF= ((temp - 32)*5)/9;
//                Log.e(TAG, "onResponse: " + tempF );

                result= "Summary: "+currently.getSummary()+"\n"+
                        "Wind Speed: "+currently.getWindSpeed()+"\n"+
                        "Wind Gust: " +currently.getWindGust();

                String icon = currently.getIcon();
                getWeatherIcon(icon);
                displayTemp.setText(temp);
                dateDisplay.setText(format3);
                displayWeather.setText(result);

            }

            @Override
            public void onFailure(Call <GetCurrently> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t );
            }
        });
    }

    public void getWeatherIcon(String icon) {
        switch(icon) {
            case "clear-day":
                displayIcon.setImageResource(R.drawable.clear_day);
                break;
            case "clear-night":
                displayIcon.setImageResource(R.drawable.clear_night);
                break;
            case "rain":
                displayIcon.setImageResource(R.drawable.rain);
                break;
            case "snow":
                displayIcon.setImageResource(R.drawable.snow);
                break;
            case "sleet":
                displayIcon.setImageResource(R.drawable.sleet);
                break;
            case "wind":
                displayIcon.setImageResource(R.drawable.windy);
                break;
            case "fog":
                displayIcon.setImageResource(R.drawable.fog);
                break;
            case "cloudy":
                displayIcon.setImageResource(R.drawable.cloudy);
                break;
            case "partly-cloudy-day":
                displayIcon.setImageResource(R.drawable.partly_cloudy);
                break;
            case "partly-cloudy-night":
                displayIcon.setImageResource(R.drawable.partly_cloudy_night);
                break;
        }
    }

}
