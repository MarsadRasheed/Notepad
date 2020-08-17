package com.hamlet.notes.ui.dashboard;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.hamlet.notes.DatabaseHelper;
import com.hamlet.notes.NoteAdapter;
import com.hamlet.notes.R;
import com.hamlet.notes.TODO;
import com.hamlet.notes.ToDoAdapter;

import java.util.ArrayList;

public class DashboardFragment extends Fragment {

    private DashboardViewModel dashboardViewModel;
    private RecyclerView recyclerView;
    private ToDoAdapter adapter;
    private ArrayList<TODO> arrayList;

    DatabaseHelper databaseHelper;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                ViewModelProviders.of(this).get(DashboardViewModel.class);
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        recyclerView = root.findViewById(R.id.toDoRecyclerView);
        databaseHelper = new DatabaseHelper(getContext());
        recyclerView.setHasFixedSize(true);
//        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

//        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        arrayList = new ArrayList<>();

        populateListView();

        return root;
    }

    private void populateListView() {
        Cursor cursor = databaseHelper.getToDo();
        while(cursor.moveToNext()){
            int id = cursor.getInt(0);
            String todo = cursor.getString(1);
            String description = cursor.getString(2);
            int priority = cursor.getInt(3);
            String dateTime = cursor.getString(4);
            TODO Todo = new TODO(id,todo,description,priority,dateTime);
            arrayList.add(Todo);
        }
        adapter = new ToDoAdapter(getContext(),arrayList);
        recyclerView.setAdapter(adapter);
    }

}
