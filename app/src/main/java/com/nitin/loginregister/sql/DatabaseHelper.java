package com.nitin.loginregister.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.nitin.loginregister.model.User;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "UserManager.db";

    // User table name
    private static final String TABLE_USER = "user";

    // User Table Columns names
    private static final String COLUMN_USER_ID = "user_id";
    private static final String COLUMN_USER_NAME = "user_name";
    private static final String COLUMN_USER_EMAIL = "user_email";
    private static final String COLUMN_USER_PASSWORD = "user_password";

    // create table sql query
    private String CREATE_USER_TABLE = "CREATE TABLE " + TABLE_USER + "("
            + COLUMN_USER_ID + " INTEGER," + COLUMN_USER_NAME + " TEXT,"
            + COLUMN_USER_EMAIL + " TEXT PRIMARY KEY," + COLUMN_USER_PASSWORD + " TEXT" + ")";

    private String CREATE_WORKSHOPS_TABLE="CREATE TABLE workshops(name TEXT PRIMARY KEY,description TEXT,date TEXT)";
    private String CREATE_APPLIED_TABLE="CREATE TABLE applied(workshops_name TEXT,user_email TEXT,FOREIGN KEY(workshops_name)REFERENCES workshops(name),FOREIGN KEY(user_email)REFERENCES user(user_email))";

    // drop table sql query
    private String DROP_USER_TABLE = "DROP TABLE IF EXISTS " + TABLE_USER;

    private String populate1="INSERT INTO workshops(name,description,date) VALUES('Android workshop','this is android','5-03-18')";
  //  private String populate2="INSERT INTO workshops VALUES('Cooking Workshop','This is cooking workshop','5-03-18')";
 //   private String populate3="INSERT INTO workshops VALUES('Robotics Workshop','This is robotics workshop','2-03-16')";
 //   private String populate4="INSERT INTO workshops VALUES('Machine Learning','This is machine learning workshop','22-03-16')";
    /**
     * Constructor
     * 
     * @param context
     */
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_USER_TABLE);
        db.execSQL(CREATE_WORKSHOPS_TABLE);
        db.execSQL(CREATE_APPLIED_TABLE);

        /*ContentValues values = new ContentValues();
        values.put("name", "Android workshop");
        values.put("description", "this is android");
        values.put("date", "12-03-18");

        // Inserting Row
        db.insert("workshops", null, values);*/
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        //Drop User Table if exist
        db.execSQL(DROP_USER_TABLE);
        db.execSQL("DROP TABLE IF EXISTS workshops");
        db.execSQL("DROP TABLE IF EXISTS applied");
        // Create tables again
        onCreate(db);

    }

    /**
     * This method is to create user record
     *
     * @param user
     */
    public void addUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_NAME, user.getName());
        values.put(COLUMN_USER_EMAIL, user.getEmail());
        values.put(COLUMN_USER_PASSWORD, user.getPassword());

        // Inserting Row
        db.insert(TABLE_USER, null, values);
        db.close();
    }
    public void addWorkshop() {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("name", "Android workshop");
        values.put("description", "this is android wrokshop");
        values.put("date", "12-03-18");
        ContentValues values2 = new ContentValues();
        values2.put("name", "Web workshop");
        values2.put("description", "This is web workshop");
        values2.put("date", "11-03-18");
        ContentValues values3 = new ContentValues();
        values3.put("name", "Machine Learning workshop");
        values3.put("description", "This is machine learning workshop");
        values3.put("date", "12-03-18");
        // Inserting Row
        db.insert("workshops", null, values);
        db.insert("workshops", null, values2);
        db.insert("workshops", null, values3);
        db.close();
    }
    /**
     * This method is to apply to the workshops
     *
     * @params workshop_name, user_email
     */
    public int apply(String workshop_name, String user_email){
        int i=0;
        SQLiteDatabase db=this.getWritableDatabase();
        db.execSQL("INSERT INTO applied VALUES('"+workshop_name+"','"+user_email+"')");
        return i;
    }

    /**
     * This method is to fetch all user and return the list of user records
     *
     * @return list
     */
    public List<String> getAllWorkshops(String user_email) {
        // array of columns to fetch
        String[] columns = {
                "name",
                "description",
                "date"
        };
        // sorting orders
        String sortOrder =
                "date" + " ASC";
        List<String> userList = new ArrayList<String>();

        SQLiteDatabase db = this.getReadableDatabase();

        if(user_email==null){
        Cursor cursor = db.query("workshops", //Table to query
                columns,    //columns to return
                null,        //columns for the WHERE clause
                null,        //The values for the WHERE clause
                null,       //group the rows
                null,       //filter by row groups
                sortOrder); //The sort order


            // Traversing through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                String[] work=new String[3];
                work[0]=cursor.getString(0);
                work[1]=cursor.getString(1);
                work[2]=cursor.getString(2);
                // Adding user record to list
                userList.add(work[0]);
            } while (cursor.moveToNext());
        }
        cursor.close();}
        else{
            String sql="SELECT * FROM workshops";
            Cursor cursor = db.rawQuery(sql,null);
            if (cursor.moveToFirst()) {
                do {
                    String[] work=new String[3];
                    work[0]=cursor.getString(0);
                    work[1]=cursor.getString(1);
                    work[2]=cursor.getString(2);
                    // Adding user record to list
                    userList.add(work[0]);
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        db.close();

        // return user list
        return userList;
    }

    public List<String> retrieveWorkshops(String user_email){
        List<String> userList = new ArrayList<String>();

        SQLiteDatabase db = this.getReadableDatabase();


        Cursor cursor = db.rawQuery("SELECT * FROM workshops, applied WHERE workshops.name=applied.workshops_name AND applied.user_email='"+user_email+"'", null);


        // Traversing through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                String[] work=new String[3];
                work[0]=cursor.getString(0);
                work[1]=cursor.getString(1);
                work[2]=cursor.getString(2);
                // Adding user record to list
                userList.add(work[0]);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        // return user list
        return userList;
    }
    /**
     * This method to update user record
     *
     * @param user
     */
    public void updateUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_NAME, user.getName());
        values.put(COLUMN_USER_EMAIL, user.getEmail());
        values.put(COLUMN_USER_PASSWORD, user.getPassword());

        // updating row
        db.update(TABLE_USER, values, COLUMN_USER_ID + " = ?",
                new String[]{String.valueOf(user.getId())});
        db.close();
    }
    public String[] getDesc(String name){
        SQLiteDatabase db=this.getReadableDatabase();
        String sql="SELECT * FROM workshops WHERE name='"+name+"'";
        Cursor cursor = db.rawQuery(sql,null);

        // Traversing through all rows and adding to list
        if (cursor.moveToFirst()) {
                String[] work=new String[3];
                work[0]=cursor.getString(cursor.getColumnIndex("workshops.name"));
                work[1]=cursor.getString(cursor.getColumnIndex("workshops.description"));
                work[2]=cursor.getString(cursor.getColumnIndex("workshops.date"));
                // Adding user record to list
                cursor.close();
                db.close();
                return work;
        }
        cursor.close();
        db.close();
        return null;
    }
    public Boolean getAppliedStatus(String name, String email){
        SQLiteDatabase db=this.getReadableDatabase();
        String sql="SELECT * FROM applied WHERE workshops_name='"+name+"' AND user_email='"+email+"'";
        Cursor cursor = db.rawQuery(sql,null);

        // Traversing through all rows and adding to list
        if (cursor.moveToFirst()) {
            cursor.close();
            db.close();
            return true;
        }
        cursor.close();
        db.close();
        return false;
    }
    /**
     * This method is to delete user record
     *
     * @param user
     */
    public void deleteUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        // delete user record by id
        db.delete(TABLE_USER, COLUMN_USER_ID + " = ?",
                new String[]{String.valueOf(user.getId())});
        db.close();
    }

    /**
     * This method to check user exist or not
     *
     * @param email
     * @return true/false
     */
    public boolean checkUser(String email) {

        // array of columns to fetch
        String[] columns = {
                COLUMN_USER_ID
        };
        SQLiteDatabase db = this.getReadableDatabase();

        // selection criteria
        String selection = COLUMN_USER_EMAIL + " = ?";

        // selection argument
        String[] selectionArgs = {email};

        // query user table with condition
        /**
         * Here query function is used to fetch records from user table this function works like we use sql query.
         * SQL query equivalent to this query function is
         * SELECT user_id FROM user WHERE user_email = 'jack@androidtutorialshub.com';
         */
        Cursor cursor = db.query(TABLE_USER, //Table to query
                columns,                    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,                       //group the rows
                null,                      //filter by row groups
                null);                      //The sort order
        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();

        if (cursorCount > 0) {
            return true;
        }

        return false;
    }

    /**
     * This method to check user exist or not
     *
     * @param email
     * @param password
     * @return true/false
     */
    public boolean checkUser(String email, String password) {

        // array of columns to fetch
        String[] columns = {
                COLUMN_USER_ID
        };
        SQLiteDatabase db = this.getReadableDatabase();
        // selection criteria
        String selection = COLUMN_USER_EMAIL + " = ?" + " AND " + COLUMN_USER_PASSWORD + " = ?";

        // selection arguments
        String[] selectionArgs = {email, password};

        // query user table with conditions
        /**
         * Here query function is used to fetch records from user table this function works like we use sql query.
         * SQL query equivalent to this query function is
         * SELECT user_id FROM user WHERE user_email = 'jack@androidtutorialshub.com' AND user_password = 'qwerty';
         */
        Cursor cursor = db.query(TABLE_USER, //Table to query
                columns,                    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,                       //group the rows
                null,                       //filter by row groups
                null);                      //The sort order

        int cursorCount = cursor.getCount();

        cursor.close();
        db.close();
        if (cursorCount > 0) {
            return true;
        }

        return false;
    }
}
