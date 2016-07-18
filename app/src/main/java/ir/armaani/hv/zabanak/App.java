package ir.armaani.hv.zabanak;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import com.onesignal.OneSignal;
import com.orm.SugarContext;

import ir.armaani.hv.zabanak.models.LearningMethod;
import ir.armaani.hv.zabanak.models.LearningMethodStep;
import ir.armaani.hv.zabanak.rest.RestClient;

/**
 * Created by Siamak on 06/07/2016.
 */
public class App extends Application {

    private static Context context;

    @Override
    public void onCreate() {
        context = getApplicationContext();
        SugarContext.init(context);
        OneSignal.startInit(this).init();
        RestClient.init();

        if (getSharedPreferences().getBoolean("isFirstTime" , true) == true)
            initializeApplicationForFirstTime();

        super.onCreate();
    }

    private void initializeApplicationForFirstTime() {
        try {
            LearningMethodStep.deleteAll(LearningMethodStep.class);
            LearningMethod.deleteAll(LearningMethod.class);
        }catch (Exception e) {}

        LearningMethod learningMethod = new LearningMethod("Leitner");
        learningMethod.save();

        new LearningMethodStep(1 , 2 , 2 , learningMethod).save();
        new LearningMethodStep(2 , 3 , 4 , learningMethod).save();
        new LearningMethodStep(3 , 4 , 8 , learningMethod).save();
        new LearningMethodStep(4 , 5 , 16 , learningMethod).save();
        new LearningMethodStep(5 , 0 , 0 , learningMethod).save();

        getSharedPreferences().edit().putBoolean("isFirstTime" , false).commit();

    }

    @Override
    public void onTerminate() {
        SugarContext.terminate();

        super.onTerminate();
    }

    public static Context getContext() {
        return context;
    }

    public static String getManifestValue(String key)  {
        ApplicationInfo applicationInfo = null;
        try {
            applicationInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
        } catch (PackageManager.NameNotFoundException e) {
            return "";
        }
        Bundle bundle = applicationInfo.metaData;
        String value = bundle.getString(key);;
        return value;
    }

    public static SharedPreferences getSharedPreferences() {
        return context.getSharedPreferences(getManifestValue("PREFERENCE_FILE"), MODE_PRIVATE);
    }

    public static SQLiteDatabase getDatabase() {
  /*      Field field = null;
      try {*/
            //field = SugarContext.getSugarContext().getClass().getField("sugarDb");
            //field.setAccessible(true);
            //SugarDb sugarDb = (SugarDb) field.get(SugarContext.getSugarContext());
            //return sugarDb.getDB();
            SQLiteDatabase db = context.openOrCreateDatabase(App.getManifestValue("DATABASE"), context.MODE_PRIVATE , null);
            return db;
/*        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }*/
/*
        return null;
    */}
}
