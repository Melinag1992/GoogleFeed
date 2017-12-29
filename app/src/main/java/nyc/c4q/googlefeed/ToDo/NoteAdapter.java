package nyc.c4q.googlefeed.ToDo;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import nyc.c4q.googlefeed.R;

/**
 * Created by yokilam on 12/24/17.
 */

class NoteAdapter extends RecyclerView.Adapter<NoteViewHolder> {
    private List<Note> noteList;

    public NoteAdapter(List <Note> noteList) {
        this.noteList = noteList;
    }

    @Override
    public NoteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View child = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_itemview, parent, false);
        return new NoteViewHolder(child);
    }

    @Override
    public void onBindViewHolder(NoteViewHolder holder, int position) {
        holder.bind(noteList.get(position));
    }

    @Override
    public int getItemCount() {
        if (noteList == null) {
            return 0;
        }
        return noteList.size();
    }

    public void swipeDelete(int adapterPosition) {
        noteList.remove(adapterPosition);
        this.notifyItemRemoved(adapterPosition);
    }

    public List <Note> getNoteList() {
        return noteList;
    }
}
