package com.android.sandwichmaker.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

public class SandwichMakerDBHelper extends SQLiteOpenHelper {

    private static String DBNAME = "sandwichmaker.db";
    private static int DBVERSION = 1;

    public static final String PHI_AUTHORITY = "com.android.sandwichmaker.provider";
    public static final Uri PHI_DATA_MOMENTS_CONTENT_URI = Uri.parse("content://"
            + PHI_AUTHORITY + "/" + Tables.DATA_TABLE_MOMENTS);
    public static final Uri PHI_DATA_ACTIONS_CONTENT_URI = Uri.parse("content://"
            + PHI_AUTHORITY + "/" + Tables.DATA_TABLE_ACTIONS);

    public static final Uri PHI_INFO_MOMENTS_CONTENT_URI = Uri.parse("content://"
            + PHI_AUTHORITY + "/" + Tables.INFO_TABLE_MOMENTS);
    public static final Uri PHI_INFO_ACTIONS_CONTENT_URI = Uri.parse("content://"
            + PHI_AUTHORITY + "/" + Tables.INFO_TABLE_ACTIONS);

    public static final Uri PHI_INFO_LOCATIONS_CONTENT_URI = Uri.parse("content://"
            + PHI_AUTHORITY + "/" + Tables.INFO_TABLE_LOCATION);

    public SandwichMakerDBHelper(Context context) {

        super(context, DBNAME, null, DBVERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("Create table " + Tables.DATA_TABLE_MOMENTS + " ("
                + Columns.COLUMN_MOMENT_ID + " INTEGER PRIMARY KEY, "
                + Columns.COLUMN_MOMENT_DESC + " TEXT, "
                + Columns.COLUMN_MOMENT_LOCATION + " TEXT, "
                + Columns.COLUMN_MOMENT_TIME + " TEXT, "
                + Columns.COLUMN_MOMENT_RATING + " INTEGER " + ");");

        db.execSQL("Create table " + Tables.DATA_TABLE_ACTIONS + " ("
                + Columns.COLUMN_ACTION_ID + " INTEGER PRIMARY KEY, "
                + Columns.COLUMN_ACTION_DESC + " TEXT " + ");");


        db.execSQL("Create table " + Tables.INFO_TABLE_MOMENTS + " ("
                + Columns.COLUMN_MOMENT_ID + " INTEGER PRIMARY KEY, "
                + Columns.COLUMN_MOMENT_DESC + " TEXT, "
                + Columns.COLUMN_MOMENT_LOCATION + " TEXT, "
                + Columns.COLUMN_MOMENT_TIME + " TEXT, "
                + Columns.COLUMN_MOMENT_RATING + " INTEGER " + ");");

        db.execSQL("Create table " + Tables.INFO_TABLE_ACTIONS + " ("
                + Columns.COLUMN_ACTION_ID + " INTEGER PRIMARY KEY, "
                + Columns.COLUMN_ACTION_DESC + " TEXT " + ");");

        db.execSQL("Create table " + Tables.INFO_TABLE_LOCATION + " ("
                + Columns.COLUMN_LOC_ID + " INTEGER PRIMARY KEY, "
                + Columns.COLUMN_LOC_LAT + " LONG, "
                + Columns.COLUMN_LOC_LONG + " LONG, "
                + Columns.COLUMN_LOC_DESC + " TEXT " + ");");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Tables.DATA_TABLE_MOMENTS);
        db.execSQL("DROP TABLE IF EXISTS " + Tables.DATA_TABLE_ACTIONS);

        db.execSQL("DROP TABLE IF EXISTS " + Tables.INFO_TABLE_MOMENTS);
        db.execSQL("DROP TABLE IF EXISTS " + Tables.INFO_TABLE_ACTIONS);

        db.execSQL("DROP TABLE IF EXISTS " + Tables.INFO_TABLE_LOCATION);
        onCreate(db);
    }

    public interface Tables {
        public static String DATA_TABLE_MOMENTS = "data_moments";
        public static final String DATA_TABLE_ACTIONS = "data_actions";

        public static String INFO_TABLE_MOMENTS = "info_moments";
        public static final String INFO_TABLE_ACTIONS = "info_actions";

        public static final String INFO_TABLE_LOCATION = "info_loc";
    }

    public interface Columns {
        public static final String COLUMN_MOMENT_ID = "moment_id";
        public static final String COLUMN_MOMENT_DESC = "moment_desc";
        public static final String COLUMN_MOMENT_LOCATION = "moment_location";
        public static final String COLUMN_MOMENT_TIME = "moment_time";
        public static final String COLUMN_MOMENT_RATING = "moment_rating";

        public static final String COLUMN_ACTION_DESC = "action_desc";
        public static final String COLUMN_ACTION_ID = "action_id";

        public static final String COLUMN_LOC_DESC = "location_desc";
        public static final String COLUMN_LOC_LONG = "location_long";
        public static final String COLUMN_LOC_LAT = "location_lat";
        public static final String COLUMN_LOC_ID = "location_id";
    }

}
