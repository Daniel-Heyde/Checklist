package com.heyde.checklist.model;

import android.content.Context;
import android.widget.TableLayout;
import android.widget.TableRow;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Daniel on 10/17/2016.
 */

public class TaskList {
    private List<Task> mTasks;
    private Context mContext;
    private TableLayout mTaskTable;
    private List<TableRow> mTableRows;
    private String mName;
    private boolean mDeleting = false;


    public TaskList(Context context, TableLayout referenceTable) {
        Random rand = new Random();
        mTasks = new ArrayList<>();
        mTableRows = new ArrayList<>();
        mContext = context;
        mTaskTable = new TableLayout(mContext);
        mName = "temp";
        createTable(referenceTable);
        //TODO prevent too many tasks
    }

    private void createTable(TableLayout referenceTable) {
        mTaskTable.setWeightSum(10);
        mTaskTable.setLayoutParams(referenceTable.getLayoutParams());
    }

    public List<TableRow> getTableRows() {
        return mTableRows;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getName() {
        return mName;
    }

    public void refreshList() {
        for (Task task : mTasks) {
            if (task.isChecked()) {
                task.toggleCheckButton();
            }
        }
    }

    public void clearList() {
        mTableRows.clear();
        mTasks.clear();
    }

    public void checkForRemoval() {
        for (int i = 0; i < mTasks.size(); i++) {
            if (mTasks.get(i).isFlaggedForDeletion()) {
                mTasks.remove(i);
            }
        }
    }

    public void makeNewLine(Task newTaskObject) {

        TableRow newTaskRow = new TableRow(mContext);
        newTaskRow.setPadding(0, 30, 0, 0);

        // add views to new row
        newTaskRow.addView(newTaskObject.getTextView());
        newTaskRow.addView(newTaskObject.getCheckButton());

        // add new row to table
//        mTaskTable.addView(newTaskRow);
        mTableRows.add(newTaskRow);
    }



    public List<Task> getTasks() {
        return mTasks;
    }

    public void addTask(Task task) {
        mTasks.add(task);
    }

}
