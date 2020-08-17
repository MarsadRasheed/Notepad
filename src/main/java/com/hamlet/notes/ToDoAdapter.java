package com.hamlet.notes;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class ToDoAdapter extends RecyclerView.Adapter<ToDoAdapter.ViewHolder>{

    private Context mContext;
    private ArrayList<TODO> toDos;
    private DatabaseHelper databaseHelper;

    public ToDoAdapter(Context context,ArrayList<TODO> toDos){
        this.mContext = context;
        this.toDos = toDos;
        databaseHelper = new DatabaseHelper(mContext);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.todo_example,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final TODO toDo = toDos.get(position);
        holder.textView.setText(toDo.getToDo());

        if(toDo.getPriority() == 1){
            holder.textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_priority_high,0,0,0);
        }

        int itemId = -1;
        Cursor cursor = databaseHelper.getToDoId(toDo.getToDo());
        while (cursor.moveToNext()){
            itemId = cursor.getInt(0);
        }

        final int finalItemId = itemId;
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext,ToDoDetailActivity.class);
                intent.putExtra("ItemId", finalItemId);
                intent.putExtra("ToDo",toDo.getToDo());
                intent.putExtra("DESCRIPTION",toDo.getDescription());
                intent.putExtra("priority",toDo.getPriority());
                intent.putExtra("DATENTIME",toDo.getDateTime());
                Toast.makeText(mContext, toDo.getDateTime(), Toast.LENGTH_SHORT).show();
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return toDos.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private CardView cardView;
        private RadioButton radioButton;
        private TextView textView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.toDoCard);
            radioButton = itemView.findViewById(R.id.radioButton);
            textView = itemView.findViewById(R.id.toDoTextView);
        }
    }
}
