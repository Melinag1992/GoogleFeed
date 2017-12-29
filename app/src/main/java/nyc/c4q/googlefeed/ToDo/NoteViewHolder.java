package nyc.c4q.googlefeed.ToDo;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import nyc.c4q.googlefeed.R;

/**
 * Created by yokilam on 12/24/17.
 */

public class NoteViewHolder extends RecyclerView.ViewHolder {

    private TextView title;
    private TextView detail;

    public NoteViewHolder(View itemView) {
        super(itemView);

        title= itemView.findViewById(R.id.title_textview);
        detail= itemView.findViewById(R.id.detail_textview);
    }


    public void bind(Note note) {
        title.setText(note.getTitle());
        detail.setText(note.getDescription());
    }
}
