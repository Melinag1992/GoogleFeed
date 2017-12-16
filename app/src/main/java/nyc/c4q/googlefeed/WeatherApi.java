package nyc.c4q.googlefeed;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by yokilam on 12/15/17.
 */

public interface WeatherApi {

    @GET("40.743235, -73.941886")
    Call<GetCurrently> getResponse();
}
