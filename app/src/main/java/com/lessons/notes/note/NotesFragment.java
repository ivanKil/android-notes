package com.lessons.notes.note;

import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lessons.notes.R;
import com.lessons.notes.note.domain.Note;

public class NotesFragment extends Fragment {
    private NotesAdapter adapter;
    private NoteViewModel viewModel;
    private static final int MY_DEFAULT_DURATION = 1000;

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
        viewModel = new ViewModelProvider(requireActivity()).get(NoteViewModel.class);
        viewModel.getNotesLiveData().observe(getViewLifecycleOwner(), notes -> {
            adapter.setData(notes);
        });
        viewModel.getSavedNote().observe(getViewLifecycleOwner(), note -> {
            if (note != null) {
                viewModel.updateNote(note);
            }
        });
        if (savedInstanceState == null) {
            viewModel.requestNotes();
        }
        initRecyclerView(view);
        initTopMenu(view);
    }

    private void initRecyclerView(View view) {
        adapter = new NotesAdapter(this);
        adapter.setClickListener(new NotesAdapter.OnNoteClicked() {
            @Override
            public void onNoteClicked(Note note) {
                viewModel.select(note);
            }

            @Override
            public void onEditClicked(Note note) {
                viewModel.select(note.setForEdit(true));
            }
        });

        RecyclerView notesList = view.findViewById(R.id.list_layot);
        RecyclerView.LayoutManager lm = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        notesList.setLayoutManager(lm);
        notesList.setAdapter(adapter);

        DefaultItemAnimator animator = new DefaultItemAnimator();
        animator.setAddDuration(MY_DEFAULT_DURATION);
        animator.setRemoveDuration(MY_DEFAULT_DURATION);
        animator.setChangeDuration(MY_DEFAULT_DURATION);
        notesList.setItemAnimator(animator);

    }

    private void initTopMenu(View view) {
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        Menu menu = toolbar.getMenu();
        final SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                viewModel.filterByName(s);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                viewModel.filterByName(s);
                return true;
            }
        });

        toolbar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.action_sort_by_name) {
                viewModel.sortByName();
                return true;
            }
            if (item.getItemId() == R.id.action_sort_by_date) {
                viewModel.sortByDate();
                return true;
            }
            if (item.getItemId() == R.id.action_add) {
                viewModel.select(new Note().setForEdit(true));
                return true;
            }

            return false;
        });
    }

    @Override
    public void onCreateContextMenu(@NonNull ContextMenu menu, @NonNull View v, @Nullable ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = requireActivity().getMenuInflater();
        inflater.inflate(R.menu.note_context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_update:
                viewModel.select(adapter.getNoteMenuPosition().setForEdit(true));
                return true;
            case R.id.action_delete:
                viewModel.delete(adapter.getNoteMenuPosition().setForEdit(true));
                return true;
        }
        return super.onContextItemSelected(item);
    }

}