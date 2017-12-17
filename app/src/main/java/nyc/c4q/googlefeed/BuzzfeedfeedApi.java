package nyc.c4q.googlefeed;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by admin on 12/16/17.
 *
 * ?&apiKey={api_key}
 */

interface BuzzfeedfeedApi {


    @GET("/v2/top-headlines")
    Call<VergeReponse> getVergeArticlesResponse(@Query("sources") String sources, @Query("apiKey") String apiKey);


}
