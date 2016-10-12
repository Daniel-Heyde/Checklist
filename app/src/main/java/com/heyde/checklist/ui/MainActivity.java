package com.heyde.checklist.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.heyde.checklist.R;


import butterknife.BindView;
import butterknife.ButterKnife;

import static android.R.drawable.checkbox_off_background;


public class MainActivity extends AppCompatActivity {

    @BindView(R.id.checkButton)
    ImageButton mCheckButton;
    @BindView(R.id.refreshButton)
    Button mRefreshButton;
    @BindView(R.id.addButton)
    ImageButton mAddButton;
    @BindView(R.id.taskTable)
    TableLayout mTaskTable;
    @BindView(R.id.sampleTask)
    TextView mSampleTask;


    private String mNewText = "";
    private Boolean mThinking;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mContext = this.getApplicationContext();

        mCheckButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        mRefreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        mAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTask();
            }
        });
    }

    private void addTask() {
        getTaskFromUser();

        Log.i("STATUS", "VIEW ADDED, VALUE" + mNewText);
    }

    private void makeNewLine() {
        TableRow newTaskRow = new TableRow(this);
        ViewGroup.LayoutParams tableParams = mTaskTable.getLayoutParams();
        newTaskRow.setLayoutParams(tableParams);

        ImageButton newCheckButton = new ImageButton(this);
        ViewGroup.LayoutParams buttonParams = mCheckButton.getLayoutParams();
        newCheckButton.setLayoutParams(buttonParams);
        Drawable drawable;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            drawable = getDrawable(checkbox_off_background);
        } else {
            drawable = getResources().getDrawable(checkbox_off_background);
        }
        newCheckButton.setImageDrawable(drawable);

        TextView newTask = new TextView(this);
        ViewGroup.LayoutParams textParams = mSampleTask.getLayoutParams();
        newTask.setLayoutParams(textParams);
        newTask.setText(mNewText);

        newTaskRow.addView(newTask);
        newTaskRow.addView(newCheckButton);

        ViewGroup.LayoutParams oldTableParams = mTaskTable.getLayoutParams();
        mTaskTable.addView(newTaskRow, new TableLayout.LayoutParams(oldTableParams));
    }


    private void getTaskFromUser() {


                // http://stackoverflow.com/questions/10903754/input-text-dialog-android
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle("New Task");

                final EditText input = new EditText(mContext);
                // specify input type
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(input); // takes builder, tells it to show the edittext that we're now editing

                builder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mNewText = input.getText().toString();// FIXME moves on without waiting for user to finish
                        makeNewLine();
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();// closes pop up dialog without input
                    }
                });
             }


}


