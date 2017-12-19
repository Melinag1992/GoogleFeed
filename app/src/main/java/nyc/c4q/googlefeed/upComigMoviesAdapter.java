package nyc.c4q.googlefeed;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by yokilam on 12/17/17.
 */

class upComigMoviesAdapter extends RecyclerView.Adapter<UpcomingMovieViewHolder> {

    List<Movie> upcomingMovies;

    public upComigMoviesAdapter(List <Movie> upcomingMovies) {
        this.upcomingMovies = upcomingMovies;
    }

    @Override
    public UpcomingMovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.upcomingmovie_itemview, parent, false);
        return new UpcomingMovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(UpcomingMovieViewHolder holder, int position) {
        holder.bind(upcomingMovies.get(position));

    }

    @Override
    public int getItemCount() {
        return upcomingMovies.size();
    }
}
