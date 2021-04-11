package com.nexm.iupacnomenclatureclassxii.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.nexm.iupacnomenclatureclassxii.IUPAC_APPLICATION;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ExtDbHelper extends SQLiteOpenHelper {
    private static String DB_PATH;
    private static String DB_NAME;
    private static int VERSION;
    private SQLiteDatabase database;
    private final Context context;
    private  static ExtDbHelper sInstance;
    private SharedPreferences sharedPreferences;
    public   static synchronized ExtDbHelper getInstance(Context context , String databaseName,int version){

        if(sInstance == null){

            sInstance = new ExtDbHelper(context.getApplicationContext(),databaseName,version);
        }

        return sInstance;
    }
    private ExtDbHelper(Context context, String databaseName,int version) {
        super(context, databaseName, null, version);
        this.context = context;
        sharedPreferences = context.getSharedPreferences("IUPACVersion",Context.MODE_PRIVATE);
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

    private void copyDataBase() throws IOException {

        //  assets
        InputStream externalDbStream = context.getAssets().open(DB_NAME);

        //String outFileName = DB_PATH + DB_NAME;
        String outFileName = DB_PATH;

        OutputStream localDbStream = new FileOutputStream(outFileName);

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
            if(sharedPreferences.getInt("VersionNumber",8)<VERSION){
                copyStatus();
               context.deleteDatabase(DB_NAME);
            }
            createDataBase();
            database = SQLiteDatabase.openDatabase(path, null,
                    SQLiteDatabase.OPEN_READWRITE);
        }
        return database;
    }

    private void copyStatus() {
        Cursor cursor;
        String status  ;
        String path = context.getDatabasePath(DB_NAME).getAbsolutePath();
        IUPAC_APPLICATION.database = SQLiteDatabase.openDatabase(path, null,
                SQLiteDatabase.OPEN_READWRITE);
        final String[] topics = {"Alkane","Haloalkane","Alcohol","Ether","Aldehyde","CarboxylicAcid","Amines"};
        for (int i =0 ; i<topics.length ; i++){
            String tableName = topics[i]+"_Rules";

            cursor = IUPAC_APPLICATION.database.rawQuery("SELECT * FROM  " + tableName + " ",null);
            cursor.moveToFirst();
            status = String.valueOf(cursor.getInt(cursor.getColumnIndex("Status")));
            while(!cursor.isLast()){
                cursor.moveToNext();
                status = status+","+String.valueOf(cursor.getInt(cursor.getColumnIndex("Status")));
            }
            cursor.close();
            sharedPreferences.edit().putString(topics[i],status).apply();
            status = "";
        }
        IUPAC_APPLICATION.database.close();
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
