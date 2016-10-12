package com.heyde.checklist.model;

/**
 * Created by Daniel on 10/8/2016.
 */

public class Task {
    private String mTaskText;
    private boolean mIsChecked;

    public Task(String taskText, boolean isChecked) {
        mTaskText = taskText;
        mIsChecked = isChecked;
    }

    public String getTaskText() {
        return mTaskText;
    }

    public void setTaskText(String taskText) { // might be used later for an edit task?
        mTaskText = taskText;
    }

    public boolean isChecked() {
        return mIsChecked;
    }

    public void setChecked(boolean checked) {
        mIsChecked = checked;
    }
}
