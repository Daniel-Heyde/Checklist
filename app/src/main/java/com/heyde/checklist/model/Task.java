package com.heyde.checklist.model;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.TypedValue;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.heyde.checklist.R;

import static android.R.color.transparent;
import static android.R.drawable.ic_menu_edit;

/**
 * Created by Daniel on 10/8/2016.
 */

public class Task {
    private String mTaskText;
    private boolean mIsChecked;
    private Context mContext;
    private TextView mTextView;
    private ImageView mCheckBox;
    private boolean mIsEditable;

    public Task(String taskText, boolean isChecked, Context context, TextView referenceTextView, ImageButton referenceImageButton) {
        mTaskText = taskText;
        mIsChecked = isChecked;
        mContext = context;
        createTextView(referenceTextView);
        createImageButton(referenceImageButton);
    }

    public TextView getTextView() {
        return mTextView;
    }

    public String taskToString(){
        return(mTaskText+ ":::" + mIsChecked);
    }

    public boolean isEditable() {
        return mIsEditable;
    }

    private void createImageButton(ImageButton referenceImageButton) {
        final ImageView newCheckBox = new ImageButton(mContext);
        newCheckBox.setLayoutParams(referenceImageButton.getLayoutParams());
        newCheckBox.setPadding(0,0,15,0);
        Drawable checkbox;
            if (!isChecked()) {
                checkbox = ContextCompat.getDrawable(mContext, R.drawable.checkbox_off);
            } else {
                checkbox = ContextCompat.getDrawable(mContext, R.drawable.checkbox_on);
            }
        newCheckBox.setImageDrawable(checkbox);
        newCheckBox.setBackgroundColor(ContextCompat.getColor(mContext, transparent));

        mCheckBox = newCheckBox;
    }

    public void toggleCheckButton() {

        Drawable checkbox;
        if (!mIsChecked) {
            checkbox = ContextCompat.getDrawable(mContext, R.drawable.checkbox_on);
            mIsChecked = true;
        } else {
            checkbox = ContextCompat.getDrawable(mContext, R.drawable.checkbox_off);
            mIsChecked = false;
        }
        mCheckBox.setImageDrawable(checkbox);
    }

    private void createTextView(TextView referenceTextView) {

        TextView newTaskText = new TextView(mContext);
        newTaskText.setMaxWidth(100);
        newTaskText.setLayoutParams(referenceTextView.getLayoutParams());
        newTaskText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        newTaskText.setText(mTaskText);
        mTextView = newTaskText;

    }

    public void restoreCheckButton() {

        Drawable checkbox;
        if (!mIsChecked) {
            checkbox = ContextCompat.getDrawable(mContext, R.drawable.checkbox_off);
        } else {
            checkbox = ContextCompat.getDrawable(mContext, R.drawable.checkbox_on);
        }
        mIsEditable = false;
        mCheckBox.setImageDrawable(checkbox);
        mTextView.setTextColor(Color.parseColor("#8a000000"));
    }


    public void prepForDelete() {
        mIsEditable = true;
        mCheckBox.setImageDrawable(ContextCompat.getDrawable(mContext, ic_menu_edit));
        mTextView.setTextColor(Color.WHITE);
    }

    public ImageView getCheckBox() {
        return mCheckBox;
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
}
