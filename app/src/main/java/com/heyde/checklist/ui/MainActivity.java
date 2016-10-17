package com.heyde.checklist.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.heyde.checklist.R;
import com.heyde.checklist.model.Task;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MainActivity extends AppCompatActivity {

    //    @BindView(R.id.checkButton)
//    ImageButton mCheckButton;
    @BindView(R.id.refreshButton)
    Button mRefreshButton;
    @BindView(R.id.addButton)
    ImageButton mAddButton;
    @BindView(R.id.taskTable)
    TableLayout mTaskTable;
    @BindView(R.id.referenceTask)
    TextView mReferenceText;
    @BindView(R.id.referenceButton)
    ImageButton mReferenceButton;

    private List<Task> taskList = new ArrayList<>();
    private Context mContext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mContext = this;
        //TODO future save, editlist menu, delete list elements, delete list, list names

        mRefreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshTasks();
            }
        });
        mAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTask();
            }
        });

    }

    private void refreshTasks() {
        for (Task task : taskList) {
            if (task.isChecked()) {
                task.toggleCheckButton();
            }
        }
    }

    private void makeNewLine(final Task newTaskObject) {
        TableRow newTaskRow = new TableRow(this);
        newTaskRow.setPadding(0, 30, 0, 0);

        // add views to new row
        newTaskRow.addView(newTaskObject.getTextView());
        newTaskRow.addView(newTaskObject.getCheckButton());

        // add new row to table
        ViewGroup.LayoutParams oldTableParams = mTaskTable.getLayoutParams();
        mTaskTable.addView(newTaskRow, new TableLayout.LayoutParams(oldTableParams));
    }


    private void addTask() {

                // http://stackoverflow.com/questions/10903754/input-text-dialog-android
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("New Task");

        final EditText input = new EditText(this);
                // specify input type
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(input); // takes builder, tells it to show the edittext that we're now editing

                builder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String newText = input.getText().toString();
                        Task newTask = new Task(newText, false, mContext, mReferenceText, mReferenceButton);
                        taskList.add(newTask);
                        makeNewLine(newTask);
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();// closes pop up dialog without input
                    }
                });

        builder.show();

             }


}


