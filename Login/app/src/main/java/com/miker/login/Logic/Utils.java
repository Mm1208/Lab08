package com.miker.login.Logic;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.ParcelFileDescriptor;

import androidx.annotation.AnyRes;
import androidx.annotation.NonNull;

import com.miker.login.DAO.ServicioPersona;
import com.miker.login.DAO.ServicioUsuario;
import com.miker.login.R;

import java.io.FileDescriptor;
import java.io.IOException;

public class Utils {

    public static ContentValues usuarioToContentValues(Usuario usuario) {
        ContentValues values = new ContentValues();
        values.put(ServicioUsuario.usuarioEntry.USER, usuario.getUser());
        values.put(ServicioUsuario.usuarioEntry.PASSWORD, usuario.getPassword());
        values.put(ServicioUsuario.usuarioEntry.PERSON, usuario.getPerson().getId());
        return values;
    }

    public static ContentValues personaToContentValues(Persona persona) {
        ContentValues values = new ContentValues();
        values.put(ServicioPersona.usuarioEntry.NAME, persona.getNombre());
        values.put(ServicioPersona.usuarioEntry.FIRST_NAME, persona.getApellido1());
        values.put(ServicioPersona.usuarioEntry.SECOND_NAME, persona.getApellido2());
        values.put(ServicioPersona.usuarioEntry.SEX, persona.getSexo().ordinal());
        values.put(ServicioPersona.usuarioEntry.PHOTO, persona.getFoto());
        return values;
    }

    public static boolean tableExists(SQLiteDatabase db, String tableName) {
        if (tableName == null || db == null || !db.isOpen()) {
            return false;
        }
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM sqlite_master WHERE type = ? AND name = ?", new String[]{"table", tableName});
        if (!cursor.moveToFirst()) {
            cursor.close();
            return false;
        }
        int count = cursor.getInt(0);
        cursor.close();
        return count > 0;
    }

    public static Uri getUrlImage(SEXO sexo, Context context){
        Uri result;
        switch (sexo){
            case MASCULINO: result = getUriToDrawable(context,R.drawable.hombre); break;
            case FEMENINO: result = getUriToDrawable(context,R.drawable.mujer); break;
            default: result = getUriToDrawable(context,R.drawable.usuario);
        }
        return result;
    }

    public static final Uri getUriToDrawable(@NonNull Context context,
                                             @AnyRes int drawableId) {
        Uri imageUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE +
                "://" + context.getResources().getResourcePackageName(drawableId)
                + '/' + context.getResources().getResourceTypeName(drawableId)
                + '/' + context.getResources().getResourceEntryName(drawableId) );
        return imageUri;
    }

    public static Bitmap getBitmapFromUri(Uri uri, Context context) throws IOException {
        ParcelFileDescriptor parcelFileDescriptor =
                context.getContentResolver().openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        return image;
    }
}
