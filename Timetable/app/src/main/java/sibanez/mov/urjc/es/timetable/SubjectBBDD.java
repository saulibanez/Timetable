package sibanez.mov.urjc.es.timetable;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

// http://elbauldeandroid.blogspot.com.es/2013/02/base-de-datos-sqlite.html

public class SubjectBBDD extends SQLiteOpenHelper {

    private static final String NAME_BBDD = "timetable.db";
    private static final int VERSION_BASEDATOS = 1;

    // Sentencia SQL para la creación de una tabla
    private static final String TABLE_SUBJECTS = "CREATE TABLE subjects" +
            "(_id INT PRIMARY KEY, alumn_name TEXT, alumn_surname TEXT, subject_name TEXT, email TEXT, days TEXT, time TEXT)";

    public SubjectBBDD(Context context) {
        super(context, NAME_BBDD, null, VERSION_BASEDATOS);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_SUBJECTS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SUBJECTS);
        onCreate(db);
    }

    public void insertSubject(int id, String name, String surname, String subject, String email, String days, String time) {
        SQLiteDatabase db = getWritableDatabase();
        if (db != null) {
            ContentValues valores = new ContentValues();
            valores.put("_id", id);
            valores.put("alumn_name", name);
            valores.put("alumn_surname", surname);
            valores.put("subject_name", subject);
            valores.put("email", email);
            valores.put("days", days);
            valores.put("time", time);
            if (db.insert("subjects", null, valores) == -1) {
                db.close();
                throw new RuntimeException("Can't insert subjects in database");
            }
            db.close();
        }
    }

    public void deleteSubject(int id) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete("subjects", "_id=" + id, null);
        db.close();
    }

    public Subjects getSubject(int id) {
        SQLiteDatabase db = getReadableDatabase();
        String[] recover_value = {"_id", "alumn_name", "alumn_surname", "subject_name", "email", "days", "time"};

        // Cursor devuelve el resultado de un registro de la tabla y lo almacena en la memoria, se aplica
        // el método: query(table, columns, selection, selectionArgs, groupBy, having, orderBy, limit)
        Cursor c = db.query("subjects", recover_value, "_id=" + id,
                null, null, null, null, null);
        if (c != null) {
            c.moveToFirst();
        }
        Subjects subj = new Subjects(c.getInt(0), c.getString(1),
                c.getString(2), c.getString(3), c.getString(4), c.getString(5), c.getString(6));
        db.close();
        c.close();
        return subj;
    }


    public List<Subjects> getSubjects() {
        SQLiteDatabase db = getReadableDatabase();
        List<Subjects> list_subjects = new ArrayList<Subjects>();
        String[] recover_value = {"_id", "alumn_name", "alumn_surname", "subject_name", "email", "days", "time"};
        Cursor c = db.query("subjects", recover_value,
                null, null, null, null, null, null);
        c.moveToFirst();
        do {
            Subjects subj = new Subjects(c.getInt(0), c.getString(1),
                    c.getString(2), c.getString(3), c.getString(4), c.getString(5), c.getString(6));
            list_subjects.add(subj);
        } while (c.moveToNext());
        db.close();
        c.close();
        return list_subjects;
    }
}
