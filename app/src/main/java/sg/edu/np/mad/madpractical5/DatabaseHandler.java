package sg.edu.np.mad.madpractical5;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Random;

public class DatabaseHandler extends SQLiteOpenHelper {

    private static final int DatabaseVersion = 1;

    //DatabaseName
    private static final String DatabaseName = "userDatabase";

    //UserTable
    private static final String UserTable = "Users";

    // UserColumn
    private static final String KeyID = "id";
    private static final String KeyName = "name";
    private static final String KeyDescription = "description";
    private static final String KeyFollowed = "followed";

    public DatabaseHandler(Context context) {
        super(context, DatabaseName, null, DatabaseVersion);
    }

    //table craetion
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_USERS_TABLE = "CREATE TABLE " + UserTable + "("
                + KeyID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KeyName + " TEXT,"
                + KeyDescription + " TEXT,"
                + KeyFollowed + " INTEGER" + ")";
        db.execSQL(CREATE_USERS_TABLE);
        insertInitialUsers(db);
    }

    //drop table and recreate
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + UserTable);

        // Create tables again
        onCreate(db);
    }

    // insert initial 20 user
    private void insertInitialUsers(SQLiteDatabase db) {
        Random random = new Random();
        for (int i = 1; i <= 20; i++) {
            ContentValues values = new ContentValues();
            values.put(KeyName, "User " + i);
            values.put(KeyDescription, "Description for User " + i);
            values.put(KeyFollowed, random.nextBoolean() ? 1 : 0);

            db.insert(UserTable, null, values);
        }
    }

    // Adding new user
    public void addUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KeyName, user.getName());
        values.put(KeyDescription, user.getDescription());
        values.put(KeyFollowed, user.getFollowed() ? 1 : 0);

        db.insert(UserTable, null, values);
        db.close();
    }

    // Get single user
    public User getUser(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(UserTable, new String[]{KeyID,
                        KeyName, KeyDescription, KeyFollowed}, KeyID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        assert cursor != null;
        User user = new User(cursor.getString(1), cursor.getString(2), cursor.getInt(0),
                cursor.getInt(3) > 0);
        cursor.close();
        return user;
    }


    // Get user
    public ArrayList<User> getUsers() {
        ArrayList<User> userList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + UserTable;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        //add to list
        if (cursor.moveToFirst()) {
            do {
                User user = new User(
                        cursor.getString(cursor.getColumnIndexOrThrow(KeyName)),
                        cursor.getString(cursor.getColumnIndexOrThrow(KeyDescription)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(KeyID)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(KeyFollowed)) == 1
                );
                userList.add(user);
            } while (cursor.moveToNext());
        }


        cursor.close();
        db.close();

        return userList;
    }

    // Update user
    public int updateUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KeyName, user.getName());
        values.put(KeyDescription, user.getDescription());
        values.put(KeyFollowed, user.getFollowed() ? 1 : 0);

        // Updating row
        return db.update(UserTable, values, KeyID + " = ?",
                new String[]{String.valueOf(user.getId())});
    }

}


