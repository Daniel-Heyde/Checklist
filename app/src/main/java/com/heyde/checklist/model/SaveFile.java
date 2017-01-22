package com.heyde.checklist.model;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by Daniel on 11/4/2016.
 */

public class SaveFile extends AsyncTask<TaskList, Void, Void> {

    private Context mContext;

    public SaveFile(Context context) {
        mContext = context;
    }

    @Override

    protected Void doInBackground(TaskList... tList) {
        TaskList list = tList[0];// varargs? research more into this
        FileWriter mFileWriter = null;
            File directory = new File(mContext.getFilesDir() + File.separator + "lists"); // list files will be stored in data/data/com.heyde.checklist/files/lists
            File textFile = new File(directory + File.separator + list.getName() + ".txt");
            try {
                mFileWriter = new FileWriter(textFile);
                for (Task task : list.getTasks()) {
                    mFileWriter.write(task.taskToString() + "\n");
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    mFileWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (NullPointerException npe){
                    npe.printStackTrace();
                }
            }
        return null;
    }
}

