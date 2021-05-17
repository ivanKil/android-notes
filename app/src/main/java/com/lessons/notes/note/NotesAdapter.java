package com.lessons.notes.note;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.lessons.notes.R;
import com.lessons.notes.note.domain.Note;

import java.util.ArrayList;
import java.util.List;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NotesViewHolder> {
    private final Fragment fragment;
    private int menuPosition;
    private final ArrayList<Note> data = new ArrayList<>();
    private OnNoteClicked clickListener;

    public NotesAdapter(Fragment fragment) {
        this.fragment = fragment;
    }

    public Note getNoteMenuPosition() {
        return data.get(menuPosition);
    }

    class NotesViewHolder extends RecyclerView.ViewHolder {
        private final TextView title;

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
            registerContextMenu(title);
            title.setOnLongClickListener(v -> {
                menuPosition = getLayoutPosition();
                title.showContextMenu();
                return true;
            });

        }

        private void registerContextMenu(@NonNull View itemView) {
            if (fragment != null) {
                itemView.setOnLongClickListener(v -> {
                    menuPosition = getLayoutPosition();
                    return false;
                });
                fragment.registerForContextMenu(itemView);
            }
        }


        public void bind(Note note) {
            title.setText(note.getName());
        }
    }

    public void setData(List<Note> toAdd) {
        NotesDiffUtilCallback callback = new NotesDiffUtilCallback(data, toAdd);
        DiffUtil.DiffResult result = DiffUtil.calculateDiff(callback);
        data.clear();
        data.addAll(toAdd);
        result.dispatchUpdatesTo(this);
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
        holder.bind(note);
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

    public static class NotesDiffUtilCallback extends DiffUtil.Callback {

        private final List<Note> oldList;
        private final List<Note> newList;

        public NotesDiffUtilCallback(List<Note> oldList, List<Note> newList) {
            this.oldList = oldList;
            this.newList = newList;
        }

        @Override
        public int getOldListSize() {
            return oldList.size();
        }

        @Override
        public int getNewListSize() {
            return newList.size();
        }

        @Override
        public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
            return oldList.get(oldItemPosition).getId() == newList.get(newItemPosition).getId();
        }

        @Override
        public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
            return oldList.get(oldItemPosition).equals(newList.get(newItemPosition));
        }
    }
}