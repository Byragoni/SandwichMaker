package com.android.sandwichmaker.data;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.android.sandwichmaker.Utils;
import com.android.sandwichmaker.provider.SandwichMakerDBHelper;

import java.util.ArrayList;

/**
 * Created by uttamb on 12/9/13.
 */
public class InfoFromData {


    private  Context mContext;

    public InfoFromData(Context context) {
        mContext = context;

    }

    public boolean updateLocationInfo(){
        //TO DO
        /* Go through all the moment info (new and old) and create new locations
         locations into clusters with an approximate value
         */
        //long [] locations = new long [15];
        ArrayList<Double> locations = new ArrayList<Double>();
        ArrayList<Double> existingLocations = new ArrayList<Double>();

        ContentResolver cr = mContext.getContentResolver();
        Cursor c = cr.query(SandwichMakerDBHelper.PHI_DATA_MOMENTS_CONTENT_URI, null, null, null, null);
        int locIndex = c.getColumnIndex(SandwichMakerDBHelper.Columns.COLUMN_MOMENT_LOCATION);


        while(c!=null && c.moveToNext()){
            Log.e(Utils.DEBUG_TAG,Integer.toString(c.getType(locIndex)));
            String momentLoc = c.getString(locIndex);
            Log.e(Utils.DEBUG_TAG,momentLoc);
            String [] coordinates = momentLoc.split(",");
            Double distance =   Utils.distance(Double.parseDouble(coordinates[1]), Double.parseDouble(coordinates[0]), Utils.HOME_LATITUDE, Utils.HOME_LONGITUDE);
            locations.add(distance);
         }

        Cursor infoCursor = cr.query(SandwichMakerDBHelper.PHI_INFO_LOCATIONS_CONTENT_URI, null, null, null, null);
        int infoLocation = infoCursor.getColumnIndex(SandwichMakerDBHelper.Columns.COLUMN_LOC_DESC);

        while(infoCursor.moveToNext()){
            String infoDistance = infoCursor.getString(infoLocation);
           // String [] coordinates = infoDistance.split(",");
           // Double distance =   Utils.distance(Double.parseDouble(coordinates[1]), Double.parseDouble(coordinates[0]), Utils.HOME_LATITUDE, Utils.HOME_LONGITUDE);

            existingLocations.add(Double.parseDouble(infoDistance));
         }

        for(Double location: locations){

            if(isThereAnythingCloseEnough(existingLocations,location)){



            } else{
                ContentValues values = new ContentValues();
                values.put(SandwichMakerDBHelper.Columns.COLUMN_LOC_DESC,location.toString());
                cr.insert(SandwichMakerDBHelper.PHI_INFO_LOCATIONS_CONTENT_URI,values);
            }

        }

        return false;
    }

    private boolean isThereAnythingCloseEnough(ArrayList<Double> existingLocations, Double location) {
      //  boolean newLoc = true;
        for(Double existing : existingLocations){
            if(location<existing+Utils.DISTANCE_ERROR_MARGIN && location>existing-Utils.DISTANCE_ERROR_MARGIN)
                return true;

        }

       return false;
    }

    public boolean updateActionInfo(){


        //TO DO
        /* Use some kind of parsing and cluster different actions, add new actions, remove non-fitting actions

         */

        return false;
    }
    public boolean updateMomentInfo(){
        //TO DO
        /*
        Update info table with modified values of action, location and time.
         */
       return false;
    }


}
