package ir.armaani.hv.zabanak.models;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import com.google.common.collect.Lists;
import com.orm.SugarRecord;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ir.armaani.hv.zabanak.App;
import ir.armaani.hv.zabanak.helpers.ImageDownloader;

/**
 * Created by Siamak on 06/07/2016.
 */
public class Series extends SugarRecord implements Serializable {
    //-------------------------------- SERVER ATTRIBUTES -------------------------------------------
    private Integer serverId;
    private String caption;
    private Integer packageCount;
    private String imageFile;
    //-------------------------------- LOCAL ATTRIBUTES --------------------------------------------

    public Series() {
    }

    public Series(Integer serverId, String caption, Integer packageCount, String imageFile) {
        this.serverId = serverId;
        this.caption = caption;
        this.packageCount = packageCount;
        this.imageFile = imageFile;
    }


    public Boolean addNewSeriesFromServer(ir.armaani.hv.zabanak.rest.responses.Series serverSeries) {
        Series.deleteAll(Series.class, "server_id = ?", serverSeries.id.toString());
        if (!ImageDownloader.downloadImage(serverSeries.image))
            return false;
        for (ir.armaani.hv.zabanak.rest.responses.Package aPackage :
                serverSeries.packages)
            if (aPackage.image != null)
                if (!ImageDownloader.downloadImage(aPackage.image))
                    return false;
        this.serverId = serverSeries.id;
        this.caption = serverSeries.caption;
        this.imageFile = serverSeries.image;
        this.save();
        for (ir.armaani.hv.zabanak.rest.responses.Package aPackage :
                serverSeries.packages)
            new Package().addNewPackageFromServer(aPackage, this);

        return true;
    }

    public Integer getServerId() {
        return serverId;
    }

    public String getCaption() {
        return caption;
    }

    public Integer getPackageCount() {
        if (getPackages() == null)
            return 0;
        return getPackages().size();
    }

    public String getImageFile() {
        return imageFile;
    }

    public String getImageURL() {
        return App.getManifestValue("IMAGES_URL") + imageFile;
    }

    public String getImagePath() {
        return Environment.getExternalStorageDirectory() + "/" + App.getManifestValue("IMAGE_STORAGE_DIR") + "/" + imageFile;
    }

    public Bitmap getImage() {
        File imgFile = new File(getImagePath());
        if (imgFile.exists()) {
            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            return myBitmap;
        } else {
            return null;
        }
    }

    public List<Package> getPackages() {
        return Package.find(Package.class, "series = ?", getId().toString());
    }

    public LearningMethod getLearningMethod() {
        return LearningMethod.find(LearningMethod.class, "caption = ?", "Leitner").get(0);
    }

    //----------------------------------------------------------------------------------------------

    public Boolean isLearningStarted() {
        Long packagesCount = Package.count(Package.class, "SERIES = ? AND DATE_OF_LEARNING_START IS NOT NULL AND DATE_OF_LEARNING_END IS NULL", new String[]{getId().toString()});
        return packagesCount > 0 ? true : false;
    }

    public Boolean isLearningFinished() {
        Long packagesCount = Package.count(Package.class, "SERIES = ? AND DATE_OF_LEARNING_END IS NULL", new String[]{getId().toString()});
        return packagesCount > 0 ? true : false;
    }

    //-------------------------------- STATIC METHODS ----------------------------------------------
    public static List<Series> getListOfSeries() {
        return Lists.newArrayList(Series.findAll(Series.class));
    }

    public static List<Series> getListOfSeriesThatLearningStarted() {
        SQLiteDatabase database = App.getDatabase();
        Cursor cursor = database.rawQuery("SELECT DISTINCT SERIES FROM PACKAGE WHERE DATE_OF_LEARNING_START IS NOT NULL AND DATE_OF_LEARNING_END IS NULL", null);
        if (cursor.getCount() > 0) {
            String seriesIds = "";
            try {
                while (cursor.moveToNext())
                    seriesIds += String.valueOf(cursor.getInt(0)) + ",";
            } finally {
                seriesIds = seriesIds.substring(0, seriesIds.length() - 1);
                cursor.close();
            }
            return Series.find(Series.class, "ID IN (?) ", seriesIds);
        } else {
            cursor.close();
            return (new ArrayList<Series>());
        }
    }

    public static List<Series> getListOfSeriesThatLearningDoesNotStarted() {
        SQLiteDatabase database = App.getDatabase();
        Cursor cursor = database.rawQuery("SELECT DISTINCT SERIES FROM PACKAGE WHERE DATE_OF_LEARNING_START IS NULL", null);
        if (cursor.getCount() > 0) {
            String seriesIds = "";
            try {
                while (cursor.moveToNext())
                    seriesIds += String.valueOf(cursor.getInt(0)) + ",";
            } finally {
                seriesIds = seriesIds.substring(0, seriesIds.length() - 1);
                cursor.close();
            }
            return Series.find(Series.class, "ID IN (?) ", seriesIds);
        } else {
            cursor.close();
            return (new ArrayList<Series>());
        }
    }

    public static List<Series> getListOfSeriesThatTodayMostReview() {
        List<Series> series = getListOfSeriesThatLearningStarted();
        ArrayList<Series> result = new ArrayList<>();
        for (Series s : series)
            for (Package p : s.getPackages())
                if (p.getCountOfTodayWords() > 0)
                    result.add(s);
        return result;
    }
}
