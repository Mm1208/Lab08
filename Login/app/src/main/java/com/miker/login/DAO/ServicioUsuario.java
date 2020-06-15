package com.miker.login.DAO;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.provider.BaseColumns;

import androidx.annotation.RequiresApi;

import com.miker.login.Logic.Persona;
import com.miker.login.Logic.Usuario;

import java.util.ArrayList;
import java.util.List;

import static com.miker.login.Logic.Utils.usuarioToContentValues;
import static com.miker.login.Logic.Utils.tableExists;

public class ServicioUsuario extends Servicio {
    public Context context;
    private static ServicioUsuario servicio = new ServicioUsuario();

    private ServicioUsuario() {
        //Constructor privado
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static ServicioUsuario getServicio(Context context) {
        servicio.context = context;
        servicio.createTable();
        return servicio;
    }

    public static abstract class usuarioEntry implements BaseColumns {
        public static final String TABLE_NAME = "USUARIO";
        public static final String USER = "user";
        public static final String PASSWORD = "password";
        public static final String PERSON = "person";
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void createTable() {
        boolean[] exists = new boolean[1];
        try {
            connection(context, (SQLiteDatabase sqLiteDatabase) -> {
                exists[0] = tableExists(sqLiteDatabase, usuarioEntry.TABLE_NAME);
                sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS " + usuarioEntry.TABLE_NAME + " ("
                        + usuarioEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
                        + usuarioEntry.USER + " TEXT NOT NULL,"
                        + usuarioEntry.PASSWORD + " TEXT NOT NULL,"
                        + usuarioEntry.PERSON + " INTEGER NOT NULL,"
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
        insert(new Usuario(1, "jose", "jose", new Persona(1)));
        insert(new Usuario(2, "monse", "monse", new Persona(2)));
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public long insert(Usuario usuario) {
        long[] result = new long[1];
        connection(context, (SQLiteDatabase sqLiteDatabase) -> {
            result[0] = sqLiteDatabase.insert(
                    usuarioEntry.TABLE_NAME,
                    null,
                    usuarioToContentValues(usuario)
            );
        });
        return result[0];
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public int update(Usuario usuario) {
        int[] result = new int[1];
        connection(context, (SQLiteDatabase sqLiteDatabase) -> {
            result[0] = sqLiteDatabase.update(
                    usuarioEntry.TABLE_NAME,
                    usuarioToContentValues(usuario),
                    usuarioEntry._ID + " LIKE ?",
                    new String[]{String.valueOf(usuario.getId())}
            );
        });
        return result[0];
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public int delete(Usuario usuario) {
        int[] result = new int[1];
        connection(context, (SQLiteDatabase sqLiteDatabase) -> {
            result[0] = sqLiteDatabase.delete(
                    usuarioEntry.TABLE_NAME,
                    usuarioEntry._ID + " LIKE ?",
                    new String[]{String.valueOf(usuario.getId())}
            );
        });
        return result[0];
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public List<Usuario> list() {
        List<Usuario>[] result = new List[1];
        connection(context, (SQLiteDatabase sqLiteDatabase) -> {
            result[0] = list_to_cursor(
                    sqLiteDatabase.query(
                            usuarioEntry.TABLE_NAME, null, null, null, null, null, null
                    )
            );
        });
        for (Usuario u : result[0]) {
            u.setPerson(ServicioPersona.getServicio(context).query(u.getPerson()));
        }
        return result[0];
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public Usuario query(Usuario usuario) {
        Usuario[] result = new Usuario[1];
        connection(context, (SQLiteDatabase sqLiteDatabase) -> {
            result[0] = list_to_cursor(sqLiteDatabase.query(
                    usuarioEntry.TABLE_NAME,
                    null,
                    usuarioEntry._ID + " LIKE ?",
                    new String[]{String.valueOf(usuario.getId())},
                    null,
                    null,
                    null)).get(0);
        });
        result[0].setPerson(ServicioPersona.getServicio(context).query(result[0].getPerson()));
        return result[0];
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public Usuario login(Usuario usuario) {
        Usuario[] result = new Usuario[1];
        connection(context, (SQLiteDatabase sqLiteDatabase) -> {
            List<Usuario> list = list_to_cursor(sqLiteDatabase.query(
                    usuarioEntry.TABLE_NAME,
                    null,
                    usuarioEntry.USER + " LIKE ? AND " + usuarioEntry.PASSWORD + " LIKE ?",
                    new String[]{usuario.getUser(), usuario.getPassword()},
                    null,
                    null,
                    null));
            result[0] = (list.isEmpty()) ? null : list.get(0);
        });
        if(result[0] != null) result[0].setPerson(ServicioPersona.getServicio(context).query(result[0].getPerson()));
        return result[0];
    }

    public List<Usuario> list_to_cursor(Cursor cursor) {
        List<Usuario> cursoList = new ArrayList<>();
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    Usuario usuario = new Usuario();
                    usuario.setId(cursor.getInt(cursor.getColumnIndex(usuarioEntry._ID)));
                    usuario.setUser(cursor.getString(cursor.getColumnIndex(usuarioEntry.USER)));
                    usuario.setPassword(cursor.getString(cursor.getColumnIndex(usuarioEntry.PASSWORD)));
                    usuario.setPerson(
                            new Persona(cursor.getInt(cursor.getColumnIndex(usuarioEntry.PERSON)))
                    );
                    //
                    cursoList.add(usuario);
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        return cursoList;
    }

}
