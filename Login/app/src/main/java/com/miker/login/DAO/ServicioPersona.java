package com.miker.login.DAO;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.provider.BaseColumns;

import androidx.annotation.RequiresApi;

import com.miker.login.Logic.Persona;
import com.miker.login.Logic.SEXO;

import java.util.ArrayList;
import java.util.List;

import static com.miker.login.Logic.Utils.personaToContentValues;
import static com.miker.login.Logic.Utils.tableExists;

public class ServicioPersona extends Servicio {
    public Context context;
    private static ServicioPersona servicio = new ServicioPersona();

    private ServicioPersona() {
        //Constructor privado
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static ServicioPersona getServicio(Context context) {
        servicio.context = context;
        servicio.createTable();
        return servicio;
    }

    public static abstract class usuarioEntry implements BaseColumns {
        public static final String TABLE_NAME = "PERSONA";
        public static final String NAME = "name";
        public static final String FIRST_NAME = "first_name";
        public static final String SECOND_NAME = "second_name";
        public static final String SEX = "sex";
        public static final String PHOTO = "photo";
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void createTable() {
        boolean[] exists = new boolean[1];
        try {
            connection(context, (SQLiteDatabase sqLiteDatabase) -> {
                exists[0] = tableExists(sqLiteDatabase, usuarioEntry.TABLE_NAME);
                sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS " + usuarioEntry.TABLE_NAME + " ("
                        + usuarioEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
                        + usuarioEntry.NAME + " TEXT NOT NULL,"
                        + usuarioEntry.FIRST_NAME + " TEXT NOT NULL,"
                        + usuarioEntry.SECOND_NAME + " TEXT NOT NULL,"
                        + usuarioEntry.SEX + " INTEGER NOT NULL,"
                        + usuarioEntry.PHOTO + " TEXT,"
                        + "UNIQUE (" + usuarioEntry._ID + "))");
            });
            // Insertar datos ficticios para prueba inicial
            if (!exists[0]) registroData();
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void registroData() {
        insert(new Persona(1,"José", "Beita", "Cascante",SEXO.MASCULINO, null));
        insert(new Persona(2,"Monserrath", "Molina", "Sanchez",SEXO.FEMENINO, null));
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public long insert(Persona persona) {
        long[] result = new long[1];
        connection(context, (SQLiteDatabase sqLiteDatabase) -> {
            result[0] = sqLiteDatabase.insert(
                    usuarioEntry.TABLE_NAME,
                    null,
                    personaToContentValues(persona)
            );
        });
        return result[0];
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public int update(Persona persona) {
        int[] result = new int[1];
        connection(context, (SQLiteDatabase sqLiteDatabase) -> {
            result[0] = sqLiteDatabase.update(
                    usuarioEntry.TABLE_NAME,
                    personaToContentValues(persona),
                    usuarioEntry._ID + " LIKE ?",
                    new String[]{String.valueOf(persona.getId())}
            );
        });
        return result[0];
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public int delete(Persona persona) {
        int[] result = new int[1];
        connection(context, (SQLiteDatabase sqLiteDatabase) -> {
            result[0] = sqLiteDatabase.delete(
                    usuarioEntry.TABLE_NAME,
                    usuarioEntry._ID + " LIKE ?",
                    new String[]{String.valueOf(persona.getId())}
            );
        });
        return result[0];
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public List<Persona> list() {
        List<Persona>[] result = new List[1];
        connection(context, (SQLiteDatabase sqLiteDatabase) -> {
            result[0] = list_to_cursor(
                    sqLiteDatabase.query(
                            usuarioEntry.TABLE_NAME, null, null, null, null, null, null
                    )
            );
        });
        return result[0];
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public Persona query(Persona persona) {
        Persona[] result = new Persona[1];
        connection(context, (SQLiteDatabase sqLiteDatabase) -> {
            result[0] = list_to_cursor(sqLiteDatabase.query(
                    usuarioEntry.TABLE_NAME,
                    null,
                    usuarioEntry._ID + " LIKE ?",
                    new String[]{String.valueOf(persona.getId())},
                    null,
                    null,
                    null)).get(0);
        });
        return result[0];
    }

    public List<Persona> list_to_cursor(Cursor cursor) {
        List<Persona> cursoList = new ArrayList<>();
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    Persona persona = new Persona();
                    persona.setId(cursor.getInt(cursor.getColumnIndex(usuarioEntry._ID)));
                    persona.setNombre(cursor.getString(cursor.getColumnIndex(usuarioEntry.NAME)));
                    persona.setApellido1(cursor.getString(cursor.getColumnIndex(usuarioEntry.FIRST_NAME)));
                    persona.setApellido2(cursor.getString(cursor.getColumnIndex(usuarioEntry.SECOND_NAME)));
                    persona.setSexo(SEXO.values()[cursor.getInt(cursor.getColumnIndex(usuarioEntry.SEX))]);
                    persona.setFoto(cursor.getString(cursor.getColumnIndex(usuarioEntry.PHOTO)));
                    //
                    cursoList.add(persona);
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        return cursoList;
    }

}
