package com.heyde.checklist.model;

import android.content.Context;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Daniel on 10/25/2016.
 */

public class FileController
{

    private Context mContext;
    private BufferedReader mBufferedReader;

    public FileController(Context context)
    {
        mContext = context;
    }

    public void saveList(TaskList list)
    {

        SaveFile saveFile = new SaveFile(mContext);
        saveFile.execute(list);
    }

    public List<String> loadFile(String fileName)
    {

        File file = new File(mContext.getFilesDir() + File.separator + "lists" + File.separator + fileName + ".txt");
        List list = new ArrayList();
        try
        {
            mBufferedReader = new BufferedReader(new FileReader(file));
        } catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        try
        {
            String line;
            while ((line = mBufferedReader.readLine()) != null)
            {
                list.add(line);
            }
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        return list;
    }


    public List<String> getAvailableFiles()
    {
        File dir = new File(mContext.getFilesDir() + File.separator + "lists");
        dir.mkdirs();
        File[] files = dir.listFiles();
        List<String> availableFiles = new ArrayList<>();
        if (files.length != 0)
        {
            for (File file : files)
            {
                String filename = file.getName();
                filename = filename.substring(0, filename.length() - 4);
                availableFiles.add(filename);
            }
        }
        return availableFiles;
    }

    public void deleteList(String filename)
    {
        File forDelete = new File(mContext.getFilesDir() + File.separator + "lists" + File.separator + filename + ".txt");
        forDelete.delete();
    }


}
