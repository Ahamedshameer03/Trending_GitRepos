package com.my_project.trending_github_repositories;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.HashMap;


public class RepoProvider extends ContentProvider {
    // Constants
    public static final String AUTHORITY = "com.my_project.trending_github_repositories";
    public static final String PATH_REPO_LIST = "REPO_LIST";
    public static final String PATH_REPO_URL = "REPO_URL";

    // Uri
    public static final Uri CONTENT_URI_LIST = Uri.parse("content://"+AUTHORITY+"/"+PATH_REPO_LIST);
    public static final Uri CONTENT_URI_URL = Uri.parse("content://"+AUTHORITY+"/"+PATH_REPO_URL);

    // Matcher
    public static final int REPO_LIST = 1;
    public static final int REPO_URL = 2;
    public static UriMatcher MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
    static{
        MATCHER.addURI(AUTHORITY, PATH_REPO_LIST, REPO_LIST);
        MATCHER.addURI(AUTHORITY, PATH_REPO_URL, REPO_URL);
    }

    // DB Column
    public static final String _ID = "_id";
    public static final String AUTHOR_R = "author";
    public static final String NAME_R = "name";
    public static final String AVATAR_R = "avatar";
    public static final String DESCRIPTION_R = "description";
    public static final String LANGUAGE_R = "language";
    public static final String LANGUAGECOLR_R = "languageColor";
    public static final String USERNAME_R = "username";
    public static final String URL_R = "url";
    public static final String STARS_R = "stars";
    public static final String FORKS_R = "forks";
    public static final String CUR_PER_STARS_R = "currentPeriodStars";
    public static final String BUILTBY_R = "builtBy";

    private SQLiteDatabase db;
    static final String DATABASE_NAME = "RepoList";
    static final String REPOSITORIES_TABLE_NAME = PATH_REPO_LIST;
    static final int DATABASE_VERSION = 1;

    static final String CREATE_DB_TABLE =
            " CREATE TABLE " + REPOSITORIES_TABLE_NAME +
                    " ("+_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    " "+NAME_R+" TEXT NOT NULL, " +
                    " "+LANGUAGE_R+" TEXT NOT NULL, " +
                    " "+LANGUAGECOLR_R+" TEXT NOT NULL, " +
                    " "+AUTHOR_R+" TEXT NOT NULL, " +
                    " "+AVATAR_R+" TEXT NOT NULL, " +
                    " "+USERNAME_R+" TEXT NOT NULL, " +
                    " "+URL_R+" TEXT NOT NULL, " +
                    " "+BUILTBY_R+" TEXT NOT NULL, " +
                    " "+STARS_R+" INTEGER NOT NULL, " +
                    " "+FORKS_R+" INTEGER NOT NULL, " +
                    " "+CUR_PER_STARS_R+" INTEGER NOT NULL, " +
                    " "+DESCRIPTION_R+" TEXT NOT NULL);";

    
    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context){
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            sqLiteDatabase.execSQL(CREATE_DB_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + REPOSITORIES_TABLE_NAME);
        }
    }
    
    @Override
    public boolean onCreate() {
        Context context = getContext();
        DatabaseHelper dbHelper = new DatabaseHelper(context);
        db = dbHelper.getWritableDatabase();
        return db != null;
    }

    private static HashMap<String, String> REPOSITORIES_PROJECTION_MAP;
    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
            SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
            qb.setTables(REPOSITORIES_TABLE_NAME);

            switch (MATCHER.match(uri)) {
                case REPO_LIST:
                    qb.setProjectionMap(REPOSITORIES_PROJECTION_MAP);
                    break;

                case REPO_URL:
                    qb.appendWhere( _ID + "=" + uri.getPathSegments().get(1));
                    break;

                default:
            }

            if (sortOrder == null || sortOrder == ""){

                sortOrder = NAME_R;
            }

            Cursor c = qb.query(db,	projection,	selection,
                    selectionArgs,null, null, sortOrder);
            c.setNotificationUri(getContext().getContentResolver(), uri);
            return c;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (MATCHER.match(uri)){
            /**
             * Get all student records
             */
            case REPO_LIST:
                return "vnd.android.cursor.dir/vnd.example.students";
            /**
             * Get a particular student
             */
            case REPO_URL:
                return "vnd.android.cursor.item/vnd.example.students";
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {

        long rowID = db.insert(	REPOSITORIES_TABLE_NAME, "", contentValues);

        /**
         * If record is added successfully
         */
        if (rowID > 0) {
            Uri _uri = ContentUris.withAppendedId(CONTENT_URI_LIST, rowID);
            getContext().getContentResolver().notifyChange(_uri, null);
            return _uri;
        }

        throw new SQLException("Failed to add a record into " + uri);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int count = 0;
        switch (MATCHER.match(uri)){
            case REPO_LIST:
                count = db.delete(REPOSITORIES_TABLE_NAME, selection, selectionArgs);
                break;

            case REPO_URL:
                String id = uri.getPathSegments().get(1);
                count = db.delete( REPOSITORIES_TABLE_NAME, _ID +  " = " + id +
                                (!TextUtils.isEmpty(selection) ?" AND (" + selection + ')' : ""), selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
            int count = 0;
            switch (MATCHER.match(uri)) {
                case REPO_LIST:
                    count = db.update(REPOSITORIES_TABLE_NAME, values, selection, selectionArgs);
                    break;

                case REPO_URL:
                    count = db.update(REPOSITORIES_TABLE_NAME, values,
                            _ID + " = " + uri.getPathSegments().get(1) +
                                    (!TextUtils.isEmpty(selection) ? " AND (" +selection + ')' : ""), selectionArgs);
                    break;
                default:
                    throw new IllegalArgumentException("Unknown URI " + uri );
            }

            getContext().getContentResolver().notifyChange(uri, null);
            return count;
    }
}
