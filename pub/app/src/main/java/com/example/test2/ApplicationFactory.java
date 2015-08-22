package com.example.test2;

import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;

import dataSources.DataSourceUtilities;
import dataSources.InMemoryKidsDataSource;
import dataSources.KidsDataSource;
import dataSources.XmlKidsDataSource;

/**
 * Created by LouisaSeever on 7/27/2015.
 */


public class ApplicationFactory {
    private final String TAG = "ApplicationFactory";
    private static ApplicationFactory mFactory = new ApplicationFactory();

    public static ApplicationFactory getInstance() {
        return mFactory;
    }

    private static KidsDataSource mKidsData;

    private ApplicationFactory() {
    }

    public  KidsDataSource getKidsDataSource(){
        if (mKidsData == null){
              //load any existing data
            try {
                //.mKidsData = new InMemoryKidsDataSource();
                mKidsData = new XmlKidsDataSource();
                //mKidsData.clear();
                //mKidsData.load();
            }
            catch(Exception ex){
                Log.d(TAG, "Cannot load Kids file: " + ex.getMessage());
            }
        }
        return mKidsData;
    }



}
