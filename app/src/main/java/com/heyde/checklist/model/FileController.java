package com.heyde.checklist.model;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Daniel on 10/25/2016.
 */

public class FileController{ //TODO set this up to run in background thread

    private Context mContext;
    private FileWriter mFileWriter;
    private BufferedReader mBufferedReader;

    public FileController(Context context) {
        mContext = context;
    }

    public void saveList(TaskList list) {
        File directory = new File(mContext.getFilesDir()+File.separator+"lists"); // list files will be stored in data/data/com.heyde.checklist/files/lists
        File textFile = new File(directory + File.separator + list.getName() + ".txt");
        try {

            directory.mkdirs();
            textFile.createNewFile();

            mFileWriter = new FileWriter(textFile);
            for (Task task : list.getTasks()){
                mFileWriter.write(task.getTaskText() + ":::" + task.isChecked()+"\n");
                Log.i("WRITING", task.getTaskText() + ":::" + task.isChecked());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally{
            try {
                mFileWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public List<String> loadFile(String fileName) {//FIXME loading temp???????????????????????
        File file = new File(mContext.getFilesDir()+File.separator + "lists" + File.separator + fileName + ".txt");
        List list = new ArrayList();
        try {
            mBufferedReader = new BufferedReader(new FileReader(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            String line;
            while((line = mBufferedReader.readLine())!=null){
                list.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<String> getAvailableFiles(){
        File dir = new File(mContext.getFilesDir()+File.separator + "lists");
        File[] files = dir.listFiles();
        List<String> availableFiles = new ArrayList<>();
        if (files!=null) {
            for (File file : files) {
                String filename = file.getName();
                filename = filename.substring(0, filename.length() - 4);
                availableFiles.add(filename);
            }
        }
        return availableFiles;
    }



}
