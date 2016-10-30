package com.heyde.checklist.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.menu.ActionMenuItemView;
import android.support.v7.widget.PopupMenu;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.heyde.checklist.R;
import com.heyde.checklist.model.FileController;
import com.heyde.checklist.model.Task;
import com.heyde.checklist.model.TaskList;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.R.drawable.ic_menu_add;
import static android.R.drawable.ic_menu_delete;
import static android.R.drawable.ic_menu_edit;


public class MainActivity extends AppCompatActivity {

    @BindView(R.id.activity_main)
    RelativeLayout mMainLayout;
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

    private TextView mTitleText;
    private boolean mTasksEditable = false;
    private TaskList mWorkingList;
    private Context mContext;
    private FileController mFileController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mContext = this;

        getSupportActionBar().setCustomView(R.layout.listname_actionbar);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        LayoutInflater layoutInflater = getLayoutInflater();
        layoutInflater.inflate(R.layout.listname_actionbar, null);
        mTitleText = (TextView) findViewById(R.id.titleEditText);


        mWorkingList = new TaskList(this, mTaskTable);
        mFileController = new FileController(this);
        // TODO editlist menu, delete list elements, delete list
        // TODO Pencil? Edit, FOR THIS USE CONTEXTUAL ACTION MODE
        // todo Trash Can? Delete list items/ delete list(if in list activity) use snackbar to show deleted
        // where to create new tasklist?
        // TODO save onpause and onclose

        mRefreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mWorkingList.refreshList();
                mFileController.saveList(mWorkingList);
            }
        });
        mAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTask();
            }
        });
        mTitleText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTitle();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_button, menu);
        return true;
    }

    private void setTitle() {

        // http://stackoverflow.com/questions/10903754/input-text-dialog-android
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Set a Title");

        final EditText input = new EditText(this);
        // specify input type
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input); // takes builder, tells it to show the edittext that we're now editing


        builder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String newText = input.getText().toString();
                mWorkingList.setName(newText);
                mFileController.saveList(mWorkingList);
                mTitleText.setText(mWorkingList.getName());
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();// closes pop up dialog without input
            }
        });

        AlertDialog alertDialog = builder.create();

        alertDialog.show();

        Button posButton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
        posButton.setTextColor(Color.GREEN);

        Button negButton = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
        negButton.setTextColor(Color.RED);

    }


    private void addTask() {

        // http://stackoverflow.com/questions/10903754/input-text-dialog-android
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add a New Task");

        final EditText input = new EditText(this);
        // specify input type
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input); // takes builder, tells it to show the edittext that we're now editing


        builder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String newText = input.getText().toString();
                Task newTask = new Task(newText, false, mContext, mReferenceText, mReferenceButton);
                mWorkingList.addTask(newTask);
                mWorkingList.makeNewLine(newTask);
                displayTable(mWorkingList);
                mFileController.saveList(mWorkingList);
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();// closes pop up dialog without input
            }
        });

        AlertDialog alertDialog = builder.create();

        alertDialog.show();

        Button posButton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
        posButton.setTextColor(Color.GREEN);

        Button negButton = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
        negButton.setTextColor(Color.RED);

    }

    private void displayTable(TaskList taskList) {
        mTaskTable.removeAllViews();
        for (TableRow row : taskList.getTableRows()) {
            mTaskTable.addView(row);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_myLists:

                if (mTasksEditable == true) {
                    toggleEditMode();
                }

                View listButtonView = findViewById(R.id.action_myLists);

                PopupMenu popup = new PopupMenu(this, listButtonView);
                for (String filename : mFileController.getAvailableFiles()){
                    popup.getMenu().add(filename);
                }
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        mFileController.saveList(mWorkingList); // save old list before rewriting table
                        String itemName = item.getTitle().toString();
                        ArrayList<String> taskList = new ArrayList<>(mFileController.loadFile(itemName));
                        TaskList newList = new TaskList(mContext, mTaskTable);

                        for(String taskText : taskList) {
                            String[] taskProps;
                            String taskName;
                            boolean isChecked;

                            taskProps = taskText.split(":::");
                            if (taskProps[1].equals("false")){
                                isChecked = false;
                            } else {
                                isChecked = true;
                            }

                            taskName = taskProps[0].trim();

                            Task newTask = new Task(taskName, isChecked, mContext, mReferenceText, mReferenceButton);

                            newList.setName(itemName);
                            newList.addTask(newTask);
                            newList.makeNewLine(newTask);

                        }
                        mWorkingList = newList;
                        mTitleText.setText(mWorkingList.getName());
                        displayTable(mWorkingList);
                        return false;
                    }
                });
                try {
                    popup.show();
                }catch(Exception e){
                    e.printStackTrace();

                }

                return true;

            case R.id.edit_list:
                toggleEditMode();

                return true;

            default:
                return false;
        }
    }

    // The following is used to edit list entries and delete lists
    // Not sure if I should make this its own activity

    private void toggleEditMode(){
        ActionMenuItemView editIcon = (ActionMenuItemView) findViewById(R.id.add_new_list);

        Drawable actionIcon;
        if (!mTasksEditable){ // make things so they can be deleted
            mRefreshButton.setVisibility(View.INVISIBLE);
            mTasksEditable = true;
            for (Task task : mWorkingList.getTasks()) {
                task.prepForDelete();
            }
            if (Build.VERSION.SDK_INT >= 21) {
                actionIcon = mContext.getDrawable(ic_menu_delete);
            }else {
                actionIcon = mContext.getResources().getDrawable(ic_menu_delete);
            }
        } else { // restore things to normal state
            mRefreshButton.setVisibility(View.VISIBLE);
            mTasksEditable = false;
            mWorkingList.checkForRemoval();
            for (Task task : mWorkingList.getTasks()) {
                task.restoreCheckButton();
            }
            if (Build.VERSION.SDK_INT >= 21) {
                actionIcon = mContext.getDrawable(ic_menu_add);
            }else {
                actionIcon = mContext.getResources().getDrawable(ic_menu_add);
            }
        }
        editIcon.setIcon(actionIcon);
        displayTable(mWorkingList);// not deleting deleted list items
    }

}



