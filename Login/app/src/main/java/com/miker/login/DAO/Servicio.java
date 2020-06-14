package com.miker.login.DAO;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.core.util.Consumer;

import static android.content.Context.MODE_PRIVATE;

public class Servicio {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "SENSORES.db";

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static void connection(Context context, Consumer<SQLiteDatabase> function) {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = context.openOrCreateDatabase(DATABASE_NAME, MODE_PRIVATE, null);
        } catch (Exception ex) {
            throw new RuntimeException("No se logró abrir la conexión con la base de datos: " + ex.getMessage());
        }
        try {
            function.accept(db);
        } catch (Exception ex) {
            throw new RuntimeException("No se logró ejecutar el procedure en la base de datos: " + ex.getMessage());
        } finally {
            try {
                db.close();
            } catch (Exception ex) {
                throw new RuntimeException("No se logró cerrar la conexión con la base de datos: " + ex.getMessage());
            }
        }
    }
}
