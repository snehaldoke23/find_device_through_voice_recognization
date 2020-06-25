package speechrecognition.com.mm.speechrecognition.activities;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by unity2 on 12/19/2017.
 */

public class DBhelper extends SQLiteOpenHelper {
    private static final String LOGCAT = null;

    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";
    private static final String EMAIL = "email";
    private static final String MOBNO = "mobno";
    private static final String LOCATION = "location";
    private static final String STATE = "state";
    private static final String JUST_SEARCH = "justsearch";



    public DBhelper(Context applicationcontext) {
        super(applicationcontext, "student.db", null, 1);
        Log.d(LOGCAT,"Created");
    }



    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + JUST_SEARCH + "("
                + USERNAME + " TEXT," + PASSWORD + " TEXT,"+ EMAIL + " TEXT,"+ MOBNO + " TEXT,"+ LOCATION + " TEXT,"
                + STATE + " TEXT" + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public boolean insertdetails(String username, String passowrd, String email,String mobno,String locaton,String state) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(USERNAME, username);
        contentValues.put(PASSWORD, passowrd);
        contentValues.put(EMAIL, email);
        contentValues.put(MOBNO, mobno);
        contentValues.put(LOCATION, locaton);
        contentValues.put(STATE, state);

        try {
            long i = db.insert(JUST_SEARCH, null, contentValues);
            Log.e("question data added=", "" + i);
            db.close();
        } catch (Exception e) {
            db.close();
        }
        return true;
    }

    public int Login(String username,String password)

    {
        SQLiteDatabase db = this.getWritableDatabase();
        String[] selectionArgs = new String[]{username, password};
        try
        {
            int i = 0;
            Cursor c = null;
            c = db.rawQuery("select * from justsearch where username=? and password=?", selectionArgs);
            c.moveToFirst();
            i = c.getCount();
            c.close();
            return i;
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return 0;
    }
}
