package com.example.minhaagenda;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.time.LocalDate;

public class Compromisso extends SQLiteOpenHelper {
    private static final String DATABASE = "AppAgenda";
    private static final int VERSAO = 1;

    public Compromisso(Context context) {
        super(context, DATABASE, null, VERSAO);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String ddl ="CREATE TABLE compromissos ("
                + "id INTEGER PRIMARY KEY,"
                + "data TEXT NOT NULL,"
                + "hora TEXT NOT NULL,"
                + "titulo TEXT NOT NULL,"
                + "descricao TEXT NOT NULL"
                + ");";
        db.execSQL(ddl);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String ddl ="DROP TABLE IF EXISTS compromissos";
        db.execSQL(ddl);
        onCreate(db);
    }

    public void salvar(String titulo, String descricao, String data, String hora) {
        ContentValues values = new ContentValues();
        values.put("titulo", titulo);
        values.put("descricao", descricao);
        values.put("data", data);
        values.put("hora", hora);
        getWritableDatabase().insert("compromissos", null, values);
    }

    public int getDataNumeroCompromissos(LocalDate data){
        String query = "SELECT count(*) as num FROM compromissos WHERE data = '"+ data +"' GROUP BY data";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            return cursor.getInt(cursor.getColumnIndex("num"));
        }

        return 0;
    }
}
