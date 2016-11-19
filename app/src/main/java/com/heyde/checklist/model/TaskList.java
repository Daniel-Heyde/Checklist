package com.heyde.checklist.model;

import android.content.Context;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;

import java.util.ArrayList;
import java.util.List;

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


    public TaskList(Context context, LinearLayout referenceLayout) {
        mTasks = new ArrayList<>();
        mLayoutRows = new ArrayList<>();
        mContext = context;
        mTaskTable = new TableLayout(mContext);
        mName = "New List";
        mNameChanged = false;
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

//    public void makeNewLine(Task newTaskObject) { //TASK TABLE VERSION
//
//        TableRow newTaskRow = new TableRow(mContext);
//
//        newTaskRow.setPadding(0, 30, 0, 0);
//
//        // add views to new row
//        newTaskRow.addView(newTaskObject.getTextView());
//        newTaskRow.addView(newTaskObject.getCheckButton());
//        newTaskRow.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//                View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder();
//                v.startDrag(null, shadowBuilder, v, 0);
//                return false;
//            }
//        });
//        newTaskRow.setOnDragListener(new View.OnDragListener() {
//            @Override
//            public boolean onDrag(View v, DragEvent event) {
//                switch(event.getAction()){
//                    case DragEvent.ACTION_DRAG_STARTED:
//                        Toast toast = Toast.makeText(mContext, "DRAG STARTED", Toast.LENGTH_SHORT);
//                        toast.show();
//                        break;
//                    case DragEvent.ACTION_DRAG_ENDED:
//                        toast = Toast.makeText(mContext, "DRAG ENDED", Toast.LENGTH_SHORT);
//                        toast.show();
//                        break;
//                    case DragEvent.ACTION_DRAG_ENTERED:
//                        toast = Toast.makeText(mContext, "DRAG ENTERED", Toast.LENGTH_SHORT);
//                        toast.show();
//                        break;
//                }
//                return false;
//            }
//        });

    public void makeNewLine(Task newTaskObject) {

        LinearLayout newLayout = new LinearLayout(mContext);

        newLayout.setOrientation(LinearLayout.HORIZONTAL);
        newLayout.setPadding(0, 30, 0, 0);

        // add views to new row
        newLayout.addView(newTaskObject.getTextView());
        newLayout.addView(newTaskObject.getCheckButton());
//        newLayout.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//                View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder();
//                v.startDrag(null, shadowBuilder, v, 0);
//                return false;
//            }
//        });
//        newLayout.setOnDragListener(new View.OnDragListener() {
//            @Override
//            public boolean onDrag(View v, DragEvent event) {
//                switch(event.getAction()){
//                    case DragEvent.ACTION_DRAG_STARTED:
//                        Toast toast = Toast.makeText(mContext, "DRAG STARTED", Toast.LENGTH_SHORT);
//                        toast.show();
//                        break;
//                    case DragEvent.ACTION_DRAG_ENDED:
//                        toast = Toast.makeText(mContext, "DRAG ENDED", Toast.LENGTH_SHORT);
//                        toast.show();
//                        break;
//                    case DragEvent.ACTION_DRAG_ENTERED:
//                        toast = Toast.makeText(mContext, "DRAG ENTERED", Toast.LENGTH_SHORT);
//                        toast.show();
//                        break;
//                }
//                return false;
//            }
//        });

        // add new row to table
//        mTaskTable.addView(newTaskRow);
        mLayoutRows.add(newLayout);
    }



    public List<Task> getTasks() {
        return mTasks;
    }

    public void addTask(Task task) {
        mTasks.add(task);
    }

}
