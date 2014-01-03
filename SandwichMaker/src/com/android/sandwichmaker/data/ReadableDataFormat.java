package com.android.sandwichmaker.data;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.Environment;
import android.util.Log;

import com.android.sandwichmaker.Utils;
import com.android.sandwichmaker.provider.SandwichMakerDBHelper;
import com.android.sandwichmaker.test.ControlPanelActivity;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Created by uttamb on 12/6/13.
 */
public class ReadableDataFormat {
    Context mContext;


    public ReadableDataFormat(Context context) {
        mContext = context;

    }

    public void generateReports() {
        try {
            FileWriter momentWriter = createFile("MomentData.txt");
            FileWriter actionWriter = createFile("ActionData.txt");
            FileWriter locationWriter = createFile("LocationData.txt");
        generateActionReport(actionWriter);
            actionWriter.close();
            generateMomentReport(momentWriter);
            momentWriter.close();
            generateLocationReport(locationWriter);
            locationWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void generateMomentReport(FileWriter momentWriter) {
        ContentResolver cr = mContext.getContentResolver();
        Cursor c = cr.query(SandwichMakerDBHelper.PHI_DATA_MOMENTS_CONTENT_URI, null, null, null, null);
        StringBuffer sb = new StringBuffer("No data found");
        Log.v(Utils.DEBUG_TAG, "generating report");
        if (c != null) {
            sb.delete(0, sb.length());
            while (c.moveToNext()) {

                int actionIndex = c.getColumnIndex(SandwichMakerDBHelper.Columns.COLUMN_MOMENT_DESC);
                int ratingIndex = c.getColumnIndex(SandwichMakerDBHelper.Columns.COLUMN_MOMENT_RATING);

                int dateIndex = c.getColumnIndex(SandwichMakerDBHelper.Columns.COLUMN_MOMENT_TIME);

                int locIndex = c.getColumnIndex(SandwichMakerDBHelper.Columns.COLUMN_MOMENT_LOCATION);

                String actionDesc = c.getString(actionIndex);
                String momentRating = c.getString(ratingIndex);
                String momentTime = c.getString(dateIndex);
                String momentLoc = c.getString(locIndex);


                long yourmilliseconds = Long.parseLong(momentTime);
                SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, HH:mm");

                Date resultdate = new Date(yourmilliseconds);

                String [] coordinates = momentLoc.split(",");
             Double distance =   Utils.distance(Double.parseDouble(coordinates[1]),Double.parseDouble(coordinates[0]),Utils.HOME_LATITUDE,Utils.HOME_LONGITUDE);

                Log.v(Utils.DEBUG_TAG, actionDesc + " Rating: " + momentRating);
                sb.append(actionDesc + "        " + momentRating + "        " +sdf.format(resultdate)+ "        " +distance.toString());
                sb.append("\n");
            }
        try {
                momentWriter.write(sb.toString());

            } catch (IOException e) {
                Log.v(Utils.DEBUG_TAG, "Unable to write anything to file");
                Log.v(Utils.DEBUG_TAG, e.toString());
                e.printStackTrace();
            } finally {

            }
        }

    }


    public FileWriter createFile(String fileName) throws IOException {
        File appDir = Utils.getAppFolder();

        File file = new File(appDir,fileName);
        if (file != null || file.exists())
            file.delete();
        file.createNewFile();
        return new FileWriter(file);
     }


    public void generateActionReport(FileWriter actionWriter) {

        ContentResolver cr = mContext.getContentResolver();
        Cursor c = cr.query(SandwichMakerDBHelper.PHI_DATA_ACTIONS_CONTENT_URI, null, null, null, null);
        StringBuffer sb = new StringBuffer("No data found");
        Log.v(Utils.DEBUG_TAG, "generating report");
        if (c != null) {
            sb.delete(0, sb.length());
            while (c.moveToNext()) {

                int actionIndex = c.getColumnIndex(SandwichMakerDBHelper.Columns.COLUMN_ACTION_DESC);

                String actionDesc = c.getString(actionIndex);

                Log.v(Utils.DEBUG_TAG, actionDesc);
                sb.append(actionDesc);
                sb.append("\n");
            }
        try {
                actionWriter.write(sb.toString());

            } catch (IOException e) {
                Log.v(Utils.DEBUG_TAG, "Unable to write anything to file");
                Log.v(Utils.DEBUG_TAG, e.toString());
                e.printStackTrace();
            }
        }

    }
    public void generateLocationReport(FileWriter actionWriter) {

        ContentResolver cr = mContext.getContentResolver();
        Cursor c = cr.query(SandwichMakerDBHelper.PHI_INFO_LOCATIONS_CONTENT_URI, null, null, null, null);
        StringBuffer sb = new StringBuffer("No data found");
        Log.v(Utils.DEBUG_TAG, "generating report");
        if (c != null) {
            sb.delete(0, sb.length());
            while (c.moveToNext()) {

                int actionIndex = c.getColumnIndex(SandwichMakerDBHelper.Columns.COLUMN_LOC_DESC);

                String actionDesc = c.getString(actionIndex);

                Log.v(Utils.DEBUG_TAG, actionDesc);
                sb.append(actionDesc);
                sb.append("\n");
            }
            try {
                actionWriter.write(sb.toString());

            } catch (IOException e) {
                Log.v(Utils.DEBUG_TAG, "Unable to write anything to file");
                Log.v(Utils.DEBUG_TAG, e.toString());
                e.printStackTrace();
            }
        }

    }
}
