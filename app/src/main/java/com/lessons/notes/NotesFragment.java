package com.lessons.notes;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.lessons.notes.domain.Note;
import com.lessons.notes.domain.NotesRepository;

import java.util.List;

public class NotesFragment extends Fragment implements Observer {

    public interface OnNoteClicked {
        void onNoteClicked(Note note);
    }

    private OnNoteClicked onCityClicked;
    private static final String CURRRENT_NOTE = "CURRRENT_NOTE";
    private Note currNote;
    private NotesRepository notesRepository;
    private Publisher publisher;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_notes, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initList(view, (notesRepository = new NotesRepository()).getNotes());
        initTopMenu(view);
    }

    private void initTopMenu(View view) {
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        Menu menu = toolbar.getMenu();
        final SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                ((LinearLayout) view.findViewById(R.id.list_layot)).removeAllViews();
                initList(view, notesRepository.filterByName(s));
                return true;
            }
        });

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.action_sort_by_name) {
                    ((LinearLayout) view.findViewById(R.id.list_layot)).removeAllViews();
                    initList(view, notesRepository.sortByName());
                    return true;
                }
                if (item.getItemId() == R.id.action_sort_by_date) {
                    ((LinearLayout) view.findViewById(R.id.list_layot)).removeAllViews();
                    initList(view, notesRepository.sortByDate());
                    return true;
                }

                return false;
            }
        });
    }

    private void openNoteDetail(Note note) {
        if (onCityClicked != null) {
            onCityClicked.onNoteClicked(note);
        }
    }

    private void initList(View view, List<Note> notex) {
        LinearLayout layoutView = view.findViewById(R.id.list_layot);
        for (Note note : notex) {
            TextView tv = new TextView(getContext());
            tv.setText(note.getName());
            tv.setTextSize(30);
            layoutView.addView(tv);
            tv.setOnClickListener(v -> {
                Note n = notesRepository.getNoteById(note.getId());
                currNote = n;
                openNoteDetail(n);
            });
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putParcelable(CURRRENT_NOTE, currNote);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void updateNote(Note note) {
        notesRepository.updateNote(note);
        currNote = note;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof PublisherHolder) {
            publisher = ((PublisherHolder) context).getPublisher();
            publisher.addObserver(this);
        }
        if (context instanceof OnNoteClicked) {
            onCityClicked = (OnNoteClicked) context;
        }
    }

    @Override
    public void onDetach() {
        onCityClicked = null;
        if (publisher != null) {
            publisher.removeObserver(this);
        }
        super.onDetach();
    }
}