package com.miker.login.Logic;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.widget.ImageView;

import androidx.annotation.AnyRes;
import androidx.annotation.NonNull;

import com.koushikdutta.ion.Ion;
import com.miker.login.DAO.ServicioPersona;
import com.miker.login.DAO.ServicioUsuario;
import com.miker.login.R;

import java.io.FileDescriptor;
import java.io.IOException;
import java.util.NoSuchElementException;

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

    public static void scaleImage(ImageView view, Context context) throws NoSuchElementException {
        // Get bitmap from the the ImageView.
        Bitmap bitmap = null;

        try {
            Drawable drawing = view.getDrawable();
            bitmap = ((BitmapDrawable) drawing).getBitmap();
        } catch (NullPointerException e) {
            throw new NoSuchElementException("No drawable on given view");
        } catch (ClassCastException e) {
            // Check bitmap is Ion drawable
            bitmap = Ion.with(view).getBitmap();
        }

        // Get current dimensions AND the desired bounding box
        int width = 0;

        try {
            width = bitmap.getWidth();
        } catch (NullPointerException e) {
            throw new NoSuchElementException("Can't find bitmap on given view/drawable");
        }

        int height = bitmap.getHeight();
        int bounding = dpToPx(2000, context);

        float xScale = ((float) bounding) / width;
        float yScale = ((float) bounding) / height;
        float scale = (xScale <= yScale) ? xScale : yScale;

        // Create a matrix for the scaling and add the scaling data
        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);

        // Create a new bitmap and convert it to a format understood by the ImageView
        Bitmap scaledBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
        width = scaledBitmap.getWidth(); // re-use
        height = scaledBitmap.getHeight(); // re-use
        BitmapDrawable result = new BitmapDrawable(scaledBitmap);

        // Apply the scaled bitmap
        view.setImageDrawable(result);
    }

    public static int dpToPx(int dp, Context context) {
        float density = context.getResources().getDisplayMetrics().density;
        return Math.round((float)dp * density);
    }
}
