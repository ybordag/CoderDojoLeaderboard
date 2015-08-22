package dataSources;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.IOException;

/**
 * Created by LouisaSeever on 7/30/2015.
 */
public class DataSourceUtilities {
    private static final String TAG = "DataSourceUtilities";

    /* Checks if external storage is available for read and write */
    public static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    /* Checks if external storage is available to at least read */
    public static boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }

    //******************************
    static private void logDirectoryContent(File dir){
        if (dir != null){
            if (dir.exists()) {
                //Debug logging of directory contents
                File files[] = dir.listFiles();
                if (files != null) {
                    Log.d(TAG, "dir name: " + dir.getAbsolutePath());
                    Log.d(TAG, "dir file count: " + files.length);
                    for (int i = 0; i < files.length; i++) {
                        Log.d(TAG, "FileName:" + files[i].getName());
                    }
                } else {
                    Log.d(TAG, "no files exist");
                }
            }
            else{
                Log.d(TAG, dir.getAbsolutePath() + " does not exist");
            }
        }
        else{
            Log.d(TAG, "directory is null");
        }
    }

    public static File getDataStorageDir() {
        /*
        File rootDir = Environment.getRootDirectory();
        logDirectoryContent(rootDir);

        File externalStorageDir = Environment.getExternalStorageDirectory();
        logDirectoryContent(externalStorageDir);

        File dataDir =  Environment.getDataDirectory();
        logDirectoryContent(dataDir);
        */

        // Get the directory for the user's public data directory.
        File publicDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
        logDirectoryContent(publicDir);

        return publicDir;
    }

    public static void deleteDataStorageFile(String fileName){
        //delete any existing file
        File publicDir = getDataStorageDir();
        File existingFile = new File(publicDir,fileName);
        existingFile.delete();

    }

    public static File getDataStorageFile(String fileName) throws Exception{
        File publicDir = getDataStorageDir();
        //do any necessary directory setup
        publicDir.mkdirs();

        File dataFile = new File(publicDir, fileName);
        if (!dataFile.exists()){
            Log.d(TAG, "getDataSytorageFile: create file :" + dataFile.getPath());
            dataFile.createNewFile();
        }

        Log.d(TAG, "getDataStorageFile " + dataFile.getPath());
        return dataFile;
    }

}
