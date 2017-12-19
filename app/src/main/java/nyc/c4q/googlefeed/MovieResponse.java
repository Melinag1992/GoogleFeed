package nyc.c4q.googlefeed;

import java.util.List;

/**
 * Created by yokilam on 12/11/17.
 */

public class MovieResponse {

    private int page;
    private List<Movie> results;
    private int totalResults;

    public int getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    private int totalPages;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public List <Movie> getResults() {
        return results;
    }

    public void setResults(List <Movie> results) {
        this.results = results;
    }


}
