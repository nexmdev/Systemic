package com.nexm.iupacnomenclatureclassxii.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ExtDbHelperPractice extends SQLiteOpenHelper {
    private static String DB_PATH;
    private static String DB_NAME;
    private static int VERSION;
    private SQLiteDatabase database;
    private final Context context;
    private  static ExtDbHelperPractice sInstance;
    private final SharedPreferences sharedPreferences;
    public   static synchronized ExtDbHelperPractice getInstance(Context context , String databaseName, int version){

        if(sInstance == null){

            sInstance = new ExtDbHelperPractice(context.getApplicationContext(),databaseName,version);
        }

        return sInstance;
    }
    private ExtDbHelperPractice(Context context, String databaseName, int version) {
        super(context, databaseName, null, version);
        this.context = context;
        sharedPreferences = context.getSharedPreferences("PracticeVersion",Context.MODE_PRIVATE);
        String packageName = context.getPackageName();
       // DB_PATH = String.format("//data//data//%s//databases//", packageName);
        DB_NAME = databaseName;
        DB_PATH = context.getDatabasePath(DB_NAME).getAbsolutePath();
        VERSION = version;
        openDataBase();
    }
    // ,
    private void createDataBase() {
        boolean dbExist = checkDataBase();
        if (!dbExist) {
            this.getReadableDatabase();
            this.close();
            try {
                copyDataBase();
            } catch (IOException e) {

                throw new Error("Error copying database!");
            }
        }
    }
    //
    private boolean checkDataBase() {
        SQLiteDatabase checkDb = null;
        String path = context.getDatabasePath(DB_NAME).getAbsolutePath();
        try {
            //String path = DB_PATH + DB_NAME;
            checkDb = SQLiteDatabase.openDatabase(path, null,
                    SQLiteDatabase.OPEN_READONLY);
        } catch (SQLException e) {

        }
        //    ,
        if (checkDb != null) {
            checkDb.close();
        }
        return checkDb != null;
    }
    //
    private void copyDataBase() throws IOException {
        //
        //  assets
        InputStream externalDbStream = context.getAssets().open(DB_NAME);
        //
        //String outFileName = DB_PATH + DB_NAME;
        String outFileName = DB_PATH;
        //
        OutputStream localDbStream = new FileOutputStream(outFileName);
        //
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = externalDbStream.read(buffer)) > 0) {
            localDbStream.write(buffer, 0, bytesRead);
        }
        //    ()
        localDbStream.close();
        externalDbStream.close();
        sharedPreferences.edit().putInt("VersionNumber",VERSION).apply();
    }
    public SQLiteDatabase openDataBase() throws SQLException {
        String path = context.getDatabasePath(DB_NAME).getAbsolutePath();
        if (database == null) {
            if(sharedPreferences.getInt("VersionNumber",1)<VERSION){
               context.deleteDatabase(DB_NAME);
            }
            createDataBase();
            database = SQLiteDatabase.openDatabase(path, null,
                    SQLiteDatabase.OPEN_READWRITE);
        }
        return database;
    }
    @Override
    public synchronized void close() {
        if (database != null) {
            database.close();

        }
        super.close();
    }
    @Override
    public void onCreate(SQLiteDatabase db) {}
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}
}
