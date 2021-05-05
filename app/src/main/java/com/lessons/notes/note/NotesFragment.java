package com.lessons.notes.note;

import android.content.Context;
import android.os.Bundle;
import android.util.TypedValue;
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

import com.lessons.notes.Observer;
import com.lessons.notes.Publisher;
import com.lessons.notes.PublisherHolder;
import com.lessons.notes.R;
import com.lessons.notes.note.domain.Note;
import com.lessons.notes.note.domain.NotesRepository;

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
    private LinearLayout listLayout;

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
        listLayout = ((LinearLayout) view.findViewById(R.id.list_layot));
        initList((notesRepository = new NotesRepository()).getNotes());
        initTopMenu(view);
    }

    private void initTopMenu(View view) {
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        Menu menu = toolbar.getMenu();
        final SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                filterNotes(s);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                filterNotes(s);
                return true;
            }
        });

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.action_sort_by_name) {
                    listLayout.removeAllViews();
                    initList(notesRepository.sortByName());
                    return true;
                }
                if (item.getItemId() == R.id.action_sort_by_date) {
                    listLayout.removeAllViews();
                    initList(notesRepository.sortByDate());
                    return true;
                }

                return false;
            }
        });
    }

    private void filterNotes(String text) {
        listLayout.removeAllViews();
        initList(notesRepository.filterByName(text));
    }

    private void openNoteDetail(Note note) {
        if (onCityClicked != null) {
            onCityClicked.onNoteClicked(note);
        }
    }

    private void initList(List<Note> notex) {
        for (Note note : notex) {
            TextView tv = new TextView(getContext());
            tv.setText(note.getName());
            tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, getResources().getDimensionPixelSize(R.dimen.tv_text_list));
            listLayout.addView(tv);
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
        if (getParentFragment() instanceof PublisherHolder) {
            publisher = ((PublisherHolder) getParentFragment()).getPublisher();
            publisher.addObserver(this);
        }
        if (getParentFragment() instanceof OnNoteClicked) {
            onCityClicked = (OnNoteClicked) getParentFragment();
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