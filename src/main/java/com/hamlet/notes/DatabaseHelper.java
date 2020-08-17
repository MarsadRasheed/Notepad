package com.hamlet.notes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String name = "notes_table";
    public static final String COL_TWO = "data";
    public static final String toDo = "toDo";
    public static final String toDoData = "toDoData";
    public static final String descriptionToDo = "description";
    public static final String priority = "priority";
    public static final String dateTime = "dateTime";

    public DatabaseHelper(@Nullable Context context) {
        super(context, name, null, 5);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "Create table " + name + " ( ID INTEGER PRIMARY KEY AUTOINCREMENT, " + COL_TWO + " TEXT )";
        String createToDos = "Create table "+ toDo + " ( ID INTEGER PRIMARY KEY AUTOINCREMENT, " + toDoData + " TEXT , "
                + descriptionToDo + " TEXT , "
                + priority + " INTEGER , "
                + dateTime + " DATETIME "
                + " )";
        db.execSQL(createTable);
        db.execSQL(createToDos);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + name);
        db.execSQL("DROP TABLE IF EXISTS " + toDo);
        onCreate(db);
    }

    public boolean addNote(String data){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_TWO,data);

        long result = db.insert(name, null, contentValues);
        if(result == -1){
            return false;
        } else {
            return true;
        }
    }

    public boolean addToDo(String data,int pri,String dateNTime){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(toDoData,data);
        contentValues.put(descriptionToDo,"");
        contentValues.put(priority,pri);
        contentValues.put(dateTime,dateNTime);

        long result = db.insert(toDo,  null, contentValues);
        if(result == -1){
            return false;
        } else {
            return true;
        }
    }

    public Cursor getData(){
        SQLiteDatabase database = this.getWritableDatabase();
        String query = "Select * from " + name;
        Cursor data = database.rawQuery(query,null);
        return data;
    }

    public Cursor getToDo(){
        SQLiteDatabase database = this.getWritableDatabase();
        String query = "Select * from " + toDo + " order by " + priority + " desc , " + dateTime + " desc";
        Cursor data = database.rawQuery(query,null);
        return data;
    }

    public Cursor getItemId(String note){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "Select ID From " + name + " where " + COL_TWO + " = " + "'" + note + "'" ;
        Cursor cursor = db.rawQuery(query,null);
        return  cursor;
    }

    public Cursor getToDoId(String todo){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "Select ID From " + toDo + " where " + toDoData + " = " + "'" + todo + "'" ;
        Cursor cursor = db.rawQuery(query,null);
        return  cursor;
    }

    public void deleteData(String note,int id){
        SQLiteDatabase database = this.getWritableDatabase();
        String query = "Delete from " + name + " where " + COL_TWO + " = " + "'" + note + "'" + " AND " + "ID = " + " ' " + id + " ' ";
        database.execSQL(query);
    }

    public void deleteToDo(String todo,int id){
        SQLiteDatabase database = this.getWritableDatabase();
        String query = "Delete from " + toDo + " where " + toDoData + " = " + "'" + todo + "'" + " AND " + "ID = " + " ' " + id + " ' ";
        database.execSQL(query);
    }

    public void updateData(String newNote,int id,String oldNote){
        SQLiteDatabase database = this.getWritableDatabase();
        String query = "Update " + name + " Set " + COL_TWO + " = " + " '" +  newNote  + "' " + " where " + " ID = " + " ' " +  id + " ' " ;
        database.execSQL(query);
    }

    public void updateToDo(String newToDo,int id,String oldToDo,String description,int pri,String dateNTime){
        SQLiteDatabase database = this.getWritableDatabase();
        String query = "Update " + toDo + " Set " + toDoData + " = " + " '" +  newToDo  + "' , " + descriptionToDo + " = " + " '" +  description  + "' , " + priority + " = " + pri + " , " + dateTime + " = " + " ' " +  dateNTime + " ' " + " where " + " ID = " + " ' " +  id + " ' " ;
        database.execSQL(query);
    }

}
