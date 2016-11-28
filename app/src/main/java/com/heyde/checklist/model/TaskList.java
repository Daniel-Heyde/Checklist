package com.heyde.checklist.model;

import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;

import com.heyde.checklist.ui.MainActivity;

import java.util.ArrayList;
import java.util.List;

import static android.R.drawable.ic_delete;
import static android.R.drawable.ic_menu_delete;

/**
 * Created by Daniel on 10/17/2016.
 */

public class TaskList {
    private List<Task> mTasks;
    private Context mContext;
    private TableLayout mTaskTable;
    private List<TableRow> mTableRows;
    private List<LinearLayout> mLayoutRows;
    private String mName;
    private Boolean mNameChanged;
    private Boolean mIsDeletable;
    private MainActivity mActivity;



    public TaskList(Context context, LinearLayout referenceLayout, MainActivity activity) {
        mTasks = new ArrayList<>();
        mLayoutRows = new ArrayList<>();
        mContext = context;
        mActivity = activity;
        mTaskTable = new TableLayout(mContext);
        mName = "New List";
        mNameChanged = false;
        mIsDeletable = false;
        //TODO prevent too many tasks
    }

//    private void createTable(TableLayout referenceTable) {
//        mTaskTable.setWeightSum(10);
//        mTaskTable.setLayoutParams(referenceTable.getLayoutParams());
//    }

//    public List<TableRow> getTableRows() {
//        return mTableRows;
//    }

    public List<LinearLayout> getTableRows() {
        return mLayoutRows;
    }

    public Boolean getNameChanged() {
        return mNameChanged;
    }

    public void setName(String name) {

        mNameChanged = true;
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

    public void checkForRemoval() {

        for (int i = 0; i < mTasks.size(); i++) {
            if (mTasks.get(i).isFlaggedForDeletion()) {
                for (int rowInd = 0; rowInd < mLayoutRows.size(); rowInd++) {
                    LinearLayout row = mLayoutRows.get(rowInd);
                    ImageButton rowImage = (ImageButton) row.getChildAt(1);
                    if (rowImage.getTag().equals("delete")) {
//                        mTaskTable.removeView(row);
                        mLayoutRows.remove(row);
                        mTasks.remove(i);
                    }
                }
            }
        }
    }

    public Context getContext() {
        return mContext;
    }

    public void makeNewLine(final Task newTaskObject) {

        final LinearLayout newLayout = new LinearLayout(mContext);

        newLayout.setOrientation(LinearLayout.HORIZONTAL);
        newLayout.setPadding(0, 30, 0, 0);

        // add views to new row
        newLayout.addView(newTaskObject.getTextView());
        newLayout.addView(newTaskObject.getCheckButton());

        newLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mIsDeletable) {
                    //TODO confirm with user? make long click then delete multiple?
                    mLayoutRows.remove(newLayout);
                } else {
                    newTaskObject.toggleCheckButton();
                }
            }
        });

        for (int i = 0; i < newLayout.getChildCount(); i++) {
            newLayout.getChildAt(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mIsDeletable) {
                        //TODO confirm with user? make long click then delete multiple?
                        mLayoutRows.remove(newLayout);
                        mActivity.displayTable(TaskList.this);
                        if (mLayoutRows.isEmpty()){
                            mActivity.toggleEditMode();
                        }
                    } else {
                        newTaskObject.toggleCheckButton();
                    }
                }
            });
        }

        // add row to linearlayout
        mLayoutRows.add(newLayout);
    }

    public void toggleDeletable(){
        if (mIsDeletable) {
            mIsDeletable = false;
        } else{
            mIsDeletable = true;
        }
    }

    public List<Task> getTasks() {
        return mTasks;
    }

    public void addTask(Task task) {
        mTasks.add(task);
    }

}


