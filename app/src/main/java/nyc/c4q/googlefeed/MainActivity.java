package nyc.c4q.googlefeed;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;

import android.widget.ScrollView;
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

import nyc.c4q.googlefeed.ToDo.ToDoActivity;
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
    private ScrollView sv;
    private CardView movieCard;
    private CardView vergeCard;
    private FloatingActionButton toDo;
    private FloatingActionButton movie;
    private FloatingActionButton verge;
    private String result = "";
    private String articleImage;
    private String articleDescription;
    private String articleTitle;
    private static final String TAG = MainActivity.class.getSimpleName();
    private final static String API_KEY = "f3887b19f7bec24ad815dde137f8f6a1";
    private List <Movie> currentMovies = new ArrayList <>();
    private RecyclerView rv;

    private static final String WEATHER_API_KEY = "d5730a368a881b4061f35adf65c2da29";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        views();
        hideSoftKeyboard();
        setScroll();
        weatherApi();
        buzzfeedApi();
        search();

        if (API_KEY.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Please check your API KEY", Toast.LENGTH_LONG).show();
        }

        toDo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(MainActivity.this, ToDoActivity.class);
                startActivity(intent);
            }
        });

        rv = findViewById(R.id.upcoming_rv);
        rv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        getRetrofit();

    }

    public void views() {
        sv = findViewById(R.id.sv);
        displayWeather = findViewById(R.id.data);
        dateDisplay = findViewById(R.id.date);
        displayTemp = findViewById(R.id.min_temp);
        titleTextview = findViewById(R.id.titletext);
        displayIcon = findViewById(R.id.weather_image);
        search = findViewById(R.id.search_bar);
        movie = findViewById(R.id.movieButton);
        verge = findViewById(R.id.vergeButton);
        toDo = findViewById(R.id.noteButton);
        titleTextview = findViewById(R.id.titletext);
        // description = findViewById(R.id.description_textView);
        newsImage = findViewById(R.id.imageView);
        movieCard = findViewById(R.id.movie_card);
        vergeCard = findViewById(R.id.verge_cardview);
    }

    private void setScroll() {
        movie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sv.smoothScrollTo(0, (int) movieCard.getY());
                Log.d(TAG, "onClick: " + movieCard.getX() + " " + (int) movieCard.getY());
            }
        });
        verge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sv.smoothScrollTo(0, ((int) vergeCard.getY() - 150));
                Log.d(TAG, "onClick: " + vergeCard.getX() + " " + (int) vergeCard.getY());
            }
        });
    }

    public void weatherApi() {

        final String url = "https://api.darksky.net/forecast/d5730a368a881b4061f35adf65c2da29/";

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        WeatherApi weatherservice = retrofit.create(WeatherApi.class);

        Call <GetCurrently> currentlyResponse = weatherservice.getResponse();
        currentlyResponse.enqueue(new Callback <GetCurrently>() {
            @Override
            public void onResponse(Call <GetCurrently> call, Response <GetCurrently> response) {

                long unixTime = response.body().getCurrently().getTime();
                Date date = new Date(unixTime * 1000L);
                Log.e(TAG, "onResponse: " + date);
                DateFormat dateformat = DateFormat.getDateInstance(DateFormat.DEFAULT, Locale.ENGLISH);
                Log.e(TAG, "onResponse: " + dateformat);
                dateformat.setTimeZone(TimeZone.getTimeZone("EST"));
                String format3 = dateformat.format(date);
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
            public void onFailure(Call <GetCurrently> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.toString());
            }
        });
    }

    public void buzzfeedApi() {

        final String url = "https://newsapi.org/";

        Retrofit buzzFeedRetroFit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        BuzzfeedfeedApi buzzfeedtopStories = buzzFeedRetroFit.create(BuzzfeedfeedApi.class);

        Call <VergeReponse> vergeArticleResponse = buzzfeedtopStories.getVergeArticlesResponse("the-next-web,the-verge", "86df0f155fd140709ce109d2f7555cb5");
        vergeArticleResponse.enqueue(new Callback <VergeReponse>() {
            @Override
            public void onResponse(Call <VergeReponse> call, Response <VergeReponse> response) {

               articleTitle =  response.body().getArticle().get(0).getTitle();
               articleImage = response.body().getArticle().get(0).getUrltoimage();

                    titleTextview.setText(articleTitle);
                    Picasso.with(getApplicationContext())
                        .load(articleImage)
                            .resize(200,200)
                            .centerCrop()
                            .placeholder(R.drawable.ic_launcher_background)
                            .error(R.drawable.ic_launcher_background)
                        .into(newsImage);
            }

            @Override
            public void onFailure(Call <VergeReponse> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t);
            }
        });
    }

    public void getWeatherIcon(String icon) {
        switch (icon) {
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
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        MovieService movieService = retrofit.create(MovieService.class);

        final Call <MovieResponse> currentM = movieService.getNowPlayingMovies(API_KEY);
        currentM.enqueue(new Callback <MovieResponse>() {
            @Override
            public void onResponse(Call <MovieResponse> call, Response <MovieResponse> response) {
                currentMovies = response.body().getResults();
                rv.setAdapter(new upComigMoviesAdapter(currentMovies));
            }

            @Override
            public void onFailure(Call <MovieResponse> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.toString());
            }
        });
    }

    public void search() {
        search.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == event.ACTION_DOWN) {
                    if ((keyCode == event.KEYCODE_ENTER || keyCode == event.KEYCODE_TAB) && !TextUtils.isEmpty(search.getText().toString())) {
                        String searchText = search.getText().toString();
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse("https://www.google.com/search?q=" + searchText));
                        startActivity(intent);
                        search.setText("");
                    }
                }
                return false;
            }
        });
    }

    public void hideSoftKeyboard() {
        this.getWindow().setSoftInputMode(WindowManager.
                LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }
}


