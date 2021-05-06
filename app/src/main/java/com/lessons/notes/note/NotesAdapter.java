package com.lessons.notes.note;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lessons.notes.R;
import com.lessons.notes.note.domain.Note;

import java.util.ArrayList;
import java.util.List;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NotesViewHolder> {

    class NotesViewHolder extends RecyclerView.ViewHolder {
        TextView title;

        public NotesViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            title.setOnClickListener(v -> {
                if (getClickListener() != null) {
                    getClickListener().onNoteClicked(data.get(getAdapterPosition()));
                }
            });
            itemView.findViewById(R.id.edit_icon).setOnClickListener(v -> {
                if (getClickListener() != null) {
                    getClickListener().onEditClicked(data.get(getAdapterPosition()));
                }
            });
            ;

        }
    }

    private final ArrayList<Note> data = new ArrayList<>();

    private OnNoteClicked clickListener;

    public void addData(List<Note> toAdd) {
        data.clear();
        data.addAll(toAdd);
    }

    @NonNull
    @Override
    public NotesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_note, parent, false);
        return new NotesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotesViewHolder holder, int position) {
        Note note = data.get(position);
        holder.title.setText(note.getName());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public OnNoteClicked getClickListener() {
        return clickListener;
    }

    public void setClickListener(OnNoteClicked clickListener) {
        this.clickListener = clickListener;
    }


    interface OnNoteClicked {
        void onNoteClicked(Note note);

        void onEditClicked(Note note);
    }
}