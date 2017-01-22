package com.heyde.checklist.model;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.heyde.checklist.R;
import com.heyde.checklist.ui.MainActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Daniel on 10/17/2016.
 */

public class TaskList {
    private List<Task> mTasks;
    private Context mContext;
    private List<LinearLayout> mLayoutRows;
    private String mName;
    private Boolean mNameChanged;
    private List<Task> mDeleteList;
    private MainActivity mActivity;
    private Boolean mInListEditMode;

    public TaskList(Context context, MainActivity activity) {
        mTasks = new ArrayList<>();
        mLayoutRows = new ArrayList<>();
        mDeleteList = new ArrayList<>();
        mContext = context;
        mActivity = activity;
        mName = "New List";
        mInListEditMode = false;
    }

    public List<LinearLayout> getLayoutRows() {
        return mLayoutRows;
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

    public Context getContext() {
        return mContext;
    }

    public void reorderList(int pos1, int pos2) {
        LinearLayout row = mLayoutRows.get(pos1);
        Task task = mTasks.get(pos1);
        mTasks.remove(task);
        mTasks.add(pos2, task);
        mLayoutRows.remove(row);
        mLayoutRows.add(pos2, row);
        for (int i = 0; i < mLayoutRows.size(); i++) {
            if (!mDeleteList.contains(mTasks.get(i))) {
                setRowBackgroundColor(mLayoutRows.get(i));
            }
        }
    }

    private void generateDeleteList() {
        mDeleteList.clear();
        for (Task task : mTasks) {
            if (task.isEditable()) {
                mDeleteList.add(task);
            }
        }
    }

    public int getDeleteListSize() {
        return mDeleteList.size();
    }


    public void makeNewLine(final Task newTaskObject) {

        final LinearLayout newLayout = new LinearLayout(mContext);

        newLayout.setOrientation(LinearLayout.HORIZONTAL);
        newLayout.setPadding(20, 30, 0, 30); // left, top, right, bottom

        // add views to new row
        ImageView dragButton = new ImageView(mContext);
        dragButton.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.drag_handle));
        dragButton.setPadding(0, 0, 10, 0);
//        dragButton.setScaleX((float).9);
//        dragButton.setScaleY((float).9);
        newLayout.addView(dragButton);
        newLayout.addView(newTaskObject.getTextView());
        newLayout.addView(newTaskObject.getCheckBox());

        newLayout.setGravity(Gravity.CENTER_VERTICAL);

        newLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (newTaskObject.isEditable()) { // if selected, edit
                    mActivity.addTask(false, newTaskObject);
                    newTaskObject.restoreCheckButton();
                    setRowBackgroundColor(newLayout);
                    mActivity.switchToDelete();
                    mDeleteList.remove(newTaskObject);
                } else { // if not selected, toggle
                    newTaskObject.toggleCheckButton();
                }
            }
        });

        // longclick to handle select and delete
        newLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (!mInListEditMode) {
                    if (!newTaskObject.isEditable()) {
                        setDeleteBackgroundColor(newLayout);
                        newTaskObject.switchToEdit();
                    } else {
                        setRowBackgroundColor(newLayout);
                        newTaskObject.restoreCheckButton();
                    }
                    generateDeleteList();
                    mActivity.switchToDelete();
                }
                return true;
            }
        });


        for (int i = 1; i < newLayout.getChildCount(); i++) { // onclick listener for buttons and text
            newLayout.getChildAt(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (newTaskObject.isEditable()) { // if selected, edit
                        mActivity.addTask(false, newTaskObject);
                        newTaskObject.restoreCheckButton();
                        setRowBackgroundColor(newLayout);
                        mActivity.switchToDelete();
                        mDeleteList.remove(newTaskObject);
                    } else { // if not selected, toggle
                        newTaskObject.toggleCheckButton();
                    }
                }
            });

            // longclick to handle select and delete
            newLayout.getChildAt(i).setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (!mInListEditMode) {
                        if (!newTaskObject.isEditable()) {
                            setDeleteBackgroundColor(newLayout);
                            newTaskObject.switchToEdit();
                        } else {
                            setRowBackgroundColor(newLayout);
                            newTaskObject.restoreCheckButton();
                        }
                        generateDeleteList();
                        mActivity.switchToDelete();
                    }
                    return true;
                }
            });
        }

        // add row to linearlayout
        mLayoutRows.add(newLayout);

        setRowBackgroundColor(newLayout);
    }

    public void refreshRows() {
        for (int i = 0; i < mLayoutRows.size(); i++) {
            if (!mLayoutRows.get(i).getChildAt(0).toString().equals(mTasks.get(i).getTaskText())) {
                TextView layoutText = (TextView) mLayoutRows.get(i).getChildAt(1);
                layoutText.setText(mTasks.get(i).getTaskText());
            }
        }
    }

    public void deleteSelected() {
        for (Task task : mDeleteList) {
            int index = mTasks.indexOf(task);
            mTasks.remove(index);
            mLayoutRows.remove(index);
        }

        for (LinearLayout layout : mLayoutRows) {
            setRowBackgroundColor(layout);
        }

        generateDeleteList();
        mActivity.switchToDelete();
        mActivity.displayTable(TaskList.this);
    }

    public void setRowBackgroundColor(LinearLayout layout) {
        if (mLayoutRows.indexOf(layout) % 2 != 0) {
            layout.setBackgroundColor(Color.parseColor("#efefef")); // light gray. Alternating colors for each list row
        } else {
            layout.setBackgroundColor(Color.WHITE);
        }
    }

    public void setDeleteBackgroundColor(LinearLayout layout) {
        if (mLayoutRows.indexOf(layout) % 2 == 0) {
            layout.setBackgroundColor(Color.parseColor("#5994f2"));
        } else {
            layout.setBackgroundColor(Color.parseColor("#2d7eff"));
        }
    }


    public List<Task> getTasks() {

        return mTasks;
    }

    public void addTask(Task task) {
        mTasks.add(task);
    }

}


