package com.hamlet.notes;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<String> notes;

    private DatabaseHelper databaseHelper;

    public NoteAdapter(Context context,ArrayList<String> list){
        this.mContext = context;
        this.notes = list;
        databaseHelper = new DatabaseHelper(mContext);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.note_example,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final String note = notes.get(position);
        int itemId = -1;

        Cursor data = databaseHelper.getItemId(note);
        while(data.moveToNext()){
            itemId = data.getInt(0);
        }

        holder.textView.setText(note);
        final int finalItemId = itemId;
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext,DetailActivity.class);
                intent.putExtra("NOTE",note);
                intent.putExtra("ID", finalItemId);
                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView textView;
        private CardView cardView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.noteExampleText);
            cardView = itemView.findViewById(R.id.noteExampleCardView);
        }
    }
}
