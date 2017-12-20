package nyc.c4q.googlefeed;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import android.widget.Switch;

import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private TextView displayWeather;
    private TextView dateDisplay;
    private TextView displayTemp;
    private TextView titleTextview;
    private TextView description;
    private ImageView newsImage;
    private ImageView displayIcon;
    private EditText search;
    private String result = "";
    private String articleImage;
    private String articleDescription;
    private String articleTitle;
    private static final String TAG= MainActivity.class.getSimpleName();
    private final static String API_KEY= "f3887b19f7bec24ad815dde137f8f6a1";
    private List<Movie> upcomingMovies= new ArrayList<>();
    private RecyclerView rv;

    private static final String WEATHER_API_KEY = "d5730a368a881b4061f35adf65c2da29";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setBuzzFeedViews();
        views();
        weatherApi();
        buzzfeedApi();
        search();

        if (API_KEY.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Please check your API KEY", Toast.LENGTH_LONG).show();
        }

        rv= findViewById(R.id.upcoming_rv);
        rv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        getRetrofit();

    }

    public void views(){
        displayWeather = findViewById(R.id.data);
        dateDisplay = findViewById(R.id.date);
        displayTemp = findViewById(R.id.min_temp);
        titleTextview = findViewById(R.id.titletext);
        displayIcon= findViewById(R.id.weather_image);
        search = findViewById(R.id.search_bar);
    }
    public void weatherApi() {

        final String url = "https://api.darksky.net/forecast/d5730a368a881b4061f35adf65c2da29/";

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        WeatherApi weatherservice = retrofit.create(WeatherApi.class);

        Call<GetCurrently> currentlyResponse = weatherservice.getResponse();
        currentlyResponse.enqueue(new Callback<GetCurrently>() {
            @Override
            public void onResponse(Call<GetCurrently> call, Response<GetCurrently> response) {

                long unixTime = response.body().getCurrently().getTime();
                Date date = new Date(unixTime * 1000L);

                Log.e(TAG, "onResponse: " + date);

                DateFormat ddf2 = DateFormat.getDateInstance(DateFormat.DEFAULT, Locale.ENGLISH);

                Log.e(TAG, "onResponse: " + ddf2);

                ddf2.setTimeZone(TimeZone.getTimeZone("EST"));
                String format3 = ddf2.format(date);
                Log.e(TAG, "onResponse: " + format3);

                Currently currently = response.body().getCurrently();

                String temp = String.valueOf((int) currently.getTemperature()) + "\u2109\n";

                result = "Summary: " + currently.getSummary() + "\n" +
                        "Wind Speed: " + currently.getWindSpeed() + "\n" +
                        "Wind Gust: " + currently.getWindGust();

                String icon = currently.getIcon();
                getWeatherIcon(icon);
                displayTemp.setText(temp);
                dateDisplay.setText(format3);
                displayWeather.setText(result);

            }

            @Override
            public void onFailure(Call<GetCurrently> call, Throwable t) {
                Log.d(TAG, "onFailure: "+ t.toString());
            }
        });
    }

    public void setBuzzFeedViews(){
        titleTextview = findViewById(R.id.titletext);
       // description = findViewById(R.id.description_textView);
        newsImage = findViewById(R.id.imageView);

    }

    public void buzzfeedApi() {

        final String url = "https://newsapi.org/";

        Retrofit buzzFeedRetroFit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        BuzzfeedfeedApi buzzfeedtopStories = buzzFeedRetroFit.create(BuzzfeedfeedApi.class);

        Call<VergeReponse> vergeArticleResponse = buzzfeedtopStories.getVergeArticlesResponse("the-next-web,the-verge", "86df0f155fd140709ce109d2f7555cb5");
        vergeArticleResponse.enqueue(new Callback<VergeReponse>() {
            @Override
            public void onResponse(Call<VergeReponse> call, Response<VergeReponse> response) {

               articleTitle =  response.body().getArticle().get(0).getTitle();
             //  articleDescription = response.body().getArticle().get(0).getDescription();
               articleImage = response.body().getArticle().get(0).getUrltoimage();

                    titleTextview.setText(articleTitle);
                   // description.setText(articleDescription);
                    Picasso.with(getApplicationContext())
                        .load(articleImage)
                            .resize(400,400)
                            .centerCrop()
                            .placeholder(R.drawable.ic_launcher_background)
                            .error(R.drawable.ic_launcher_background)
                        .into(newsImage);
            }

            @Override
            public void onFailure(Call <VergeReponse> call, Throwable t) {
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

    private void getRetrofit() {
        final String BASE_URL = "http://api.themoviedb.org/3/";
        Retrofit retrofit= new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        MovieService movieService = retrofit.create(MovieService.class);

        final Call<MovieResponse> upcomingM= movieService.getUpcomingMovies(API_KEY);
        upcomingM.enqueue(new Callback <MovieResponse>() {
            @Override
            public void onResponse(Call <MovieResponse> call, Response<MovieResponse> response) {
                upcomingMovies=response.body().getResults();
                rv.setAdapter(new upComigMoviesAdapter(upcomingMovies));
            }

            @Override
            public void onFailure(Call <MovieResponse> call, Throwable t) {
                Log.d(TAG, "onFailure: "+ t.toString());
            }
        });
    }

    public void search(){
        search.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(event.getAction() == event.ACTION_DOWN ) {
                    if((keyCode == event.KEYCODE_ENTER || keyCode == event.KEYCODE_TAB)&& !TextUtils.isEmpty(search.getText().toString())){
                        String searchText= search.getText().toString();
                        Intent intent= new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse("https://www.google.com/search?q=" + searchText));
                        startActivity(intent);
                        search.setText("");
                    }
                }
                return false;
            }
        });
    }


}


