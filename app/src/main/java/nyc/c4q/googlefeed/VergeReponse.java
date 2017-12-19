package nyc.c4q.googlefeed;

import java.util.List;

/**
 * Created by admin on 12/16/17.
 */

public class VergeReponse {


    private String status;
    private int totalResults;
    private List<Articles> articles;

    public List<Articles> getArticle() {
        return articles;
    }

}
