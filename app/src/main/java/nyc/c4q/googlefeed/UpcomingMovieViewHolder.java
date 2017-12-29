package nyc.c4q.googlefeed;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

/**
 * Created by yokilam on 12/17/17.
 */

class UpcomingMovieViewHolder extends RecyclerView.ViewHolder {

    private TextView title;
    private ImageView image;
    private Movie movieObject;

    public UpcomingMovieViewHolder(View view) {
        super(view);

        title= view.findViewById(R.id.movie_title);
        image= view.findViewById(R.id.movie_poster);
    }

    public void bind(Movie movie) {
//        movieObject= movie;
        StringBuilder sb= new StringBuilder();
        sb.append("https://image.tmdb.org/t/p/w500").append(movie.getPosterPath());
        Glide.with(itemView.getContext()).load(sb.toString()).into(image);
        title.setText(movie.getTitle());

//        itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent= new Intent(itemView.getContext(), MovieActivity.class);
//                intent.putExtra("movieObject", movie);
//
//            }
//        });
    }
}
